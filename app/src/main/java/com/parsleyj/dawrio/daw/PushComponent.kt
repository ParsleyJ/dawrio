package com.parsleyj.dawrio.daw

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.R
import com.parsleyj.dawrio.daw.device.ConstEmitter
import com.parsleyj.dawrio.ui.composables.BottomLabeled
import com.parsleyj.dawrio.ui.composables.DeviceCard
import com.parsleyj.dawrio.ui.composables.KnobWithLabel
import com.parsleyj.dawrio.ui.composables.PushGateButton
import com.parsleyj.dawrio.util.NameGenerator

class PushComponent(
    val voice: Voice,
    label: String = NameGenerator.newName("Button"),
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    description: String = "",
    padding: Dp = 8.dp,
) : Component {
    override val gui: @Composable () -> Unit = {
        DeviceCard(
            voice = voice,
            device = device
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(padding)
                ) {
                    BottomLabeled(label = "Hear") {
                        PushGateButton(
                            onStartPush = { device.value = range.endInclusive },
                            onStopPush = { device.value = range.start }
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(0.9f),
                                contentScale = ContentScale.Inside,
                                painter = painterResource(R.drawable.outline_fiber_manual_record_24),
                                contentDescription = stringResource(id = R.string.gate_button),
                                colorFilter = ColorFilter.tint(LocalContentColor.current)
                            )
                        }
                    }
                }
            }
        }

    }
    override val device: ConstEmitter = ConstEmitter(0.0f, label, description)
}