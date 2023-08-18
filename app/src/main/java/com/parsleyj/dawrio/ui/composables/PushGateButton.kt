package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun PushGateButton(
    onStartPush: () -> Unit,
    onStopPush: () -> Unit = {},
    size: DpSize = DpSize(64.dp, 64.dp),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }


    FilledIconButton(
        modifier = Modifier.size(size),
        onClick = {},
        interactionSource = interactionSource,
        content = content
    )

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            when (it) {
                is PressInteraction.Press -> {
                    onStartPush()
                }

                is PressInteraction.Release, is PressInteraction.Cancel -> {
                    onStopPush()
                }
            }
        }
    }
}