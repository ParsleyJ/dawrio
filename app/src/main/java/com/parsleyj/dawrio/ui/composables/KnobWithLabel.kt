package com.parsleyj.dawrio.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun KnobWithLabel(
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    initialValue: Float = valueRange.start,
    padding: Dp = 8.dp,
    size: Dp = 64.dp
) {
    fun format(f:Float): String {
        return String.format("%.1f Hz", f)
    }

    var value by remember { mutableStateOf(initialValue) }
    var text by remember { mutableStateOf(format(value)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(padding)
    ) {
        Knob(
            modifier = Modifier.size(size),
            valueRange = valueRange,
            onValueChange = { f ->
                text = format(f)
                value = f
                onValueChange(f)
            },
        )
        Spacer(Modifier.size(padding))
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}