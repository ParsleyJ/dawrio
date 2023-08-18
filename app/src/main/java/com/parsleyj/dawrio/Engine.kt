package com.parsleyj.dawrio

import com.parsleyj.dawrio.daw.components.Component
import com.parsleyj.dawrio.daw.components.KnobComponent
import com.parsleyj.dawrio.daw.components.LFOComponent
import com.parsleyj.dawrio.daw.components.SquareOscComponent
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.components.VolumeComponent

object Engine {
    // Used to load the 'dawrio' library on application startup.
    init {
        System.loadLibrary("dawrio")
    }
//TODO integrate components in voice
    fun createAndSetVoice(): Pair<Voice, List<Component>> {
        val components = mutableListOf<Component>()

        val voice = Voice().edit {
            listOf(
                KnobComponent(range = 1f..880f),
                KnobComponent(range = 0f..8f),
//                KnobComponent(range = 0f..8f),
//                KnobComponent(range = 0f..8f),
//                PushComponent(range = 0f..1f),
                LFOComponent(),
                SquareOscComponent(),
                VolumeComponent()
            ).forEach {
                components.add(it)
                addElement(it.element)
            }


//            constEmitter = constEmitter(1.0f, "Lfo Frq")
//            val minConst = constEmitter(0f, "Min Vol")
//            val maxConst = constEmitter(1f, "Max Vol")
//            val lfoTypeConst = constEmitter(2f, "LFO type")
//            val oscFreq = constEmitter(220f, "Note:A2")
//            lfo = lfo("LFO")
//            val osc = sawOsc("MyOsc")
//            val volume = volume(1.0f, "Volume")
//            osc.outAudioL.connect(volume.inAudioL)
//            osc.outAudioR.connect(volume.inAudioR)
//            constEmitter.outPort.connect(lfo.inFrequency)
//            oscFreq.outPort.connect(osc.inFrequency)
//            minConst.outPort.connect(lfo.inMinimum)
//            maxConst.outPort.connect(lfo.inMaximum)
//            lfoTypeConst.outPort.connect(lfo.inType)
//            lfo.outValue.connect(volume.inAmount)
        }



        setVoice(voice.handle.toAddress)


        startEngine()

        return voice to components
    }


    private external fun setVoice(address: Long)

    external fun startEngine()

    external fun stopEngine()

    external fun beepEvent(on: Boolean)
}