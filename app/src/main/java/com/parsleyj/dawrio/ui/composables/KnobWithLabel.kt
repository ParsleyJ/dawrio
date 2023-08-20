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
    onOverValueScaleChange: (Float) -> Unit = {},
    onClick: () -> Unit = {},
    headerText: String = "",
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    overValueRangeEnd: Float = 0f,
    showOverValue: Boolean = false,
    initialValue: Float = valueRange.start,
    overValue: Float = 0f,
    knobSize: Dp = 64.dp,
    centerIndicator: Boolean = false,
    format: (f: Float) -> String = ValueFormat.NumericWithDecimals(1).convertToString
) {
    var value by remember { mutableStateOf(initialValue) }
    var text by remember(value, format) { mutableStateOf(format(value)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = headerText, textAlign = TextAlign.Center)
        Spacer(Modifier.size(8.dp))
        Knob(
            modifier = Modifier.size(knobSize),
            initialValue = initialValue,
            valueRange = valueRange,
            onValueChange = { f ->
                text = format(f)
                value = f
                onValueChange(f)
            },
            overValue = overValue,
            initialOverValueRangeEnd = overValueRangeEnd,
            showOverValue = showOverValue,
            onOverValueScaleChange = onOverValueScaleChange,
            onClick = onClick,
            special = centerIndicator
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}