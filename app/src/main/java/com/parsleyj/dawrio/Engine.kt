package com.parsleyj.dawrio

import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.device.DeviceCreator
import com.parsleyj.dawrio.daw.device.LFOCreator
import com.parsleyj.dawrio.daw.device.MainOutputDevice
import com.parsleyj.dawrio.daw.device.SawOSCCreator

object Engine {
    // Used to load the 'dawrio' library on application startup.
    init {
        System.loadLibrary("dawrio")
    }

    var createdVoice: Voice? = null

    val deviceCreators: List<DeviceCreator> = listOf(
        LFOCreator,
        SawOSCCreator,
    )


    fun initialize() {
        val voice = Voice().edit {
            val mainOut = addDevice { MainOutputDevice("Main Output") }
            setMainOutput(mainOut)
        }

        createdVoice = voice
        setVoice(voice.handle.toAddress)

        startEngine()
    }


    private external fun setVoice(address: Long)

    external fun startEngine()

    external fun stopEngine()

    external fun setSoundOn(on: Boolean)

    external fun isSoundOn():Boolean
}