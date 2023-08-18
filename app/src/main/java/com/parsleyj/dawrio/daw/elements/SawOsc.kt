package com.parsleyj.dawrio.daw.elements

import com.parsleyj.dawrio.daw.Element
import com.parsleyj.dawrio.daw.ElementHandle
import com.parsleyj.dawrio.daw.DeviceType
import com.parsleyj.dawrio.daw.InPort
import com.parsleyj.dawrio.daw.OutPort
import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.Voice

fun Voice.VoiceUpdater.sawOsc(
    label: String,
    description: String = "",
):SawOsc = addDevice(SawOsc(label, description))

class SawOsc internal constructor(
    label:String,
    description: String = "",
    handle: ElementHandle = ElementHandle(createSawOsc()),
): Element(label, description, handle) {
    override val type: DeviceType
        get() = DeviceType.Generator
    override val allInputs: List<InPort>
        get() = listOf(inFrequency)

    override val allOutputs: List<OutPort>
        get() = listOf(outAudioL, outAudioR)

    val outAudioL: OutPort
        get() = OutPort(this, "audioL", 0)
    val outAudioR: OutPort
        get() = OutPort(this, "audioR", 1)
    val inFrequency: InPort
        get() = InPort(this, "frequency", 0, ValueFormat.Frequency)
}