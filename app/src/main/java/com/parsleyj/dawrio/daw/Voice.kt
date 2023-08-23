package com.parsleyj.dawrio.daw

import android.util.Log
import com.parsleyj.dawrio.Engine
import com.parsleyj.dawrio.daw.device.Connection
import com.parsleyj.dawrio.daw.device.ConnectionID
import com.parsleyj.dawrio.daw.device.Device
import com.parsleyj.dawrio.daw.device.DeviceInput
import com.parsleyj.dawrio.daw.device.DeviceOutput
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.element.ElementHandle
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.daw.elementroute.RouteHandle
import java.util.UUID
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@JvmInline
value class VoiceHandle(val toAddress: Long)

class Voice(val handle: VoiceHandle = VoiceHandle(createVoice())) {

    private val _devices: MutableList<Device> = mutableListOf()
    val devices: List<Device> get() = _devices.toList()

    private val _customConnections: MutableMap<ConnectionID, Connection> = mutableMapOf()
    val customConnections: Map<ConnectionID, Connection> = _customConnections

    val connectionsList: List<Connection> get() = customConnections.values.toList()

    val allRoutes: List<Route>
        get() = _customConnections.values.flatMap { it.routes } +
                devices.flatMap { it.internalRoutes }

    private var mainOutputDevice: Device? = null

    private val routesTrashBin = mutableListOf<Route>()
    private val elementsTrashBin = mutableListOf<Element>()


    fun start() {
        startVoice(handle.toAddress)
    }

    fun stop() {
        stopVoice(handle.toAddress)
    }

    fun destroy() {
        destroyVoice(handle.toAddress)
    }


    private fun addCustomConnectionWithoutCommit(connection: Connection) {
        //only one connection for each input
        val incompatible = _customConnections.values.filter { it.to.id == connection.to.id }
        incompatible.forEach { deleteCustomConnectionWithoutCommit(it) }

        _customConnections[connection.id] = connection //There are no new routes!
    }

    private fun deleteCustomConnectionWithoutCommit(connection: Connection) {
        val old = _customConnections.remove(connection.id)
        old?.routes?.let { routesTrashBin.addAll(it) }
    }


    private fun addDeviceWithoutCommit(device: Device, position: Int = lastDevicePosition) {

        val safePosition = if (position < 0 || position >= devices.size) {
            devices.size
        } else {
            position
        }

        if (safePosition >= devices.size) {
            _devices.add(device)
        } else {
            _devices.add(safePosition, device)
        }
    }

    private fun deleteDeviceWithoutCommit(device: Device) {
        elementsTrashBin.addAll(device.allElements)
        routesTrashBin.addAll(device.internalRoutes)
        _devices.removeIf { device.id == it.id }
    }

    private fun moveDeviceWithoutCommit(from: Int, to: Int) {
        val removed = _devices.removeAt(from)
        _devices.add(to, removed)
    }


    inner class VoiceEditingScope {
        fun <T : Device> addDevice(position: Int = lastDevicePosition, getDevice: () -> T): T {
            val device = getDevice()
            addDeviceWithoutCommit(device, position)
            return device
        }

        fun DeviceOutput.connectTo(input: DeviceInput): Connection? {
            val connection = input.buildConnection(this)
            connection?.let { addCustomConnectionWithoutCommit(it) }
            return connection
        }

        fun DeviceInput.connectTo(output: DeviceOutput): Connection? {
            return output.connectTo(this)
        }

        fun updateConnection(source: DeviceOutput?, sink: DeviceInput) {
            if (source == null) {
                sink.findConnection(customConnections.values.toList())?.let {
                    deleteCustomConnectionWithoutCommit(it)
                }
                return
            }

            val newConnection = sink.buildConnection(source)
            newConnection?.let {
                addCustomConnectionWithoutCommit(it)
            }
        }

        fun moveDevice(from: Int, to: Int) {
            moveDeviceWithoutCommit(from, to)
        }

        fun deleteDevice(device: Device) {
            deleteDeviceWithoutCommit(device)
        }


        fun setMainOutput(device: Device) {
            this@Voice.mainOutputDevice = device
        }

    }


    fun edit(block: VoiceEditingScope.() -> Unit): Voice {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        block(VoiceEditingScope())
        commitNativeLayout()
        return this
    }


    private fun commitNativeLayout() {
        val routesAddresses = allRoutes
            .map { it.handle.toAddress }
            .toLongArray()
        val elementAddresses = devices
            .flatMap { it.allElements }
            .map { it.handle.toAddress }
            .toLongArray()
        val outElementAddress = mainOutputDevice?.mainAudioOutputElement?.handle?.toAddress ?: 0L

        Log.d("Voice", "Out element address is $outElementAddress")

        val wasOn = Engine.isSoundOn()

        if(wasOn) {
            Engine.setSoundOn(false)
        }
        updateNativeLayout(
            handle.toAddress,
            elementAddresses,
            routesAddresses,
            outElementAddress
        )
        if(wasOn) {
            Engine.setSoundOn(true)
        }
        removeGarbage(routesAddresses, elementAddresses, outElementAddress)
    }

    private fun removeGarbage(
        routesAddresses: LongArray,
        elementAddresses: LongArray,
        outElementAddress: Long
    ) {
        val deletedElements = mutableListOf<ElementHandle>()
        elementsTrashBin
            .filter {
                it.handle.toAddress !in elementAddresses
                        && it.handle.toAddress != outElementAddress
            }
            .distinctBy { it.handle }
            .forEach {
                Log.d("Voice", "Destroying element: $it")
                destroyElement(it.handle.toAddress)
                deletedElements.add(it.handle)
            }

        val deletedRoutes = mutableListOf<RouteHandle>()
        routesTrashBin
            .filter { it.handle.toAddress !in routesAddresses }
            .distinctBy { it.handle }
            .forEach {
                Log.d("Voice", "Destroying route: $it")
                destroyRoute(it.handle.toAddress)
                deletedRoutes.add(it.handle)
            }

        elementsTrashBin.removeIf { it.handle in deletedElements }
        routesTrashBin.removeIf { it.handle in deletedRoutes }
    }


    companion object {
        const val lastDevicePosition: Int = -1

        private external fun createVoice(): Long
        private external fun startVoice(address: Long)
        private external fun stopVoice(address: Long)
        private external fun destroyVoice(address: Long)
        private external fun getElementCount(addr: Long): Int
        private external fun getElements(addr: Long, resultArray: LongArray)
        private external fun getRoutesCount(addr: Long): Int
        private external fun getRoutes(addr: Long, resultArray: LongArray)
        private external fun getOutElement(addr: Long): Long
        private external fun updateNativeLayout(
            address: Long,
            elements: LongArray,
            routes: LongArray,
            outElement: Long
        )

        private external fun destroyRoute(addr: Long)
        private external fun destroyElement(addr: Long)
    }
}
