package com.parsleyj.dawrio

import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.device.LFODevice
import com.parsleyj.dawrio.daw.device.SawOSCDevice

object Engine {
    // Used to load the 'dawrio' library on application startup.
    init {
        System.loadLibrary("dawrio")
    }

    var testVoice: Voice? = null


    fun initialize() {
        val voice = Voice().edit {
            addDevice { LFODevice() }
            addDevice { LFODevice() }
            addDevice { SawOSCDevice() }
        }

        testVoice = voice
        setVoice(voice.handle.toAddress)

        startEngine()
    }


    private external fun setVoice(address: Long)

    external fun startEngine()

    external fun stopEngine()

    external fun beepEvent(on: Boolean)
}