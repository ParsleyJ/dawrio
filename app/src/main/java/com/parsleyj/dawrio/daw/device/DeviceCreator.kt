package com.parsleyj.dawrio.daw.device

import androidx.annotation.DrawableRes

data class DeviceCreator(
    val name: String,
    @DrawableRes val icon: Int,
    val create: () -> Device,
)