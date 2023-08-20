package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.parsleyj.dawrio.daw.device.Connection
import com.parsleyj.dawrio.daw.device.Device
import com.parsleyj.dawrio.daw.device.DeviceInput
import com.parsleyj.dawrio.daw.device.DeviceOutput


@Composable
fun DeviceCard(
    device: Device,
    allDevices: List<Device>,
    allConnections: List<Connection>,
    onConnectChangeRequest: (input: DeviceInput, output: DeviceOutput?) -> Unit
) {

    val headerHeight = 64.dp
    val deviceName = device.label

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        DeviceCardHeader(headerHeight, device.icon, deviceName)
        device.InnerGUI(allDevices, allConnections, onConnectChangeRequest)
    }

}

@Composable
fun DeviceCardHeader(
    headerHeight: Dp,
    icon: ImageVector,
    deviceName: String
) {
    //TODO hold and drag on header to rearrange devices
    //TODO click on icon to options
    //TODO long click on icon to select
    Card(
        modifier = Modifier
            .height(headerHeight)
            .fillMaxWidth(),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconSize = 0.75 * headerHeight
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                imageVector = icon,
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.contentColorFor(
                        MaterialTheme.colorScheme.background
                    )
                ),
                contentScale = ContentScale.Inside,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = deviceName,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

