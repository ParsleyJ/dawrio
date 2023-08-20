package com.parsleyj.dawrio

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Notifications
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.device.DeviceCreator
import com.parsleyj.dawrio.daw.device.LFODevice
import com.parsleyj.dawrio.daw.device.SawOSCDevice

object Engine {
    // Used to load the 'dawrio' library on application startup.
    init {
        System.loadLibrary("dawrio")
    }

    var createdVoice: Voice? = null

    val deviceCreators: List<DeviceCreator> = listOf(
        DeviceCreator("LFO", Icons.Outlined.ExitToApp) {
            LFODevice()
        },
        DeviceCreator("SawOSC", Icons.Outlined.Notifications) {
            SawOSCDevice()
        }
    )


    fun initialize() {
        val voice = Voice()/*.edit {
            addDevice { LFODevice() }
            addDevice { LFODevice() }
            addDevice { SawOSCDevice() }
        }*/

        createdVoice = voice
        setVoice(voice.handle.toAddress)

        startEngine()
    }


    private external fun setVoice(address: Long)

    external fun startEngine()

    external fun stopEngine()

    external fun beepEvent(on: Boolean)
}