package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.Device
import com.parsleyj.dawrio.daw.DeviceHandle
import com.parsleyj.dawrio.daw.DeviceType
import com.parsleyj.dawrio.daw.InPort
import com.parsleyj.dawrio.daw.OutPort

class SawOsc internal constructor(
    label:String,
    description: String = "",
    handle: DeviceHandle = DeviceHandle(createSawOsc()),
): Device(label, description, handle) {
    override val type: DeviceType
        get() = DeviceType.Generator

    val outAudioL: OutPort
        get() = OutPort(this, "audioL", 0)
    val outAudioR: OutPort
        get() = OutPort(this, "audioR", 1)
    val inFrequency: InPort
        get() = InPort(this, "frequency", 0)
}