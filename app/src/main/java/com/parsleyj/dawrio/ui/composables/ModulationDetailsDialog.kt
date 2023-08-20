package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.parsleyj.dawrio.daw.device.Device
import com.parsleyj.dawrio.daw.device.DeviceInput
import com.parsleyj.dawrio.daw.device.DeviceOutput

@Composable
fun ModulationDetailsDialog(
    devices: List<Device>,
    deviceInput: DeviceInput,
    selected: DeviceOutput?,
    onSelectSource: (DeviceOutput?) -> Unit,
    onDismiss: () -> Unit,
) {

    val otherDevices = devices.filter { it.id != deviceInput.device.id }

    var selectedOption by remember { mutableStateOf(selected) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Modulation details",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "for ${deviceInput.device.label}/${deviceInput.name}",
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = MaterialTheme.colorScheme.outline)
                LazyColumn(modifier = Modifier.height(500.dp)) {
                    item {
                        DeviceSelectSourceDialogOption(
                            text = "None",
                            currentlySelected = selectedOption == null,
                            onSelect = { selectedOption = null }
                        )
                    }
                    itemsIndexed(
                        otherDevices
                            .flatMap { it.allOutputs }
                            .filter { deviceInput.compatibleWith(it) }
                    ) { _, outPort ->
                        DeviceSelectSourceDialogOption(
                            text = "$outPort",
                            currentlySelected = selectedOption == outPort,
                            onSelect = { selectedOption = outPort }
                        )
                    }
                }

                Divider(color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onDismiss()
                        }) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onSelectSource(selectedOption)
                        onDismiss()
                    }) {
                        Text(text = "Select")
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceSelectSourceDialogOption(
    text: String,
    currentlySelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onSelect() }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = currentlySelected,
            onClick = { onSelect() }
        )
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 10.dp)
                .clickable { onSelect() }
        )
    }
}