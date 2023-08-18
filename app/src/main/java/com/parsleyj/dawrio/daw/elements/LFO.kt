package com.parsleyj.dawrio.daw.elements

import com.parsleyj.dawrio.daw.Element
import com.parsleyj.dawrio.daw.ElementHandle
import com.parsleyj.dawrio.daw.DeviceType
import com.parsleyj.dawrio.daw.InPort
import com.parsleyj.dawrio.daw.OutPort
import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.Voice

fun Voice.VoiceUpdater.lfo(
    label: String,
    description: String = "",
):LFO = addDevice(LFO(label, description))

class LFO(
    label:String,
    description: String = "",
    handle: ElementHandle = ElementHandle(createLFO()),
) : Element(label, description, handle) {
    override val type: DeviceType
        get() = DeviceType.Generator

    override val allInputs: List<InPort>
        get() = listOf(inFrequency, inType, inMinimum, inMaximum)

    override val allOutputs: List<OutPort>
        get() = listOf(outValue)


    val outValue: OutPort
        get() = OutPort(this, "outVal", 0)

    val inFrequency: InPort
        get() = InPort(this, "freq", 0, ValueFormat.Frequency)

    val inType: InPort
        get() = InPort(this, "wave", 1)

    val inMinimum: InPort
        get() = InPort(this, "minVal", 2)

    val inMaximum: InPort
        get() = InPort(this, "maxVal", 3)
}