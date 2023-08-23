package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.elementroute.Route
import java.util.UUID

@JvmInline
value class ConnectionID(val uuid: UUID = UUID.randomUUID())

class Connection(
    val from: DeviceOutput,
    val to: DeviceInput,
    val routes: List<Route>,
    val id: ConnectionID = ConnectionID()
) {
    override fun toString(): String = "$from --> $to"
}