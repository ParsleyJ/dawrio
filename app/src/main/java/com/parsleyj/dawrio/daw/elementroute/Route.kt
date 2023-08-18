package com.parsleyj.dawrio.daw.elementroute

import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.element.ElementHandle

class Route(
    val outPort: ElementOutPort,
    val inPort: ElementInPort,
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
            outElement: Long,
            outPort: Int,
            inElement: Long,
            inPort: Int,
        ): Long
    }
}