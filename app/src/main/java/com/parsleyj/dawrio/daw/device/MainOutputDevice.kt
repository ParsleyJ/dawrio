package com.parsleyj.dawrio.daw.device

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.R
import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.element.ConstEmitter
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.element.Volume
import com.parsleyj.dawrio.daw.element.VolumeMeter
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.ui.composables.SelectSourceButton
import com.parsleyj.dawrio.ui.composables.VerticalLabeledMeter
import com.parsleyj.dawrio.ui.composables.VerticalLabeledStereoMeter
import com.parsleyj.dawrio.ui.composables.refreshingState


class MainOutputDevice(label: String) : Device(
    "Main Output",
    label = label,
    icon = R.drawable.baseline_adjust_24,
    removable = false,
) {

    val volumeConst: ConstEmitter = ConstEmitter("VolumeAmount", 1f)
    val volume: Volume = Volume("MainOutputVolume", 1f)

    val volumeMeter: VolumeMeter = VolumeMeter("VolumeMeter",
//        300 /* <- just 300 samples */,
//        13230 /* <- 300ms */,
//        2205 /* <- 50ms */,
        441 /* <- 10ms */,
//        882 /* <- 20ms */,

    )

    val stereoInput: DeviceInput = DeviceInput(
        this,
        "Audio Input",
        true,
        DeviceStreamType.StereoAudio,
    ) {
        it.provideOutPort(0).addRouteTo(volume.inAudioL)
        it.provideOutPort(1).addRouteTo(volume.inAudioR)
    }

    val stereoOutput: DeviceOutput = DeviceOutput(
        this,
        "Audio Output",
        true,
        DeviceStreamType.StereoAudio
    ) {
        when (it) {
            0 -> volume.outAudioL
            1 -> volume.outAudioR
            else -> throw RuntimeException("Invalid channel: $id")
        }
    }

    override val allOutputs: List<DeviceOutput>
        get() = listOf(stereoOutput)
    override val allInputs: List<DeviceInput>
        get() = listOf(stereoInput)
    override val allElements: List<Element>
        get() = listOf(
            volume,
            volumeConst,
            volumeMeter
        )
    override val internalRoutes: List<Route>
        get() = listOf(
            volumeConst.outValue.connectionTo(volume.inAmount),
            volume.outAudioL.connectionTo(volumeMeter.inAudioL),
            volume.outAudioR.connectionTo(volumeMeter.inAudioR),
        )
    override val mainAudioOutputElement: Element get() = volume

    @Composable
    override fun InnerGUI(
        allDevices: List<Device>,
        allConnections: List<Connection>,
        onConnectChangeRequest: (input: DeviceInput, output: DeviceOutput?) -> Unit
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp),
            ) {
                SelectSourceButton(
                    headerLabel = "Audio Source",
                    allDevices = allDevices,
                    deviceInput = stereoInput,
                    findConnection = { stereoInput.findConnection(allConnections) },
                    onConnectionToOutput = { onConnectChangeRequest(stereoInput, it) }
                )

                val outputStereo by refreshingState(read = {
                    Pair(
                        volumeMeter.outDecibelsL.readValue(),
                        volumeMeter.outDecibelsR.readValue()
                    )
                })

                VerticalLabeledMeter(
                    topLabel = "",
                    outMeterValue = outputStereo.first,
                    meterColor = MaterialTheme.colorScheme.primary,
                    meterRange = 0f..1f,
                    valueFormatter = ValueFormat.NumericWithDecimals(2).convertToString
                )
            }
        }
    }

}