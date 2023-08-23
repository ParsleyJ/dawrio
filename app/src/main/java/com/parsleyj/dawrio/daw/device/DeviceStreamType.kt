package com.parsleyj.dawrio.daw.device

import com.parsleyj.dawrio.daw.ValueFormat

sealed class DeviceStreamType(vararg val channels: ChannelDescriptor) {
    object StereoAudio : DeviceStreamType(
        ChannelDescriptor(ValueFormat.AudioSamples, "Left side"),
        ChannelDescriptor(ValueFormat.AudioSamples, "Right side")
    )

    object Modulation : DeviceStreamType(
        ChannelDescriptor(ValueFormat.NumericWithDecimals(1, signed = false))
    )
}