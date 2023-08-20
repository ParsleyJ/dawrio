package com.parsleyj.dawrio.daw

import com.parsleyj.dawrio.daw.device.Connection
import com.parsleyj.dawrio.daw.device.Device
import com.parsleyj.dawrio.daw.device.DeviceInput
import com.parsleyj.dawrio.daw.device.DeviceOutput
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
    val devices: List<Device> get() = _devices

    private val _customConnections: MutableMap<UUID, Connection> = mutableMapOf()
    val customConnections: Map<UUID, Connection> = _customConnections

    val connectionsList: List<Connection> get() = customConnections.values.toList()

    val allRoutes: List<Route>
        get() = _customConnections.values.flatMap { it.routes } +
                devices.flatMap { it.internalRoutes }

    private val routesTrashBin = mutableListOf<RouteHandle>()
    private val elementsTrashBin = mutableListOf<ElementHandle>()

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
        old?.routes?.map { it.handle }?.let { routesTrashBin.addAll(it) }
    }


    private fun addDeviceWithoutCommit(device: Device, position: Int = -1) {

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
        elementsTrashBin.addAll(device.allElements.map { it.handle })
        routesTrashBin.addAll(device.internalRoutes.map { it.handle })
        _devices.removeIf { device.id == it.id }
    }

    private fun moveDeviceWithoutCommit(from: Int, to: Int) {
        val removed = _devices.removeAt(from)
        _devices.add(to, removed)
    }


    inner class VoiceEditingScope {
        fun <T : Device> addDevice(position: Int = -1, getDevice: () -> T): T {
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
        updateNativeLayout(
            handle.toAddress,
            devices.flatMap { it.allElements }.map { it.handle.toAddress }.toLongArray(),
            allRoutes.map { it.handle.toAddress }.toLongArray(),
            devices.lastOrNull()?.mainAudioOutputElement?.handle?.toAddress ?: 0L
        )
//        routesTrashBin.forEach { destroyRoute(it.toAddress) }
//        elementsTrashBin.forEach { destroyElement(it.toAddress) }
    }


    companion object {
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
