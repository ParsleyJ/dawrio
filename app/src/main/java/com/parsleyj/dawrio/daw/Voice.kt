package com.parsleyj.dawrio.daw

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@JvmInline
value class VoiceHandle(val toAddress: Long)

class Voice(val handle: VoiceHandle = VoiceHandle(createVoice())) {


    private val devicesPrivate: MutableMap<DeviceHandle, Device> = mutableMapOf()
    val devices: List<Device>
        get() = devicesPrivate.values.toList()


    private val routesPrivate: MutableMap<RouteHandle, Route> = mutableMapOf()
    val routes: List<Route>
        get() = routesPrivate.values.toList()

    fun start() {
        startVoice(handle.toAddress)
    }

    fun stop() {
        stopVoice(handle.toAddress)
    }

    fun destroy() {
        destroyVoice(handle.toAddress)
    }

    private fun setRouteWithoutUpdating(route: Route) {
        val found = this.routes.find { it.inDevice == route.inDevice && it.inPort == route.inPort }
        if (found != null) {
            this.routesPrivate.remove(found.handle)
        }
        this.routesPrivate[route.handle] = route
    }

    private fun setDeviceWithoutUpdating(device: Device) {
        this.devicesPrivate[device.handle] = device
    }

    fun addDevice(dev: Device) {
        setDeviceWithoutUpdating(dev)
        commitNativeLayout()
    }

    fun addRoute(route: Route) {
        setRouteWithoutUpdating(route)
        commitNativeLayout()
    }

    fun removeDevice(dev: Device) {
        removeDevice(dev.handle)
        commitNativeLayout()
    }

    fun removeDevice(handle: DeviceHandle) {
        this.devicesPrivate.remove(handle)
        commitNativeLayout()
    }

    fun removeRoute(dev: Route) {
        removeRoute(dev.handle)
        commitNativeLayout()
    }

    fun removeRoute(handle: RouteHandle) {
        this.routesPrivate.remove(handle)
        commitNativeLayout()
    }



    interface VoiceUpdater {
        val voice:Voice
        fun InPort.connect(outPort: OutPort): Route
        fun OutPort.connect(inPort: InPort): Route
        fun <T : Device> addDevice(device: T): T
    }


    fun edit(block: VoiceUpdater.() -> Unit): Voice {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        val builder = object : VoiceUpdater {
            override val voice: Voice
                get() = this@Voice

            override fun InPort.connect(outPort: OutPort): Route {
                val result = this.connectionTo(outPort)
                setRouteWithoutUpdating(result)
                return result
            }

            override fun OutPort.connect(inPort: InPort): Route {
                val result = this.connectionTo(inPort)
                setRouteWithoutUpdating(result)
                return result
            }

            override fun <T : Device> addDevice(device: T): T {
                setDeviceWithoutUpdating(device)
                return device
            }
        }
        block(builder)
        commitNativeLayout()
        return this
    }


    private fun commitNativeLayout() {
        updateNativeLayout(
            handle.toAddress,
            devices.map { it.handle.toAddress }.toLongArray(),
            routes.map { it.handle.toAddress }.toLongArray(),
            if (devices.isEmpty()) 0L else devices.last().handle.toAddress
        )
    }

    fun updateRoute(source: OutPort?, input: InPort) {
        if (source == null) {
            val toBeRemoved = ArrayList<RouteHandle>(routesPrivate.size)
            routes.filter {
                it.inDevice == input.device.handle && it.inPort == input.portNumber
            }.map {
                it.handle
            }.forEach(toBeRemoved::add)
            toBeRemoved.forEach(routesPrivate::remove)
        } else {
            setRouteWithoutUpdating(source.connectionTo(input))
        }
        commitNativeLayout()
    }

    companion object {
        private external fun createVoice(): Long
        private external fun startVoice(address: Long)
        private external fun stopVoice(address: Long)
        private external fun destroyVoice(address: Long)
        private external fun getDevicesCount(addr: Long): Int
        private external fun getDevices(addr: Long, resultArray: LongArray)
        private external fun getRoutesCount(addr: Long): Int
        private external fun getRoutes(addr: Long, resultArray: LongArray)
        private external fun getOutDevice(addr: Long): Long
        private external fun updateNativeLayout(
            address: Long,
            devices: LongArray,
            routes: LongArray,
            outDevice: Long
        )
    }
}
