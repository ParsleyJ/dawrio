package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort
import com.parsleyj.dawrio.daw.elementroute.Route
import java.util.UUID

@JvmInline
value class DeviceInputID(val uuid: UUID = UUID.randomUUID())

class DeviceInput(
    val device: Device,
    val name: String,
    val isMain: Boolean,
    val type: DeviceStreamType,
    val id: DeviceInputID = DeviceInputID(),
    val onConnect: RouteListBuilder.(output: DeviceOutput) -> Unit,
) {
    fun compatibleWith(output: DeviceOutput): Boolean {
        return this.type.channels.size == output.type.channels.size
    }

    fun buildConnection(output: DeviceOutput): Connection? {
        if (!compatibleWith(output)) {
            return null
        }
        val builtList = mutableListOf<Route>()
        object : RouteListBuilder {
            override fun ElementInPort.addRouteTo(outPort: ElementOutPort): Route {
                val result = this.connectionTo(outPort)
                builtList.add(result)
                return result
            }

            override fun ElementOutPort.addRouteTo(inPort: ElementInPort): Route {
                val result = this.connectionTo(inPort)
                builtList.add(result)
                return result
            }
        }.onConnect(output)
        return Connection(output, this, builtList)
    }

    fun findConnection(allConnections: List<Connection>): Connection? {
        return allConnections.find { it.to.id == this.id }
    }

    override fun toString(): String = "${device.label}/$name"
}