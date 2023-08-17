package com.parsleyj.dawrio.daw

import androidx.compose.runtime.Composable

interface Component {
    val gui: @Composable () -> Unit
    val device: Device
}