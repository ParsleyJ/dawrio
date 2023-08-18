package com.parsleyj.dawrio.daw.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.ui.composables.ElementCard

interface Component {
    @Composable
    fun ComponentGUI(
        allElements: List<Element>,
        allRoutes: List<Route>,
        onSetRoute: (input: ElementInPort, output: ElementOutPort?) -> Unit,
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