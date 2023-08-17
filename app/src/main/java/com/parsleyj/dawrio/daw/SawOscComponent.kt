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
import com.parsleyj.dawrio.ui.composables.DeviceCard
import com.parsleyj.dawrio.util.NameGenerator

class SawOscComponent(
    val voice: Voice,
    label: String = NameGenerator.newName("SawOSC"),
    description: String = "",
    padding: Dp = 8.dp,
) : Component {
    override val gui: @Composable () -> Unit = {
        DeviceCard(
            voice = voice,
            device = this@SawOscComponent.device,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(padding),
                ) {
                    Text("Saw OSC")
                }
            }
        }

    }
    override val device: SawOsc = SawOsc(label, description)
}