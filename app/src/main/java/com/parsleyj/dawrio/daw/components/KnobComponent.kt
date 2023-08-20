package com.parsleyj.dawrio.daw.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.element.ConstEmitter
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.ui.composables.KnobWithLabel
import com.parsleyj.dawrio.util.NameGenerator

class KnobComponent(
    val label: String = NameGenerator.newName("Knob"),
    val range: ClosedFloatingPointRange<Float> = 0f..1f,
    private val initialValue: Float = range.start,
    description: String = "",
) : Component {


    @Composable
    override fun InnerGUI(allElements: List<Element>, allRoutes: List<Route>) {
        val inPorts = element.outValue.findRoutes(allRoutes).map { it.inPort }
        val valueFormat = if (inPorts.size == 1) {
            inPorts[0].streamFormat
        } else {
            ValueFormat.NumericWithDecimals(1)
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp),
            ) {
                //TODO add detail dialog allowing to:
                // - select source (if possible)
                // - define range
                // - write value
                KnobWithLabel(
                    onValueChange = { element.value = it },
                    onOverValueScaleChange = {},
                    valueRange = range,
                    initialValue = initialValue,
                    format = valueFormat.convertToString
                )
            }
        }
    }


    override val element: ConstEmitter = ConstEmitter(initialValue, label, description)

}