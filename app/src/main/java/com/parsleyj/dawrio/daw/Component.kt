package com.parsleyj.dawrio.daw

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.parsleyj.dawrio.ui.composables.DeviceCard


interface Component {
    @Composable
    fun ComponentGUI(
        allDevices: List<Device>,
        allRoutes: List<Route>,
        onSetRoute: (input: InPort, output: OutPort?) -> Unit,
    ) {
        DeviceCard(
            device = device,
            allDevices = allDevices,
            allRoutes = allRoutes,
            imageVector = icon,
            onSetRoute = onSetRoute,
        ) {
            InnerGUI(allDevices, allRoutes)
        }
    }

    @Composable
    fun InnerGUI(allDevices: List<Device>, allRoutes: List<Route>)

    val icon: ImageVector get() = Icons.Outlined.Warning

    val device: Device
}