package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.elementroute.ElementOutPort
import java.util.UUID

@JvmInline
value class DeviceOutputID(val uuid: UUID = UUID.randomUUID())

class DeviceOutput(
    val device: Device,
    val name: String,
    val isMain: Boolean,
    val type: DeviceStreamType,
    val id: DeviceOutputID = DeviceOutputID(),
    val provideOutPort: (channelIndex: Int) -> ElementOutPort,
) {
    override fun toString(): String = "${device.label}/$name"
}