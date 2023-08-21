package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.ValueFormat

@Composable
fun Meter(
    modifier: Modifier = Modifier,
    value: Float = .9f,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    meterColor: Color = MaterialTheme.colorScheme.primary,
    bgColor: Color = MaterialTheme.colorScheme.background,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    showInnerText: Boolean = false,
    orientation: Orientation = Orientation.Horizontal,
    format: (f: Float) -> String = ValueFormat.NumericWithDecimals(1).convertToString,
) {
    val rangeExtent = range.endInclusive - range.start
    val normalizedValue = if (rangeExtent == 0f) {
        0f
    } else {
        value / rangeExtent
    }


    val changeFactorX = when (orientation) {
        Orientation.Horizontal -> normalizedValue
        Orientation.Vertical -> 1f
    }
    val changeFactorY = when (orientation) {
        Orientation.Horizontal -> 1f
        Orientation.Vertical -> -normalizedValue
    }




    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = modifier, onDraw = {
            val topLeft: Offset = when (orientation) {
                Orientation.Horizontal -> Offset.Zero
                Orientation.Vertical -> Offset(0.0f, this.size.height)
            }
            val cornerRadius = CornerRadius(2.dp.toPx())
            this.drawRoundRect(
                color = bgColor,
                size = this.size,
                cornerRadius = cornerRadius,
            )
            if (normalizedValue != 0f) {
                this.drawRect(
                    color = meterColor,
                    topLeft = topLeft,
                    size = Size(
                        this.size.width * changeFactorX,
                        this.size.height * changeFactorY
                    ),
                )
            }
            this.drawRoundRect(
                color = outlineColor,
                size = this.size,
                style = Stroke(width = 2.dp.toPx()),
                cornerRadius = cornerRadius,
            )
        })
        if (showInnerText) {
            Text(
                text = format(value),
                style = MaterialTheme.typography.labelSmall,
                color = outlineColor
            )
        }
    }

}