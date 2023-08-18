package com.parsleyj.dawrio

import androidx.lifecycle.ViewModel
import com.parsleyj.dawrio.daw.Component
import com.parsleyj.dawrio.daw.Element
import com.parsleyj.dawrio.daw.ElementHandle
import com.parsleyj.dawrio.daw.InPort
import com.parsleyj.dawrio.daw.OutPort
import com.parsleyj.dawrio.daw.Route
import com.parsleyj.dawrio.daw.Voice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update

class VoiceViewModel : ViewModel() {

    //TODO change
    val privatePair = Engine.createAndSetVoice()
    val voice: Voice = privatePair.first


    private val _devices = MutableStateFlow(listOf<Element>())
    val devices: StateFlow<List<Element>> = _devices.asStateFlow()

    private val _routes = MutableStateFlow(listOf<Route>())
    val routes: StateFlow<List<Route>> = _routes.asStateFlow()

    private val _playing = MutableStateFlow(false)
    val playing: StateFlow<Boolean> = _playing

    private val _components = MutableStateFlow(listOf<Component>())
    val components: StateFlow<List<Component>> = _components

    init {
        _devices.update {
            voice.elements
        }
        _routes.update {
            voice.routes
        }
        _components.update {
            //TODO change
            privatePair.second
        }
    }

    fun setPlaying(playing: Boolean) {
        _playing.update {
            Engine.beepEvent(playing)
            playing
        }
    }

    fun pushRouteChange(input: InPort, output: OutPort?) {
        _routes.update {
            voice.updateRoute(output, input)
            voice.routes
        }
    }

    inline fun <reified T:Element> getDevice(handle: ElementHandle): Flow<T?> {
        return devices.transform { list ->
            (list.find { it.handle == handle } as? T)?.let { emit(it) } ?: emit(null)
        }
    }
}