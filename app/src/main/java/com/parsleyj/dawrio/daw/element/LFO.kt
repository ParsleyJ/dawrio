package com.parsleyj.dawrio.daw.element

import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort

enum class LFOWaveTypes{
    Sin, SawUp, SawDown, Square //Match with enum in LFO.h!
}

class LFO(
    label:String,
    description: String = "",
    handle: ElementHandle = ElementHandle(createLFO()),
) : Element(label, description, handle) {



    override val allInputs: List<ElementInPort>
        get() = listOf(inFrequency, inType, inMinimum, inMaximum)

    override val allOutputs: List<ElementOutPort>
        get() = listOf(outValue)


    val outValue: ElementOutPort
        get() = ElementOutPort(this, "outVal", 0)

    val inFrequency: ElementInPort
        get() = ElementInPort(this, "freq", 0, ValueFormat.Frequency)

    val inType: ElementInPort
        get() = ElementInPort(this, "wave", 1, ValueFormat.Options<LFOWaveTypes>())

    val inMinimum: ElementInPort
        get() = ElementInPort(this, "minVal", 2)

    val inMaximum: ElementInPort
        get() = ElementInPort(this, "maxVal", 3)
}