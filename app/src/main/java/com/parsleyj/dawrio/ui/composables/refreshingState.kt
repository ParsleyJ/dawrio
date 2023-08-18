package com.parsleyj.dawrio.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow


@Composable
fun <T> refreshingState(
    active: Boolean = true,
    fps: Float = 120f,
    read: () -> T,
    initialValue: T = read(),
): State<T> {
    val interval = (1000.0 / fps.toDouble()).toLong()

    val tickFlow = flow {
        while (true) {
            delay(interval)
            emit(Unit)
        }
    }

    val valueFlow = MutableSharedFlow<T>()

    LaunchedEffect(read, active, fps) {
        if (active) {
            while (true) {
                tickFlow.collectLatest { // Used to skip late frames
                    valueFlow.emit(read())
                }
            }
        }
    }

    return valueFlow.collectAsStateWithLifecycle(initialValue = initialValue)
}

