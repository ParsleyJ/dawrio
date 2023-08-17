package com.parsleyj.dawrio.data

import androidx.lifecycle.LiveData
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.VoiceHandle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MemoryProjectRepository(
    private val memoryDataSource: ProjectDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProjectRepository {
    override fun observeVoices(): LiveData<List<Voice>> {
        return memoryDataSource.observeVoices()
    }

    override suspend fun getVoices(forceUpdate: Boolean): List<Voice> {
        return memoryDataSource.getVoices()
    }

    override suspend fun refreshVoices() {
        //Do nothing
    }

    override suspend fun deleteVoice(voiceIdentifier: VoiceHandle) {
        coroutineScope {
            launch { memoryDataSource.deleteVoice(voiceIdentifier) }
        }
    }


    override suspend fun refreshVoice(voiceIdentifier: VoiceHandle) {
        //Do nothing
    }

    override suspend fun getVoice(voiceIdentifier: VoiceHandle, forceUpdate: Boolean): Voice? {
        return memoryDataSource.getVoice(voiceIdentifier)
    }

    override fun observeVoice(voiceIdentifier: VoiceHandle): LiveData<Voice?> {
        return memoryDataSource.observeVoice(voiceIdentifier)
    }


}