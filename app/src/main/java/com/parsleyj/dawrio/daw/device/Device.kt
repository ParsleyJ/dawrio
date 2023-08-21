package com.parsleyj.dawrio.daw.device

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.parsleyj.dawrio.R
import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.elementroute.ElementInPort
import com.parsleyj.dawrio.daw.elementroute.ElementOutPort
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.ui.composables.DeviceCard
import com.parsleyj.dawrio.util.NameGenerator
import java.util.UUID


class ChannelDescriptor(
    val valueFormat: ValueFormat = ValueFormat.NumericWithDecimals(1),
    val name: String? = null,
)

sealed class DeviceStreamType(vararg val channels: ChannelDescriptor) {
    object StereoAudio : DeviceStreamType(
        ChannelDescriptor(ValueFormat.AudioSamples, "Left side"),
        ChannelDescriptor(ValueFormat.AudioSamples, "Right side")
    )

    //TODO clip values between 0 and 1
    object Modulation : DeviceStreamType(
        ChannelDescriptor(ValueFormat.NumericWithDecimals(1, signed = false))
    )
}

interface RouteListBuilder {
    fun ElementInPort.addRouteTo(outPort: ElementOutPort): Route
    fun ElementOutPort.addRouteTo(inPort: ElementInPort): Route
}

class Connection(
    val from: DeviceOutput,
    val to: DeviceInput,
    val routes: List<Route>,
    val id: UUID = UUID.randomUUID()
) {
    override fun toString(): String = "$from --> $to"
}

class DeviceInput(
    val device: Device,
    val name: String,
    //TODO main inputs are automatically connected to
    // main outputs of previous device in chain (if compatible)
    val isMain: Boolean,
    val type: DeviceStreamType,
    val id: UUID = UUID.randomUUID(),
    val onConnect: RouteListBuilder.(output: DeviceOutput) -> Unit,
) {
    fun compatibleWith(output: DeviceOutput): Boolean {
        return this.type.channels.size == output.type.channels.size
    }

    fun buildConnection(output: DeviceOutput): Connection? {
        if (!compatibleWith(output)) {
            return null
        }
        val builtList = mutableListOf<Route>()
        object : RouteListBuilder {
            override fun ElementInPort.addRouteTo(outPort: ElementOutPort): Route {
                val result = this.connectionTo(outPort)
                builtList.add(result)
                return result
            }

            override fun ElementOutPort.addRouteTo(inPort: ElementInPort): Route {
                val result = this.connectionTo(inPort)
                builtList.add(result)
                return result
            }
        }.onConnect(output)
        return Connection(output, this, builtList)
    }

    fun findConnection(allConnections: List<Connection>): Connection? {
        return allConnections.find { it.to.id == this.id }
    }

    override fun toString(): String = "${device.label}/$name"
}

class DeviceOutput(
    val device: Device,
    val name: String,
    val isMain: Boolean,
    val type: DeviceStreamType,
    val id: UUID = UUID.randomUUID(),
    val provideOutPort: (channelIndex: Int) -> ElementOutPort,
) {
    override fun toString(): String = "${device.label}/$name"
}

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
        onConnectChangeRequest: (input: DeviceInput, output: DeviceOutput?) -> Unit
    ) {
        DeviceCard(
            modifier = modifier,
            device = this,
            showHeader = removable,
            allDevices = allDevices,
            allConnections = allConnections,
            onConnectChangeRequest = onConnectChangeRequest
        )
    }


}

