package com.parsleyj.dawrio.daw


@JvmInline
value class ElementHandle(val toAddress: Long)

fun Long.toElementHandle(): ElementHandle {
    return ElementHandle(this)
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
    val element: Element,
    val portName: String,
    val portNumber: Int,
) {
    fun getValue(): Float {
        return Element.readElementOutput(element.handle.toAddress, portNumber)
    }

    fun connectionTo(inPort: InPort): Route {
        return Route(this, inPort)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OutPort) return false

        if (portNumber != other.portNumber) return false
        if (element.handle.toAddress != other.element.handle.toAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = portNumber
        result = 31 * result + element.handle.toAddress.hashCode()
        return result
    }

    override fun toString() = "${this.element.label}/${this.portName}"
    fun findRoutes(allRoutes: List<Route>): List<Route> = allRoutes.filter { this == it.outPort }


}

class InPort(
    val element: Element,
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
        if (element.handle.toAddress != other.element.handle.toAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = portNumber
        result = 31 * result + element.handle.toAddress.hashCode()
        return result
    }

    override fun toString() = "${this.element.label}/${this.portName}"
}

@JvmInline
value class RouteHandle(val toAddress: Long)

class Route(
    val outPort: OutPort,
    val inPort: InPort,
    val handle: RouteHandle = RouteHandle(
        createRoute(
            outPort.element.handle.toAddress,
            outPort.portNumber,
            inPort.element.handle.toAddress,
            inPort.portNumber
        )
    )
) {

    val inElement: Element get() = inPort.element
    val outElement: Element get() = outPort.element
    val inElementHandle: ElementHandle get() = inElement.handle
    val outElementHandle: ElementHandle get() = outElement.handle
    val inElementAddress: Long = inElementHandle.toAddress
    val outElementAddress: Long = outElementHandle.toAddress
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

abstract class Element protected constructor(
    val label: String,
    val description: String = "",
    val handle: ElementHandle
) {

    abstract val type: DeviceType
    abstract val allInputs: List<InPort>
    abstract val allOutputs: List<OutPort>

    fun destroy() {
        destroy(handle.toAddress)
    }


    companion object {
        external fun readElementOutput(deviceAddress: Long, portNumber: Int): Float
        external fun destroy(deviceAddress: Long)
        external fun createSinOsc(): Long
        external fun createSawOsc(): Long
        external fun createSquareOsc(): Long
        external fun createLFO(): Long
        external fun createVolume(amount: Float): Long
        external fun createConstEmitter(value: Float): Long
    }
}

