package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.Device
import com.parsleyj.dawrio.daw.DeviceHandle
import com.parsleyj.dawrio.daw.DeviceType
import com.parsleyj.dawrio.daw.InPort
import com.parsleyj.dawrio.daw.OutPort
import com.parsleyj.dawrio.daw.Voice

fun Voice.VoiceUpdater.sinOsc(
    label: String,
    description: String = "",
):SinOsc = addDevice(SinOsc(label, description))

class SinOsc internal constructor(
    label: String,
    description: String = "",
    handle: DeviceHandle = DeviceHandle(createSinOsc()),
) : Device(label, description, handle) {
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
        get() = InPort(this, "frequency", 0)
}

