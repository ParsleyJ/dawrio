package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import com.parsleyj.dawrio.daw.device.DeviceCreator

@Composable
fun AddDeviceDialog(
    deviceCreators: List<DeviceCreator>,
    onSelect: (DeviceCreator) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedOption by remember { mutableStateOf<DeviceCreator?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "New Device",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = MaterialTheme.colorScheme.outline)
                LazyColumn(modifier = Modifier.height(500.dp)) {
                    itemsIndexed(deviceCreators) { _, deviceCreator ->
                        AddDeviceDialogOption(
                            icon = ImageVector.vectorResource(id = deviceCreator.icon),
                            text = deviceCreator.name,
                            height= 48.dp,
                            currentlySelected = selectedOption == deviceCreator,
                            onSelect = { selectedOption = deviceCreator }
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
                        Text(text = "Cancel", color= LocalContentColor.current)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            selectedOption?.let {
                                onSelect(it)
                                onDismiss()
                            }
                        },
                        enabled = selectedOption != null
                    ) {
                        Text(text = "Select", color= LocalContentColor.current)
                    }
                }
            }
        }
    }
}

@Composable
fun AddDeviceDialogOption(
    icon: ImageVector,
    text: String,
    height: Dp,
    currentlySelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onSelect() }
            .height(height)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = currentlySelected,
            onClick = { onSelect() }
        )
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
                .size(.75 * height)
                .clip(CircleShape)
                .border(
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                    shape = CircleShape
                )
        )

        Text(
            text = text,
            modifier = Modifier
                .padding(start = 10.dp)
                .clickable { onSelect() }
        )
    }
}