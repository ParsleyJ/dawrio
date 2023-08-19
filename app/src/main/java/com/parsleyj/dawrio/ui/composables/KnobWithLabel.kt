package com.parsleyj.dawrio.ui.composables

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
import com.parsleyj.dawrio.daw.ValueFormat

@Composable
fun KnobWithLabel(
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    overValueRange: ClosedFloatingPointRange<Float> = 0f..0f,
    initialValue: Float = valueRange.start,
    overValue: Float = 0f,
    knobSize: Dp = 64.dp,
    format: (f: Float) -> String = ValueFormat.Decimal(1).convertToString
) {
    var value by remember { mutableStateOf(initialValue) }
    var text by remember(value, format) { mutableStateOf(format(value)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Knob(
            modifier = Modifier.size(knobSize),
            valueRange = valueRange,
            onValueChange = { f ->
                text = format(f)
                value = f
                onValueChange(f)
            },
            overValue = overValue,
            overValueRange = overValueRange,
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}