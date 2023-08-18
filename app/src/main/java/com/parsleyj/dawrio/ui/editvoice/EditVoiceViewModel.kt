package com.parsleyj.dawrio.ui.editvoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parsleyj.dawrio.data.ProjectRepository
import com.parsleyj.dawrio.daw.Device
import com.parsleyj.dawrio.daw.Route
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.VoiceHandle
import kotlinx.coroutines.launch


class EditVoiceViewModel(
    val voiceHandle: VoiceHandle,
    private val repository: ProjectRepository
) : ViewModel() {

    val devices = MutableLiveData<List<Device>>()

    val routes = MutableLiveData<List<Route>>()


    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading
    private var isDataLoaded = false

    fun start() {
        if (_dataLoading.value == true) {
            return
        }

        if (isDataLoaded) {
            return
        }
        _dataLoading.value = true

        viewModelScope.launch {
            repository.getVoice(voiceHandle)?.let {
                onVoiceLoaded(it)
            }
        }
    }

    private fun onVoiceLoaded(voice: Voice) {
        devices.value = voice.devices
        routes.value = voice.routes
        _dataLoading.value = false
        isDataLoaded = true
    }


    //TODO this should only commit changes
    fun saveVoice(handle: VoiceHandle, editor: Voice.VoiceUpdater.() -> Voice) {
//        viewModelScope.launch {
//            repository.editVoice(handle, editor)
//            _voiceUpdatedEvent.value = true
//        }
    }

}