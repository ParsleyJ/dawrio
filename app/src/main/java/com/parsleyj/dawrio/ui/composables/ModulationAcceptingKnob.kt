package com.parsleyj.dawrio.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.device.Connection
import com.parsleyj.dawrio.daw.device.Device
import com.parsleyj.dawrio.daw.device.DeviceInput
import com.parsleyj.dawrio.daw.device.DeviceOutput



@Composable
fun ModulationAcceptingKnob(
    headerText: String,
    modulationInput: DeviceInput,
    findConnection: () -> Connection?,
    allDevices: List<Device>,
    initialValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    valueFormat: ValueFormat,
    onValueChange: (Float) -> Unit,
    getModScale: () ->  Float,
    onModScaleChange: (Float)->Unit,
    refreshingOvervalue: () -> Float,
    onConnectChangeRequest: (DeviceOutput?) -> Unit,
) {


    var showFreqInDialog by remember { mutableStateOf(false) }

    val connIn = findConnection()

    if (showFreqInDialog) {
        ModulationDetailsDialog(
            devices = allDevices,
            deviceInput = modulationInput,
            selected = connIn?.from,
            onSelectSource = onConnectChangeRequest,
            onDismiss = { showFreqInDialog = false }
        )
    }

    //TODO enable overValue metering
//    val overValue: State<Float> = if (connIn != null) {
//        refreshingState(read = refreshingOvervalue)
//    } else {
//        remember { mutableStateOf(0f) }
//    }

    KnobWithLabel(
        headerText = headerText,
        onValueChange = onValueChange,
        valueRange = valueRange,
        initialValue = initialValue,
        format = valueFormat.convertToString,
//        overValue = overValue.value,
        overValue = 0f,
        overValueRangeEnd = getModScale(),
        showOverValue = connIn != null,
        centerIndicator = true,
        onClick = { showFreqInDialog = true },
        onOverValueScaleChange = onModScaleChange
    )

}