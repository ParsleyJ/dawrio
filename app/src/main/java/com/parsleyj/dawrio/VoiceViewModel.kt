package com.parsleyj.dawrio

import androidx.lifecycle.ViewModel
import com.parsleyj.dawrio.daw.components.Component
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.element.ElementHandle
import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort
import com.parsleyj.dawrio.daw.elementroute.Route
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


    private val _elements = MutableStateFlow(listOf<Element>())
    val elements: StateFlow<List<Element>> = _elements.asStateFlow()

    private val _routes = MutableStateFlow(listOf<Route>())
    val routes: StateFlow<List<Route>> = _routes.asStateFlow()

    private val _playing = MutableStateFlow(false)
    val playing: StateFlow<Boolean> = _playing

    private val _components = MutableStateFlow(listOf<Component>())
    val components: StateFlow<List<Component>> = _components

    init {
        _elements.update {
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

    fun pushRouteChange(input: ElementInPort, output: ElementOutPort?) {
        _routes.update {
            voice.updateRoute(output, input)
            voice.routes
        }
    }

    inline fun <reified T: Element> getElement(handle: ElementHandle): Flow<T?> {
        return elements.transform { list ->
            (list.find { it.handle == handle } as? T)?.let { emit(it) } ?: emit(null)
        }
    }
}