package com.parsleyj.dawrio.daw.device

import android.media.Image
import androidx.compose.ui.graphics.vector.ImageVector

data class DeviceCreator(
    val name: String,
    val icon:ImageVector,
    val create: () -> Device,
)