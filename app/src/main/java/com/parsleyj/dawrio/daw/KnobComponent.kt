package com.parsleyj.dawrio.daw

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.parsleyj.dawrio.daw.device.ConstEmitter
import com.parsleyj.dawrio.ui.composables.KnobWithLabel
import com.parsleyj.dawrio.util.NameGenerator


class KnobComponent(
    val label: String = NameGenerator.newName("Knob"),
    val range: ClosedFloatingPointRange<Float> = 0f..1f,
    private val initialValue: Float = range.start,
    description: String = "",
) : Component {


    @Composable
    override fun InnerGUI(allDevices: List<Device>, allRoutes: List<Route>) {
        val inPorts = device.outPort.findRoutes(allRoutes).map { it.inPort }
        val valueFormat = if (inPorts.size == 1) {
            inPorts[0].streamFormat
        } else {
            ValueFormat.Decimal(1)
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp),
            ) {

                KnobWithLabel(
                    onValueChange = { device.value = it },
                    initialValue = initialValue,
                    valueRange = range,
                    format = valueFormat.convertToString
                )
            }
        }
    }


    override val device: ConstEmitter = ConstEmitter(initialValue, label, description)

}