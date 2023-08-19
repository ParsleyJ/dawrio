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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun Knob(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    angleRange: ClosedFloatingPointRange<Float> = 25f..335f,
    special:Boolean = false,
    overValue: Float = 0f,
    overValueRange: ClosedFloatingPointRange<Float> = 0f..0f,
    overValuePlacementScale: Float = 0.7f,
    showOverValue: Boolean = true,
    orientation: Orientation = Orientation.Vertical,
    strokeWidth: Dp = 2.dp,
    fineModeDelayMs: Long = 1000L,
    onValueChange: (Float) -> Unit = {}
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
    val overValueLineColor = MaterialTheme.colorScheme.secondary
    val overValueSweepColor = MaterialTheme.colorScheme.secondaryContainer
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
            val v = value - deltaIn160dp * valueRangeSize * 0.5f * if (fine) .1f else 1f
            val next = v.coerceIn(valueRange)


            isBeingDragged = true
            if (value != next) {
                value = next
                rotation = next.toAngle(valueRange, angleRange)
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
                val strokeWidthPx = strokeWidth.toPx()
                val zero = 90f
                val lineC = if (inFineSettingMode) fineLineColor else lineColor
                val fillC = if (inFineSettingMode) fineSweepColor else sweepColor

                val overValueStartAngle =
                    overValueRange.start.toAngle(valueRange, angleRange) - angleRange.start
                val overValueEndAngle =
                    overValueRange.endInclusive.toAngle(valueRange, angleRange) - angleRange.start
                val overValueAngle =
                    overValue.toAngle(valueRange, angleRange) - angleRange.start

                this.drawArc(
                    color = fillC,
                    startAngle = zero,
                    sweepAngle = -(rotation - angleRange.start),
                    useCenter = true,
                )

                if(showOverValue) {
                    //Over value sweep
                    this.drawArc(
                        color = overValueSweepColor,
                        startAngle = zero,
                        sweepAngle = overValueAngle,
                        size = this.size * overValuePlacementScale,
                        topLeft = this.center - Offset(
                            this.size.width / 2f * overValuePlacementScale,
                            this.size.height / 2f * overValuePlacementScale,
                        ),
                        useCenter = true
                    )
                }

                //Outer circle
                this.drawCircle(
                    color = lineC,
                    style = Stroke(width = strokeWidthPx),
                    radius = radius,
                )


                if(showOverValue) {
                    // Over value line for over value range
                    this.drawArc(
                        color = overValueLineColor,
                        style = Stroke(width = strokeWidthPx),
                        size = this.size * overValuePlacementScale,
                        topLeft = this.center - Offset(
                            this.size.width / 2f * (overValuePlacementScale),
                            this.size.height / 2f * (overValuePlacementScale),
                        ),
                        startAngle = zero + overValueStartAngle,
                        sweepAngle = overValueEndAngle - overValueStartAngle,
                        useCenter = false
                    )
                }
                // Indicator line
                this.drawLine(
                    color = lineC,
                    start = this.center,
                    end = this.center + Offset(0.0f, radius),
                    strokeWidth = strokeWidthPx,
                )

                //Little point at center
                this.drawCircle(
                    color = if(special) overValueLineColor else lineC,
                    radius = strokeWidthPx*1.5f,
                )
            }
        )

    }

}

private fun Float.toAngle(
    vRange: ClosedFloatingPointRange<Float>,
    aRange: ClosedFloatingPointRange<Float> = 0f..360f
): Float {
    val vRangeSize = vRange.endInclusive - vRange.start
    val aRangeSize = aRange.endInclusive - aRange.start
    return ((this - vRange.start) / vRangeSize) * aRangeSize + aRange.start
}
