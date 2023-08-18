package com.parsleyj.dawrio.daw

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.parsleyj.dawrio.R
import com.parsleyj.dawrio.daw.device.ConstEmitter
import com.parsleyj.dawrio.ui.composables.BottomLabeled
import com.parsleyj.dawrio.ui.composables.PushGateButton
import com.parsleyj.dawrio.util.NameGenerator

class PushComponent(
    label: String = NameGenerator.newName("Button"),
    private val range: ClosedFloatingPointRange<Float> = 0f..1f,
    description: String = "",
) : Component {

    @Composable
    override fun InnerGUI(allDevices: List<Device>, allRoutes: List<Route>) {

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
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
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(LocalContentColor.current)
                        )
                    }
                }
            }
        }

    }


    override val device: ConstEmitter = ConstEmitter(0.0f, label, description)
}