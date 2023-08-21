package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.device.DeviceCreator

@Composable
fun AddDeviceButton(
    modifier: Modifier,
    deviceCreators: List<DeviceCreator>,
    onCreateDevice: (DeviceCreator) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddDeviceDialog(
            deviceCreators = deviceCreators,
            onSelect = onCreateDevice,
            onDismiss = { showDialog = false }
        )
    }

    FilledIconButton(
        modifier = modifier.size(64.dp, 32.dp),
        onClick = { showDialog = true },
    ) {
        Image(
            imageVector = Icons.Outlined.Add,
            modifier = Modifier.fillMaxSize(0.9f),
            contentScale = ContentScale.Inside,
            contentDescription = null,
            colorFilter = ColorFilter.tint(LocalContentColor.current)
        )
    }
}