package com.parsleyj.dawrio.daw.device

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.parsleyj.dawrio.R
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.ui.composables.DeviceCard
import com.parsleyj.dawrio.util.NameGenerator
import java.util.UUID


abstract class Device(
    val deviceName: String,
    var label: String = NameGenerator.newName(deviceName),
    val description: String = "",
    @DrawableRes val icon: Int = R.drawable.baseline_warning_24,
    val removable: Boolean = true,
    val id: UUID = UUID.randomUUID() //Use value classes){}
) {

    abstract val allOutputs: List<DeviceOutput>
    abstract val allInputs: List<DeviceInput>
    abstract val allElements: List<Element>
    abstract val internalRoutes: List<Route>
    abstract val mainAudioOutputElement: Element?

    @Composable
    abstract fun InnerGUI(
        allDevices: List<Device>,
        allConnections: List<Connection>,
        onConnectChangeRequest: (input: DeviceInput, output: DeviceOutput?) -> Unit
    )


    @Composable
    fun DeviceGUI(
        modifier: Modifier,
        allDevices: List<Device>,
        allConnections: List<Connection>,
        onConnectChangeRequest: (input: DeviceInput, output: DeviceOutput?) -> Unit,
        onDeleteRequest: ()->Unit
    ) {
        DeviceCard(
            modifier = modifier,
            device = this,
            showHeader = removable,
            allDevices = allDevices,
            allConnections = allConnections,
            onConnectChangeRequest = onConnectChangeRequest,
            onDeleteRequest = onDeleteRequest,
        )
    }


}

