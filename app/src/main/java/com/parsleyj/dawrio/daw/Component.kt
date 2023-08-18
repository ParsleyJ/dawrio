package com.parsleyj.dawrio.daw

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.parsleyj.dawrio.ui.composables.ElementCard


interface Component {
    @Composable
    fun ComponentGUI(
        allElements: List<Element>,
        allRoutes: List<Route>,
        onSetRoute: (input: InPort, output: OutPort?) -> Unit,
    ) {
        ElementCard(
            element = element,
            allElements = allElements,
            allRoutes = allRoutes,
            imageVector = icon,
            onSetRoute = onSetRoute,
        ) {
            InnerGUI(allElements, allRoutes)
        }
    }

    @Composable
    fun InnerGUI(allElements: List<Element>, allRoutes: List<Route>)

    val icon: ImageVector get() = Icons.Outlined.Warning

    val element: Element
}