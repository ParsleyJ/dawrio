package com.parsleyj.dawrio.daw.elements

import com.parsleyj.dawrio.daw.Element
import com.parsleyj.dawrio.daw.ElementHandle
import com.parsleyj.dawrio.daw.DeviceType
import com.parsleyj.dawrio.daw.InPort
import com.parsleyj.dawrio.daw.OutPort
import com.parsleyj.dawrio.daw.Voice

fun Voice.VoiceUpdater.volume(
    initialAmount: Float,
    label: String,
    description: String = "",
):Volume = addDevice(Volume(initialAmount, label, description))

class Volume(
    initialAmount: Float,
    label: String,
    description: String = "",
    handle: ElementHandle = ElementHandle(createVolume(initialAmount))
) : Element(label, description, handle) {
    override val type: DeviceType
        get() = DeviceType.Effect
    override val allInputs: List<InPort>
        get() = listOf(inAudioL, inAudioR, inAmount)

    override val allOutputs: List<OutPort>
        get() = listOf(outAudioL, outAudioR)


    val outAudioL: OutPort
        get() = OutPort(this, "audioL", 0)
    val outAudioR: OutPort
        get() = OutPort(this, "audioR", 1)

    val inAudioL: InPort
        get() = InPort(this, "inAudioL", 0)
    val inAudioR: InPort
        get() = InPort(this, "inAudioR", 1)
    val inAmount = InPort(this, "amount", 2)

    var amount: Float
        get() = getAmount(handle.toAddress)
        set(value) = setAmount(handle.toAddress, value)


    companion object {
        private external fun getAmount(addr: Long): Float
        private external fun setAmount(addr: Long, amount: Float)
    }


}