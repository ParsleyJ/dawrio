package com.parsleyj.dawrio.data

import androidx.lifecycle.LiveData
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.VoiceHandle

interface ProjectDataSource {
    fun observeVoices(): LiveData<List<Voice>>
    suspend fun getVoices(): List<Voice>
    suspend fun refreshVoices()
    fun observeVoice(voiceIdentifier: VoiceHandle): LiveData<Voice?>
    suspend fun saveVoice(voice:Voice)
    suspend fun getVoice(voiceIdentifier: VoiceHandle): Voice?
    suspend fun refreshVoice(voiceIdentifier: VoiceHandle)
    suspend fun deleteVoice(voiceIdentifier: VoiceHandle)

    //TODO add CRUD for devices
    //TODO add CRUD for routes
}