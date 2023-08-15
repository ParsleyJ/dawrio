package com.parsleyj.dawrio.daw

@JvmInline
value class VoiceHandle(val toAddress:Long)

class Voice(val handle: VoiceHandle = VoiceHandle(createVoice())) {

    fun start(){
        startVoice(handle.toAddress)
    }
    fun stop() {
        stopVoice(handle.toAddress)
    }
    fun destroy() {
        destroyVoice(handle.toAddress)
    }

    fun setLayout(devices:List<Device>, routes:List<Route>, outDevice:Device = devices[0]){
        setLayout(
            handle.toAddress,
            devices.map { it.handle.toAddress }.toLongArray(),
            routes.map{it.handle.toAddress}.toLongArray(),
            outDevice.handle.toAddress
        )
    }

    companion object{
        private external fun createVoice():Long
        private external fun startVoice(address:Long)
        private external fun stopVoice(address:Long)
        private external fun destroyVoice(address:Long)
        private external fun setLayout(
            address: Long,
            devices: LongArray,
            routes: LongArray,
            outDevice: Long
        )
    }
}
