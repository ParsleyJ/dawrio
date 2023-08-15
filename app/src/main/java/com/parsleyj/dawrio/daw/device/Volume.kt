package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.Device
import com.parsleyj.dawrio.daw.DeviceHandle
import com.parsleyj.dawrio.daw.DeviceType
import com.parsleyj.dawrio.daw.InPort
import com.parsleyj.dawrio.daw.OutPort

class Volume(
    initalAmount: Float,
    label: String,
    description: String = "",
    handle: DeviceHandle = DeviceHandle(createVolume(initalAmount))
) : Device(label, description, handle) {
    override val type: DeviceType
        get() = DeviceType.Effect

    val outAudioL: OutPort
        get() = OutPort(this, "audioL", 0)
    val outAudioR: OutPort
        get() = OutPort(this, "audioR", 1)

    val inAudioL: InPort
        get() = InPort(this, "inAudioL", 0)
    val inAudioR: InPort
        get() = InPort(this, "inAudioR", 1)
    val inAmount = InPort(this, "amount", 2)

    var amount: Float
        get() = getAmount(handle.toAddress)
        set(value) = setAmount(handle.toAddress, value)


    companion object{
        private external fun getAmount(addr:Long):Float
        private external fun setAmount(addr:Long, amount:Float)
    }


}