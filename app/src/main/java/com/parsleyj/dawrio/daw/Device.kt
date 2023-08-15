package com.parsleyj.dawrio.daw


@JvmInline
value class DeviceHandle(val toAddress: Long)

fun Long.toDevice(): DeviceHandle {
    return DeviceHandle(this)
}


class OutPort(val device: Device, val portName: String, val portNumber: Int) {
    fun getValue(): Float {
        return Device.readDeviceOutput(device.handle.toAddress, portNumber)
    }

    fun connectionTo(inPort: InPort): Route {
        return Route(
            this.device.handle,
            this.portNumber,
            inPort.device.handle,
            inPort.portNumber
        )
    }
}

class InPort(val device: Device, val portName: String, val portNumber: Int) {
    fun connectionTo(outPort: OutPort): Route {
        return Route(
            outPort.device.handle,
            outPort.portNumber,
            this.device.handle,
            this.portNumber,
        )
    }
}

@JvmInline
value class RouteHandle(val toAddress: Long)

class Route(
    val outDevice: DeviceHandle,
    val outPort: Int,
    val inDevice: DeviceHandle,
    val inPort: Int,
    val handle: RouteHandle = RouteHandle(
        createRoute(
            outDevice.toAddress,
            outPort,
            inDevice.toAddress,
            inPort
        )
    )
) {
    companion object {
        private external fun createRoute(
            outDevice: Long,
            outPort: Int,
            inDevice: Long,
            inPort: Int,
        ): Long
    }
}

enum class DeviceType {
    Generator, Effect
}

abstract class Device protected constructor(
    val label: String,
    val description: String = "",
    val handle: DeviceHandle
) {

    abstract val type: DeviceType

    fun destroy() {
        destroy(handle.toAddress)
    }

    companion object {
        external fun readDeviceOutput(deviceAddress: Long, portNumber: Int): Float
        external fun destroy(deviceAddress: Long)
        external fun createSinOsc(): Long
        external fun createSawOsc(): Long
        external fun createSquareOsc(): Long
        external fun createLFO(): Long
        external fun createVolume(amount: Float): Long
        external fun createConstEmitter(value: Float): Long
    }
}

