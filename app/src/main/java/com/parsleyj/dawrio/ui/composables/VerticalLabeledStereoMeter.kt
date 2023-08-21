package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun VerticalLabeledStereoMeter(
    topLabel: String,
    outMeterValueL: Float,
    outMeterValueR: Float,
    meterColor: Color,
    meterRange:ClosedFloatingPointRange<Float> = 0f..1f,
    meterSize: DpSize = DpSize(16.dp, 64.dp),
    combineValues: (Float, Float)->Float,
    valueFormatter: (Float) -> String = { String.format("%.1f", it) },
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp),
    ) {
        Text(text = topLabel, textAlign = TextAlign.Center)
        Spacer(Modifier.size(8.dp))
        Row {
            Meter(
                modifier = Modifier.size(meterSize),
                value = outMeterValueL,
                meterColor = meterColor,
                range = meterRange,
                orientation = Orientation.Vertical,
                showInnerText = false,
            )
            Meter(
                modifier = Modifier.size(meterSize),
                value = outMeterValueR,
                meterColor = meterColor,
                range = meterRange,
                orientation = Orientation.Vertical,
                showInnerText = false,
            )
        }
        Spacer(Modifier.size(8.dp))
        Text(
            text = valueFormatter(combineValues(outMeterValueL, outMeterValueR)),
            textAlign = TextAlign.Center
        )
    }
}