package com.parsleyj.dawrio.daw

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.device.ConstEmitter
import com.parsleyj.dawrio.daw.device.SawOsc
import com.parsleyj.dawrio.daw.device.SquareOsc
import com.parsleyj.dawrio.ui.composables.DeviceCard
import com.parsleyj.dawrio.util.NameGenerator

class SquareOscComponent(
    val voice: Voice,
    label: String = NameGenerator.newName("SquareOSC"),
    description: String = "",
    padding: Dp = 8.dp,
) : Component {
    override val gui: @Composable () -> Unit = {
        DeviceCard(
            voice = voice,
            device = this@SquareOscComponent.device,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(padding),
                ) {
                    Text("Square OSC")
                }
            }
        }

    }
    override val device: SquareOsc = SquareOsc(label, description)
}