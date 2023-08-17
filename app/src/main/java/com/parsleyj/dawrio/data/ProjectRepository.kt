package com.parsleyj.dawrio.data

import androidx.lifecycle.LiveData
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.VoiceHandle


interface ProjectRepository {


    fun observeVoices(): LiveData<List<Voice>>

    suspend fun getVoices(forceUpdate: Boolean = false): List<Voice>

    suspend fun refreshVoices()

    fun observeVoice(voiceIdentifier: VoiceHandle): LiveData<Voice?>

    suspend fun getVoice(voiceIdentifier: VoiceHandle, forceUpdate: Boolean = false): Voice?

    suspend fun refreshVoice(voiceIdentifier: VoiceHandle)

    suspend fun deleteVoice(voiceIdentifier: VoiceHandle)

    //TODO add CRUD for devices
    //TODO add CRUD for routes

}