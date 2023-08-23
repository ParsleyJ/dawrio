package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.elementroute.Route
import java.util.UUID

class Connection(
    val from: DeviceOutput,
    val to: DeviceInput,
    val routes: List<Route>,
    val id: UUID = UUID.randomUUID()
) {
    override fun toString(): String = "$from --> $to"
}