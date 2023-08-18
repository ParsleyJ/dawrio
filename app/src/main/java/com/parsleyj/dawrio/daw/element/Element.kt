package com.parsleyj.dawrio.daw.element

import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort


abstract class Element protected constructor(
    val label: String,
    val description: String = "",
    val handle: ElementHandle
) {

    abstract val allInputs: List<ElementInPort>
    abstract val allOutputs: List<ElementOutPort>

    fun destroy() {
        destroy(handle.toAddress)
    }


    companion object {
        external fun readElementOutput(elementAddress: Long, portNumber: Int): Float
        external fun destroy(elementAddress: Long)
        external fun createSinOsc(): Long
        external fun createSawOsc(): Long
        external fun createSquareOsc(): Long
        external fun createLFO(): Long
        external fun createVolume(amount: Float): Long
        external fun createConstEmitter(value: Float): Long
    }
}

