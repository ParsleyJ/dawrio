package com.parsleyj.dawrio.daw.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.element.LFO
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.ui.composables.Meter
import com.parsleyj.dawrio.ui.composables.refreshingState
import com.parsleyj.dawrio.util.NameGenerator

private data class RangedValue(val value: Float, val range: ClosedFloatingPointRange<Float>)

class LFOComponent(
    label: String = NameGenerator.newName("LFO"),
    description: String = "",
) : Component {

    @Composable
    override fun InnerGUI(allElements: List<Element>, allRoutes: List<Route>) {

        Log.d("NOTUPDATINGRANGESBUG", "allRoutes InnerGUI: $allRoutes")

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp),
            ) {
                Text("LFO")
                Spacer(Modifier.width(8.dp))
                val outValue by refreshingState(read = {
                    RangedValue(
                        element.outValue.readValue(),
                        (element.inMinimum.readValue(allRoutes) ?: 0f)..
                                (element.inMaximum.readValue(allRoutes) ?: 1f),
                    )
                })
                Meter(
                    value = outValue.value,
                    range = outValue.range,
                    showText = true,
                    modifier = Modifier
                        .size(96.dp, 40.dp)
                        .padding(8.dp)
                )
            }
        }
    }


    override val element: LFO = LFO(label, description)
}