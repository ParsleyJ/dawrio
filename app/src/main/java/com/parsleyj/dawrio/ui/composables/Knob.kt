package com.parsleyj.dawrio.ui.composables

import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.util.toAngle


@Composable
fun Knob(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    initialValue: Float = valueRange.start,
    angleRange: ClosedFloatingPointRange<Float> = 25f..335f,
    special: Boolean = false,
    overValue: Float = 0f,
    initialOverValueRangeEnd: Float = 0f,
    overValuePlacementRadiusScale: Float = 0.9f,
    showOverValue: Boolean = true,
    orientation: Orientation = Orientation.Vertical,
    strokeWidth: Dp = 2.dp,
    overValueDelayMs: Long = android.view.ViewConfiguration.getLongPressTimeout().toLong(),
    fineModeDelayMs: Long = 5000L,
    onClick: () -> Unit = {},
    onValueChange: (Float) -> Unit = {},
    onOverValueScaleChange: (Float) -> Unit = {}
) {

    val vibrator = LocalContext.current.getSystemService(Vibrator::class.java)


    val valueRangeSize = valueRange.endInclusive - valueRange.start


    var value by remember { mutableStateOf(initialValue) }

    var overValueRangeEnd by remember(initialOverValueRangeEnd) { mutableStateOf(initialOverValueRangeEnd) }

    val rotation by remember {
        derivedStateOf { value.toAngle(valueRange, angleRange) }
    }

    val lineColor = MaterialTheme.colorScheme.primary
    val sweepColor = MaterialTheme.colorScheme.primaryContainer
    val overValueLineColor = MaterialTheme.colorScheme.secondary
    val overValueSweepColor = MaterialTheme.colorScheme.secondaryContainer
    val fineLineColor = MaterialTheme.colorScheme.tertiary
    val fineSweepColor = MaterialTheme.colorScheme.tertiaryContainer

    var lastDragActionTimeInMillis by remember { mutableStateOf(0L) }
    var inOverValueRangeSettingMode by remember { mutableStateOf(false) }
    var inFineSettingMode by remember { mutableStateOf(false) }



    with(LocalDensity.current) {
        var isBeingDragged by remember {
            mutableStateOf(false)
        }

        val draggableState = rememberDraggableState(onDelta = {
            val deltaIn160dp = it.toDp() / 160.dp

            val elapsedSinceLastAction = System.currentTimeMillis() - lastDragActionTimeInMillis

            inOverValueRangeSettingMode = showOverValue && (inOverValueRangeSettingMode ||
                    elapsedSinceLastAction in overValueDelayMs..fineModeDelayMs)
            inFineSettingMode = inFineSettingMode ||
                    elapsedSinceLastAction > fineModeDelayMs

            lastDragActionTimeInMillis = System.currentTimeMillis()

            val fine = inFineSettingMode
            val overSet = inOverValueRangeSettingMode



            if (overSet) {
                val nextUnlimited =
                    overValueRangeEnd - deltaIn160dp * valueRangeSize * 0.5f
                val next = nextUnlimited.coerceIn(-valueRange.endInclusive..valueRange.endInclusive)
                isBeingDragged = true
                if (overValueRangeEnd != next) {
                    overValueRangeEnd = next
                    onOverValueScaleChange(overValueRangeEnd / valueRangeSize)
                }
            } else {
                val nextUnlimited =
                    value - deltaIn160dp * valueRangeSize * 0.5f * if (fine) .1f else 1f
                val next = nextUnlimited.coerceIn(valueRange)
                isBeingDragged = true
                if (value != next) {
                    value = next
                    onValueChange(next)
                }
            }


        })

        Canvas(modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onClick = { onClick() },
            )
            .draggable(draggableState, orientation = orientation, onDragStopped = {
                isBeingDragged = false
                inOverValueRangeSettingMode = false
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
                val lineC = when {
                    inFineSettingMode -> fineLineColor
                    else -> lineColor
                }

                val fillC = when {
                    inFineSettingMode -> fineSweepColor
                    inOverValueRangeSettingMode -> overValueSweepColor
                    else -> sweepColor
                }

                val overValueStartAngle = 0f.toAngle(valueRange, angleRange) - angleRange.start
                val overValueEndAngle =
                    overValueRangeEnd.toAngle(valueRange, angleRange) - angleRange.start
                val overValueAngle = overValue.toAngle(valueRange, angleRange) - angleRange.start

                val showOverIndicatorStart =
                    value + overValueRangeEnd < valueRange.start
                val showOverIndicatorEnd =
                    value + overValueRangeEnd > valueRange.endInclusive

                this.drawArc(
                    color = fillC,
                    startAngle = zero,
                    sweepAngle = -(rotation - angleRange.start),
                    useCenter = true,
                )

                if (showOverValue) {
                    //Over value sweep
                    this.drawArc(
                        color = overValueSweepColor,
                        startAngle = zero,
                        sweepAngle = overValueAngle,
                        size = this.size * overValuePlacementRadiusScale,
                        topLeft = this.center - Offset(
                            this.size.width / 2f * overValuePlacementRadiusScale,
                            this.size.height / 2f * overValuePlacementRadiusScale,
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


                if (showOverValue) {
                    // Over value line for over value range
                    this.drawArc(
                        color = overValueLineColor,
                        style = Stroke(width = strokeWidthPx),
                        size = this.size * overValuePlacementRadiusScale,
                        topLeft = this.center - Offset(
                            this.size.width / 2f * (overValuePlacementRadiusScale),
                            this.size.height / 2f * (overValuePlacementRadiusScale),
                        ),
                        startAngle = zero + overValueStartAngle,
                        sweepAngle = (overValueEndAngle - overValueStartAngle)
                            .coerceIn(
                                (-rotation + angleRange.start - overValueStartAngle)..
                                        (-rotation + angleRange.endInclusive - overValueStartAngle)
                            ),
                        useCenter = false
                    )

                    if (showOverIndicatorEnd) {
                        this.drawArc(
                            color = overValueLineColor,
                            style = Stroke(width = strokeWidthPx),
                            size = this.size * overValuePlacementRadiusScale,
                            topLeft = this.center - Offset(
                                this.size.width / 2f * (overValuePlacementRadiusScale),
                                this.size.height / 2f * (overValuePlacementRadiusScale),
                            ),
                            useCenter = false,
                            startAngle = zero - rotation + angleRange.endInclusive + 5f,
                            sweepAngle = 5f
                        )
                    }
                    if (showOverIndicatorStart) {
                        this.drawArc(
                            color = overValueLineColor,
                            style = Stroke(width = strokeWidthPx),
                            size = this.size * overValuePlacementRadiusScale,
                            topLeft = this.center - Offset(
                                this.size.width / 2f * (overValuePlacementRadiusScale),
                                this.size.height / 2f * (overValuePlacementRadiusScale),
                            ),
                            useCenter = false,
                            startAngle = zero - rotation + angleRange.start - 5f,
                            sweepAngle = -5f
                        )
                    }
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
                    color = if (special) overValueLineColor else lineC,
                    radius = strokeWidthPx * 1.5f,
                )
            }
        )

    }

}


