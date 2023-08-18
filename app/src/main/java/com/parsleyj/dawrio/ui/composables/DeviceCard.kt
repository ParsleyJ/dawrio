package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import com.parsleyj.dawrio.daw.Device
import com.parsleyj.dawrio.daw.InPort
import com.parsleyj.dawrio.daw.OutPort
import com.parsleyj.dawrio.daw.Route


@Composable
fun DeviceCard(
    device: Device,
    allDevices: List<Device>,
    allRoutes: List<Route>,
    imageVector: ImageVector = Icons.Outlined.Warning,
    onSetRoute: (input: InPort, it: OutPort?)->Unit,
    innerContent: @Composable ColumnScope.() -> Unit
) {
    val headerHeight = 64.dp
    val inputsHeight = 64.dp
    val outputsHeight = 40.dp

    val inputs = device.allInputs
    val outputs = device.allOutputs
    val deviceName = device.label

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        DeviceCardHeader(headerHeight, imageVector, deviceName)
        if (inputs.isNotEmpty()) {
            DeviceCardInputSection(device, allDevices, allRoutes, inputsHeight, onSetRoute)
            Divider(color = MaterialTheme.colorScheme.outline)
        }

        innerContent()

        if (outputs.isNotEmpty()) {
            Divider(color = MaterialTheme.colorScheme.outline)
            DeviceCardOutputSection(device, allRoutes, outputsHeight)
        }
    }

}

@Composable
private fun DeviceCardHeader(
    headerHeight: Dp,
    imageVector: ImageVector,
    deviceName: String
) {
    Card(
        modifier = Modifier
            .height(headerHeight)
            .fillMaxWidth(),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconSize = 0.75 * headerHeight
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                imageVector = imageVector,
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

@Composable
private fun DeviceCardInputSection(
    device: Device,
    allDevices: List<Device>,
    allRoutes: List<Route>,
    inputsHeight: Dp,
    onSetRoute: (input: InPort, it: OutPort?)->Unit,
) {

    val otherDevices = allDevices.filter { it.handle != device.handle }
    val inputs = device.allInputs
    Box(
        modifier = Modifier
            .height(inputsHeight)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "IN")
            Spacer(modifier = Modifier.width(20.dp))
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.Center
            ) {
                for ((index, input) in inputs.withIndex()) {
                    var showDialog by remember { mutableStateOf(false) }
                    val routeIn = input.findRoute(allRoutes)
                    val hasRouteIn = routeIn != null
                    if (showDialog) {
                        SelectSourceDialog(
                            devices = otherDevices,
                            inPort = input,
                            selected = routeIn?.outPort,
                            onSelectSource = { onSetRoute(input, it) },
                            onDismiss = { showDialog = false },
                        )
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (hasRouteIn) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        ),
                        onClick = {
                            showDialog = true
                        },
                        border = BorderStroke(
                            width = 2.dp,
                            color = if (hasRouteIn) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                    ) {
                        Text(
                            text = input.portName,
                            color = if (hasRouteIn) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        )
                    }
                    if (index < inputs.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}



@Composable
private fun DeviceCardOutputSection(
    device: Device,
    allRoutes: List<Route>,
    outputsHeight: Dp,
) {
    val outputs = device.allOutputs
    Box(
        modifier = Modifier
            .height(outputsHeight)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "OUT")
            Spacer(modifier = Modifier.width(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for ((index, output) in outputs.withIndex()) {
                    val hasRoutesOut = output.findRoutes(allRoutes).isNotEmpty()
                    Text(
                        text = output.portName,
                        modifier = Modifier
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (hasRoutesOut) {
                                        MaterialTheme.colorScheme.onSecondaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    },
                                ),
                                shape = CircleShape
                            )
                            .padding(4.dp, 1.dp),
                    )
                    if (index < outputs.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun SelectSourceDialog(
    devices: List<Device>,
    inPort: InPort,
    selected: OutPort?,
    onSelectSource: (OutPort?) -> Unit,
    onDismiss: () -> Unit,
) {

    val otherDevices = devices.filter { it.handle != inPort.device.handle }

    var selectedOption by remember { mutableStateOf(selected) }
    Dialog(
        onDismissRequest = onDismiss,

        ) {
        Surface(
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Select source",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "for ${inPort.device.label}/${inPort.portName}",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(modifier = Modifier.height(500.dp)) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOption == null,
                                onClick = { selectedOption = null }
                            )
                            Text(text = "None", modifier = Modifier.padding(start = 10.dp))
                        }
                    }
                    itemsIndexed(
                        otherDevices.flatMap { it.allOutputs }
                    ) { _, outPort ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOption == outPort,
                                onClick = {
                                    selectedOption = outPort
                                }
                            )
                            Text(text = "$outPort", modifier = Modifier.padding(start = 10.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

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
fun KindRadioGroup(
    mItems: List<String>,
    selected: String,
    setSelected: (selected: String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        mItems.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == item,
                    onClick = {
                        setSelected(item)
                    },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(text = item, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }

}