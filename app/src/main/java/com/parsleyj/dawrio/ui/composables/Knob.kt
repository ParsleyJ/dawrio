package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun Knob(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    angleRange: ClosedFloatingPointRange<Float> = 25f..335f,
    orientation: Orientation = Orientation.Vertical,
    strokeWidth: Float = 5.0f,
    fineModeDelayMs:Long = 1000L,
    onValueChange: (Float) -> Unit
) {

    val valueRangeSize = valueRange.endInclusive - valueRange.start

    var rotation by remember {
        mutableStateOf(angleRange.start)
    }

    var value by remember {
        mutableStateOf(valueRange.start)
    }


    val lineColor = MaterialTheme.colorScheme.primary
    val sweepColor = MaterialTheme.colorScheme.primaryContainer
    val fineLineColor = MaterialTheme.colorScheme.tertiary
    val fineSweepColor = MaterialTheme.colorScheme.tertiaryContainer

    var lastDragActionTimeInMillis by remember { mutableStateOf(0L) }
    var inFineSettingMode by remember { mutableStateOf(false) }

    with(LocalDensity.current) {
        var isBeingDragged by remember {
            mutableStateOf(false)
        }

        val draggableState = rememberDraggableState(onDelta = {
            val deltaIn160dp = it.toDp() / 160.dp

            inFineSettingMode = inFineSettingMode ||
                    System.currentTimeMillis() - lastDragActionTimeInMillis > fineModeDelayMs
            lastDragActionTimeInMillis = System.currentTimeMillis()

            val fine = inFineSettingMode
            val v = value - deltaIn160dp * valueRangeSize * 0.5f * if(fine) .1f else 1f
            val next = v.coerceIn(valueRange)

            val angleRangeSize = angleRange.endInclusive - angleRange.start

            isBeingDragged = true
            if (value != next) {
                value = next
                val nextAngle = ((next - valueRange.start) / valueRangeSize) * angleRangeSize +
                    angleRange.start
                rotation = nextAngle
                onValueChange(next)
            }
        })

        Canvas(modifier = modifier
            .draggable(draggableState, orientation = orientation, onDragStopped = {
                isBeingDragged = false
                inFineSettingMode = false
            })
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown()
                        lastDragActionTimeInMillis = System.currentTimeMillis()
                    }
                }
            }
            .rotate(rotation),
            onDraw = {
                val radius = size.minDimension / 2.0f
                val zero = 90f
                this.drawArc(
                    color = if(inFineSettingMode) fineSweepColor else sweepColor,
                    startAngle = zero,
                    sweepAngle = -(rotation-angleRange.start),
                    useCenter = true
                )
                this.drawCircle(
                    color = if(inFineSettingMode) fineLineColor else lineColor,
                    style = Stroke(width = strokeWidth),
                    radius = radius,
                )
                this.drawLine(
                    color = if(inFineSettingMode) fineLineColor else lineColor,
                    start = this.center,
                    end = this.center + Offset(0.0f, radius),
                    strokeWidth = strokeWidth
                )
            }
        )

    }

}
