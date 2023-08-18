package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.parsleyj.dawrio.daw.ValueFormat

@Composable
fun Meter(
    modifier: Modifier = Modifier,
    value: Float = .9f,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    meterColor: Color = MaterialTheme.colorScheme.primary,
    bgColor: Color = MaterialTheme.colorScheme.background,
    showText: Boolean = false, //TODO text
    orientation: Orientation = Orientation.Horizontal,
    format: (f: Float) -> String = ValueFormat.Decimal(1).convertToString //TODO text
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
        Orientation.Vertical -> normalizedValue
    }

    val outline = MaterialTheme.colorScheme.outline

    Canvas(modifier = modifier, onDraw = {
        this.drawRect(color = bgColor, size = this.size)
        this.drawRect(color = outline, size = this.size, style = Stroke(width = 5.0f))
        if(normalizedValue != 0f) {
            this.drawRect(
                color = meterColor,
                size = Size(this.size.width * changeFactorX, this.size.height * changeFactorY)
            )
        }
    })
}