package com.parsleyj.dawrio.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.VoiceHandle

class MemoryDataSource : ProjectDataSource {
    private val voices = mutableMapOf<VoiceHandle, Voice>()

    private val voicesLiveData = MutableLiveData(listOf<Voice>())
    private val eachVoiceLiveData = mutableMapOf<VoiceHandle, MutableLiveData<Voice?>>()


    override fun observeVoices(): LiveData<List<Voice>> {
        return voicesLiveData
    }

    override suspend fun getVoices(): List<Voice> {
        return voices.values.toList()
    }

    override suspend fun refreshVoices() {
        //Do nothing.
    }

    override suspend fun saveVoice(voice: Voice) {
        val previous = voices.put(voice.handle, voice)
        voicesLiveData.value = voices.values.toList()
        if (previous != null) {
            eachVoiceLiveData[voice.handle]?.value = voice
        }
    }

    override suspend fun deleteVoice(voiceIdentifier: VoiceHandle) {
        val removed = voices.remove(voiceIdentifier)
        if (removed != null) {
            voicesLiveData.value = voices.values.toList()
            eachVoiceLiveData[voiceIdentifier]?.value = null
            removed.destroy()
        }
    }



    override suspend fun refreshVoice(voiceIdentifier: VoiceHandle) {
        //Do nothing.
    }

    override suspend fun getVoice(voiceIdentifier: VoiceHandle): Voice? {
        return voices[voiceIdentifier]
    }

    override fun observeVoice(voiceIdentifier: VoiceHandle): LiveData<Voice?> {
        val voice: Voice? = voices[voiceIdentifier]
        return eachVoiceLiveData.getOrPut(voiceIdentifier) { MutableLiveData<Voice?>(voice) }
    }

}