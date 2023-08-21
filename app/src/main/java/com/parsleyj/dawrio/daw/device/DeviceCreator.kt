package com.parsleyj.dawrio.daw.device

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class DeviceCreator(
    val name: String,
    @DrawableRes val icon: Int,
    val create: () -> Device,
)