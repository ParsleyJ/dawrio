package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.Device
import com.parsleyj.dawrio.daw.DeviceHandle
import com.parsleyj.dawrio.daw.DeviceType
import com.parsleyj.dawrio.daw.InPort
import com.parsleyj.dawrio.daw.OutPort

class LFO(
    label:String,
    description: String = "",
    handle: DeviceHandle = DeviceHandle(createLFO()),
) : Device(label, description, handle) {
    override val type: DeviceType
        get() = DeviceType.Generator

    val outValue: OutPort
        get() = OutPort(this, "outValue", 0)

    val inFrequency: InPort
        get() = InPort(this, "frequency", 0)

    val inType: InPort
        get() = InPort(this, "waveType", 1)

    val inMinimum: InPort
        get() = InPort(this, "minimumValue", 2)

    val inMaximum: InPort
        get() = InPort(this, "maximumValue", 3)
}