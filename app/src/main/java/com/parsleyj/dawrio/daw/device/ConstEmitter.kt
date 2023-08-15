package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.Device
import com.parsleyj.dawrio.daw.DeviceHandle
import com.parsleyj.dawrio.daw.DeviceType
import com.parsleyj.dawrio.daw.OutPort

class ConstEmitter internal constructor(
    initialValue: Float,
    label: String,
    description: String = "",
    handle: DeviceHandle = DeviceHandle(createConstEmitter(initialValue))
) : Device(label, description, handle) {
    override val type: DeviceType
        get() = DeviceType.Generator

    val outPort: OutPort
        get() = OutPort(this, "constant", 0)

    var value: Float
        get() = getValue(handle.toAddress)
        set(value) = setValue(handle.toAddress, value)

    companion object {
        private external fun setValue(handle: Long, float: Float)
        private external fun getValue(handle: Long): Float
    }
}