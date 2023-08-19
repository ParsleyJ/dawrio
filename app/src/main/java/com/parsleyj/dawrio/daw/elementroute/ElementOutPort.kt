package com.parsleyj.dawrio.daw.elementroute

import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.element.Element

class ElementOutPort(
    val element: Element,
    val portName: String,
    val portNumber: Int,
    val valueFormat: ValueFormat = ValueFormat.Decimal(1)
) {
    fun readValue(): Float {
        return Element.readElementOutput(element.handle.toAddress, portNumber)
    }

    fun connectionTo(inPort: ElementInPort): Route {
        return Route(this, inPort)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ElementOutPort) return false

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