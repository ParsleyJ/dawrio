package com.parsleyj.dawrio.daw


@JvmInline
value class DeviceHandle(val toAddress: Long)

fun Long.toDevice(): DeviceHandle {
    return DeviceHandle(this)
}

fun Float.scaleIn(range: ClosedFloatingPointRange<Float>): Float {
    val rangeExtent = range.endInclusive - range.start
    return (this - range.start) / rangeExtent
}

sealed class ValueFormat(val convertToString: (v: Float) -> String) {
    class Percent(min: Float, max: Float) : ValueFormat({
        String.format("%.1f%", it.scaleIn(min..max))
    })

    object Frequency : ValueFormat({ String.format("%.1f Hz", it) })

    class Decimal(val decimals: Int) : ValueFormat({ String.format("%.${decimals}f", it) })

    //TODO enum
}



class OutPort(
    val device: Device,
    val portName: String,
    val portNumber: Int,
) {
    fun getValue(): Float {
        return Device.readDeviceOutput(device.handle.toAddress, portNumber)
    }

    fun connectionTo(inPort: InPort): Route {
        return Route(this, inPort)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OutPort) return false

        if (portNumber != other.portNumber) return false
        if (device.handle.toAddress != other.device.handle.toAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = portNumber
        result = 31 * result + device.handle.toAddress.hashCode()
        return result
    }

    override fun toString() = "${this.device.label}/${this.portName}"
    fun findRoutes(allRoutes: List<Route>): List<Route> = allRoutes.filter { this == it.outPort }


}

class InPort(
    val device: Device,
    val portName: String,
    val portNumber: Int,
    val streamFormat: ValueFormat = ValueFormat.Decimal(1)
) {
    fun connectionTo(outPort: OutPort): Route {
        return Route(outPort, this)
    }

    fun findRoute(routes: List<Route>): Route? = routes.find { this == it.inPort }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InPort) return false

        if (portNumber != other.portNumber) return false
        if (device.handle.toAddress != other.device.handle.toAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = portNumber
        result = 31 * result + device.handle.toAddress.hashCode()
        return result
    }

    override fun toString() = "${this.device.label}/${this.portName}"
}

@JvmInline
value class RouteHandle(val toAddress: Long)

class Route(
    val outPort: OutPort,
    val inPort: InPort,
    val handle: RouteHandle = RouteHandle(
        createRoute(
            outPort.device.handle.toAddress,
            outPort.portNumber,
            inPort.device.handle.toAddress,
            inPort.portNumber
        )
    )
) {

    val inDevice: Device get() = inPort.device
    val outDevice: Device get() = outPort.device
    val inDeviceHandle: DeviceHandle get() = inDevice.handle
    val outDeviceHandle: DeviceHandle get() = outDevice.handle
    val inDeviceAddress: Long = inDeviceHandle.toAddress
    val outDeviceAddress: Long = outDeviceHandle.toAddress
    val inPortNumber: Int = inPort.portNumber
    val outPortNumber: Int = outPort.portNumber


    override fun toString() = "$outPort -> $inPort"
    fun destroy() {
        //TODO destroy
    }

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
    abstract val allInputs: List<InPort>
    abstract val allOutputs: List<OutPort>

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

