package com.parsleyj.dawrio.daw.element

import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort

fun Voice.VoiceUpdater.sinOsc(
    label: String,
    description: String = "",
):SinOsc = addElement(SinOsc(label, description))

class SinOsc internal constructor(
    label: String,
    description: String = "",
    handle: ElementHandle = ElementHandle(createSinOsc()),
) : Element(label, description, handle) {
    override val allInputs: List<ElementInPort>
        get() = listOf(inFrequency)

    override val allOutputs: List<ElementOutPort>
        get() = listOf(outAudioL, outAudioR)

    val outAudioL: ElementOutPort
        get() = ElementOutPort(this, "audioL", 0)
    val outAudioR: ElementOutPort
        get() = ElementOutPort(this, "audioR", 1)
    val inFrequency: ElementInPort
        get() = ElementInPort(this, "frequency", 0)
}

