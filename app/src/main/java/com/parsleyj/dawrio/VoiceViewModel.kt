package com.parsleyj.dawrio

import android.util.Log
import androidx.lifecycle.ViewModel
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.device.Connection
import com.parsleyj.dawrio.daw.device.Device
import com.parsleyj.dawrio.daw.device.DeviceCreator
import com.parsleyj.dawrio.daw.device.DeviceInput
import com.parsleyj.dawrio.daw.device.DeviceOutput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import java.util.UUID

class VoiceViewModel : ViewModel() {
    private val voice: Voice

    init {
        Engine.initialize()
        voice = Engine.createdVoice!!
    }


    private val _devices = MutableStateFlow(listOf<Device>())
    val devices = _devices.asStateFlow()

    private val _connections = MutableStateFlow(listOf<Connection>())
    val connections = _connections.asStateFlow()

    private val _playing = MutableStateFlow(false)
    val playing: StateFlow<Boolean> = _playing


    init {
        _devices.update { voice.devices }
        _connections.update { voice.connectionsList }
    }

    fun setPlaying(playing: Boolean) {
        Log.d("Sound", "===========================================")
        Log.d("Sound", "devices(${voice.devices.size})=${voice.devices}")
        Log.d("Sound", "connections(${voice.connectionsList.size})=${voice.connectionsList}")
        val allElements = voice.devices.flatMap { it.allElements }
        Log.d("Sound", "elements(${allElements.size})=$allElements")
        Log.d("Sound", "routes(${voice.allRoutes.size})=${voice.allRoutes}")
        _playing.update {
            Engine.setSoundOn(playing)
            if (playing) {
                voice.start()
            } else {
                voice.stop()
            }
            playing
        }
    }

    fun pushConnectionChange(input: DeviceInput, output: DeviceOutput?) {
        _connections.update {
            voice.edit { updateConnection(output, input) }
            voice.connectionsList
        }
    }

    fun pushNewDevice(position: Int, deviceCreator: DeviceCreator) {
        _devices.update {
            voice.edit { addDevice(position, deviceCreator.create) }
            Log.d("Updating devices", "Updated Devices: ${voice.devices.map { it.label }}")
            voice.devices.toList()
        }
    }


    inline fun <reified T : Device> getDevice(deviceUUID: UUID): Flow<T?> {
        return devices.transform { list ->
            (list.find { it.id == deviceUUID } as? T)?.let { emit(it) } ?: emit(null)
        }
    }


}