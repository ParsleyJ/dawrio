package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.device.Connection
import com.parsleyj.dawrio.daw.device.Device
import com.parsleyj.dawrio.daw.device.DeviceInput
import com.parsleyj.dawrio.daw.device.DeviceOutput

@Composable
fun SelectSourceButton(
    headerLabel: String,
    allDevices: List<Device>,
    deviceInput: DeviceInput,
    findConnection: () -> Connection?,
    onConnectionToOutput: (DeviceOutput?) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    val connIn = findConnection()

    if (showDialog) {
        ModulationDetailsDialog(
            devices = allDevices,
            deviceInput = deviceInput,
            selected = connIn?.from,
            onSelectSource = { output -> onConnectionToOutput(output) },
            onDismiss = { showDialog = false }
        )
    }

    val containerColor: Color
    val contentColor: Color
    if (connIn != null) {
        containerColor = MaterialTheme.colorScheme.secondaryContainer
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        containerColor = MaterialTheme.colorScheme.primaryContainer
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = headerLabel, textAlign = TextAlign.Center)
        Spacer(Modifier.size(8.dp))
        FilledIconButton(
            modifier = Modifier.size(64.dp),
            onClick = { showDialog = true },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = containerColor,
                contentColor = contentColor
            )
        ) {
            Image(
                imageVector = Icons.Filled.ExitToApp,
                modifier = Modifier.fillMaxSize(0.9f),
                contentScale = ContentScale.Inside,
                contentDescription = null,
                colorFilter = ColorFilter.tint(LocalContentColor.current)
            )
        }
        Spacer(Modifier.size(8.dp))
        Text(text = "", textAlign = TextAlign.Center)
    }
}