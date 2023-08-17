package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
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