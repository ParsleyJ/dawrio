package com.parsleyj.dawrio.daw

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.device.ConstEmitter
import com.parsleyj.dawrio.ui.composables.DeviceCard
import com.parsleyj.dawrio.ui.composables.KnobWithLabel
import com.parsleyj.dawrio.util.NameGenerator

class KnobComponent(
    val voice: Voice,
    label: String = NameGenerator.newName("Knob"),
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    initialValue: Float = range.start,
    description: String = "",
    padding: Dp = 8.dp,
) : Component {
    override val gui: @Composable () -> Unit = {
        DeviceCard(
            voice = voice,
            device = this@KnobComponent.device,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(padding),
                ) {
                    KnobWithLabel(
                        onValueChange = { device.value = it },
                        initialValue = initialValue,
                        valueRange = range,
                        padding = padding,
                    )
                }
            }
        }

    }
    override val device: ConstEmitter = ConstEmitter(0.0f, label, description)

}