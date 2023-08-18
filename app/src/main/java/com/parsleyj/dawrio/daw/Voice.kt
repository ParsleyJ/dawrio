package com.parsleyj.dawrio.daw

import android.util.Log
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.element.ElementHandle
import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.daw.elementroute.RouteHandle
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@JvmInline
value class VoiceHandle(val toAddress: Long)

class Voice(val handle: VoiceHandle = VoiceHandle(createVoice())) {


    private val elementsPrivate: MutableMap<ElementHandle, Element> = mutableMapOf()
    val elements: List<Element>
        get() = elementsPrivate.values.toList()


    private val routesPrivate: MutableMap<RouteHandle, Route> = mutableMapOf()
    val routes: List<Route>
        get() = routesPrivate.values.toList()

    fun start() {
        startVoice(handle.toAddress)
    }

    fun stop() {
        stopVoice(handle.toAddress)
    }

    fun destroy() {
        destroyVoice(handle.toAddress)
    }

    private fun setRouteWithoutUpdating(route: Route) {
        val routesForSameInput = this.routes.filter { it.inPort == route.inPort }
        routesForSameInput.map { it.handle }.forEach { this.routesPrivate.remove(it) }
        //TODO: destroy prev when commit
        this.routesPrivate[route.handle] = route
    }

    private fun setElementWithoutUpdating(element: Element) {
        this.elementsPrivate[element.handle] = element
    }

    fun addElement(dev: Element) {
        setElementWithoutUpdating(dev)
        commitNativeLayout()
    }

    fun addRoute(route: Route) {
        setRouteWithoutUpdating(route)
        commitNativeLayout()
    }

    fun removeElement(el: Element) {
        removeElement(el.handle)
        commitNativeLayout()
    }

    fun removeElement(handle: ElementHandle) {
        this.elementsPrivate.remove(handle)
        commitNativeLayout()
    }

    fun removeRoute(dev: Route) {
        removeRoute(dev.handle)
        commitNativeLayout()
    }

    fun removeRoute(handle: RouteHandle) {
        this.routesPrivate.remove(handle)
        commitNativeLayout()
    }


    interface VoiceUpdater {
        val voice: Voice
        fun ElementInPort.connect(outPort: ElementOutPort): Route
        fun ElementOutPort.connect(inPort: ElementInPort): Route
        fun <T : Element> addElement(element: T): T
    }


    fun edit(block: VoiceUpdater.() -> Unit): Voice {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        val builder = object : VoiceUpdater {
            override val voice: Voice
                get() = this@Voice

            override fun ElementInPort.connect(outPort: ElementOutPort): Route {
                val result = this.connectionTo(outPort)
                setRouteWithoutUpdating(result)
                return result
            }

            override fun ElementOutPort.connect(inPort: ElementInPort): Route {
                val result = this.connectionTo(inPort)
                setRouteWithoutUpdating(result)
                return result
            }

            override fun <T : Element> addElement(element: T): T {
                setElementWithoutUpdating(element)
                return element
            }


        }
        block(builder)
        commitNativeLayout()
        return this
    }


    private fun commitNativeLayout() {
        updateNativeLayout(
            handle.toAddress,
            elements.map { it.handle.toAddress }.toLongArray(),
            routes.map { it.handle.toAddress }.toLongArray(),
            if (elements.isEmpty()) 0L else elements.last().handle.toAddress
        )
    }

    fun updateRoute(source: ElementOutPort?, input: ElementInPort) {
        Log.d("VoiceKt", "updateRoute: $source -> $input")
        if (source == null) {
            input.findRoute(routes)?.let {
                routesPrivate.remove(it.handle) ?.let { itRoute->
                    Log.d("VoiceKt", "updateRoute: removed - $itRoute")
                }
            }
        } else {
            val newRoute = source.connectionTo(input)
            setRouteWithoutUpdating(newRoute)
        }
        commitNativeLayout()
    }

    companion object {
        private external fun createVoice(): Long
        private external fun startVoice(address: Long)
        private external fun stopVoice(address: Long)
        private external fun destroyVoice(address: Long)
        private external fun getElementCount(addr: Long): Int
        private external fun getElements(addr: Long, resultArray: LongArray)
        private external fun getRoutesCount(addr: Long): Int
        private external fun getRoutes(addr: Long, resultArray: LongArray)
        private external fun getOutElement(addr: Long): Long
        private external fun updateNativeLayout(
            address: Long,
            elements: LongArray,
            routes: LongArray,
            outElement: Long
        )
    }
}
