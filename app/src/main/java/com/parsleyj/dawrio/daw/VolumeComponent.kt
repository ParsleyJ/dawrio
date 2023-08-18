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
import com.parsleyj.dawrio.daw.elements.Volume
import com.parsleyj.dawrio.util.NameGenerator

class VolumeComponent(
    label: String = NameGenerator.newName("Volume"),
    description: String = "",
) : Component {
    override val element: Element = Volume(1.0f, label, description)

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
                Text("Volume")
            }
        }

    }
}