package com.parsleyj.dawrio.daw.element

import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort


class Volume(
    label: String,
    initialAmount: Float,
    description: String = "",
    handle: ElementHandle = ElementHandle(createVolume(initialAmount))
) : Element(label, description, handle) {
    override val allInputs: List<ElementInPort>
        get() = listOf(inAudioL, inAudioR, inAmount)

    override val allOutputs: List<ElementOutPort>
        get() = listOf(outAudioL, outAudioR)


    val outAudioL: ElementOutPort
        get() = ElementOutPort(this, "audioL", 0)
    val outAudioR: ElementOutPort
        get() = ElementOutPort(this, "audioR", 1)

    val inAudioL: ElementInPort
        get() = ElementInPort(this, "inAudioL", 0)
    val inAudioR: ElementInPort
        get() = ElementInPort(this, "inAudioR", 1)
    val inAmount = ElementInPort(this, "amount", 2)

    var amount: Float
        get() = getAmount(handle.toAddress)
        set(value) = setAmount(handle.toAddress, value)


    companion object {
        private external fun getAmount(addr: Long): Float
        private external fun setAmount(addr: Long, amount: Float)
    }


}