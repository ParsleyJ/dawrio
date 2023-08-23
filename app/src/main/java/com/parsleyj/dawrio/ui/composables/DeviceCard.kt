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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.parsleyj.dawrio.daw.device.Connection
import com.parsleyj.dawrio.daw.device.Device
import com.parsleyj.dawrio.daw.device.DeviceInput
import com.parsleyj.dawrio.daw.device.DeviceOutput


@Composable
fun DeviceCard(
    modifier: Modifier,
    showHeader: Boolean,
    device: Device,
    allDevices: List<Device>,
    allConnections: List<Connection>,
    onConnectChangeRequest: (input: DeviceInput, output: DeviceOutput?) -> Unit,
    onDeleteRequest: () -> Unit
) {

    val headerHeight = 64.dp
    val deviceName = device.label

    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (showHeader) {
            DeviceCardHeader(
                headerHeight = headerHeight,
                icon = ImageVector.vectorResource(id = device.icon),
                deviceName = deviceName,
                onDeleteRequest = onDeleteRequest,
            )
        }
        device.InnerGUI(allDevices, allConnections, onConnectChangeRequest)
    }

}

@Composable
fun DeviceCardHeader(
    headerHeight: Dp,
    icon: ImageVector,
    deviceName: String,
    onDeleteRequest: () -> Unit,
) {
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

            var showDialog by remember { mutableStateOf(false) }

            if (showDialog) {
                ConfirmationDialog(
                    text = "Delete $deviceName?",
                    onDismiss = { showDialog = false }
                ) {
                    onDeleteRequest()
                }
            }

            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = { showDialog = true }) {
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
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = deviceName,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

