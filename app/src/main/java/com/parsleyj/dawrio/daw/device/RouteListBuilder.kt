package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort
import com.parsleyj.dawrio.daw.elementroute.Route

interface RouteListBuilder {
    fun ElementInPort.addRouteTo(outPort: ElementOutPort): Route
    fun ElementOutPort.addRouteTo(inPort: ElementInPort): Route
}