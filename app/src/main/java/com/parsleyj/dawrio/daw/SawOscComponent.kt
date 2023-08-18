package com.parsleyj.dawrio.daw

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.elements.SawOsc
import com.parsleyj.dawrio.util.NameGenerator

class SawOscComponent(
    val voice: Voice,
    label: String = NameGenerator.newName("SawOSC"),
    description: String = "",
) : Component {
    override val element: Element = SawOsc(label, description)

    @Composable
    override fun InnerGUI(allElements: List<Element>, allRoutes: List<Route>) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp),
            ) {
                Text("Saw OSC")
            }
        }

    }
}