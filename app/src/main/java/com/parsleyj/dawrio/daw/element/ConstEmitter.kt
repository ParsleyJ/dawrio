package com.parsleyj.dawrio.daw.element

import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort

fun Voice.VoiceUpdater.constEmitter(
    initialValue: Float,
    label: String,
    description: String = "",
):ConstEmitter = addElement(ConstEmitter(initialValue, label, description))

class ConstEmitter(
    initialValue: Float,
    label: String,
    description: String = "",
    handle: ElementHandle = ElementHandle(createConstEmitter(initialValue))
) : Element(label, description, handle) {
    override val allInputs: List<ElementInPort>
        get() = listOf()

    override val allOutputs: List<ElementOutPort>
        get() = listOf(outPort)

    val outPort: ElementOutPort
        get() = ElementOutPort(this, "constant", 0)

    var value: Float
        get() = getValue(handle.toAddress)
        set(value) = setValue(handle.toAddress, value)

    companion object {
        private external fun setValue(handle: Long, float: Float)
        private external fun getValue(handle: Long): Float
    }
}