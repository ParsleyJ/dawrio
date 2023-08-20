package com.parsleyj.dawrio.daw.element

import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort

class VolumeMeter(
    label: String,
    bufferSize: Int,
    description: String = "",
    handle: ElementHandle = ElementHandle(createVolumeMeter(bufferSize))
) : Element(label, description, handle) {

    val inAudioL = ElementInPort(this, "inAudioL", 0, ValueFormat.AudioSamples)
    val inAudioR = ElementInPort(this, "inAudioR", 1, ValueFormat.AudioSamples)
    val outDecibelsL = ElementOutPort(this, "outDecibelsL", 0)
    val outDecibelsR = ElementOutPort(this, "outDecibelsR", 1)

    override val allInputs: List<ElementInPort>
        get() = listOf(inAudioL, inAudioR)

    override val allOutputs: List<ElementOutPort>
        get() = listOf(outDecibelsL, outDecibelsR)


    companion object{
        private external fun createVolumeMeter(bufferSize:Int):Long
    }
}