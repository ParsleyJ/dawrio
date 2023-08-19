package com.parsleyj.dawrio.daw.elementroute

import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.element.Element

class ElementInPort(
    val element: Element,
    val portName: String,
    val portNumber: Int,
    val streamFormat: ValueFormat = ValueFormat.Numeric(1)
) {
    fun connectionTo(outPort: ElementOutPort): Route {
        return Route(outPort, this)
    }

    fun readValue(routes: List<Route>): Float? = findRoute(routes)?.outPort?.readValue()

    fun findRoute(routes: List<Route>): Route? = routes.find { this == it.inPort }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ElementInPort) return false

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