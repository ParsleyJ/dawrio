package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.elementroute.ElementOutPort
import java.util.UUID

class DeviceOutput(
    val device: Device,
    val name: String,
    val isMain: Boolean,
    val type: DeviceStreamType,
    val id: UUID = UUID.randomUUID(),
    val provideOutPort: (channelIndex: Int) -> ElementOutPort,
) {
    override fun toString(): String = "${device.label}/$name"
}