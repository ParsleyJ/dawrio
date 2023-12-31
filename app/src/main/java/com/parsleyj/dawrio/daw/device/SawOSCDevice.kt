package com.parsleyj.dawrio.daw.device

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.R
import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.element.ConstEmitter
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.element.SawOsc
import com.parsleyj.dawrio.daw.element.ValueAdder
import com.parsleyj.dawrio.daw.element.Volume
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.ui.composables.ModulationAcceptingKnob
import com.parsleyj.dawrio.util.size

val sawOSCIcon = R.drawable.baseline_volume_up_24

val SawOSCCreator = DeviceCreator(
    name = "SawOSC",
    icon = sawOSCIcon,
    create = { SawOSCDevice() }
)

class SawOSCDevice : Device("SawOSC", icon = sawOSCIcon) {
    private val frequencyRange: ClosedFloatingPointRange<Float> = 20f..1_760f
    private val frequencyInitialValue: Float = 440f

    private val volumeRange: ClosedFloatingPointRange<Float> = 0f..1f
    private val volumeInitialValue: Float = 1f

    private val freqKnobElement = ConstEmitter("FreqKnob", frequencyInitialValue)
    private val freqModAdder = ValueAdder("FreqAdder", clippingRange = frequencyRange)
    private var freqModScale: Float
        get() = freqModAdder.scale
        set(value) {
            freqModAdder.scale = value
        }

    private val sawOSC = SawOsc("OSC")

    private val volumeKnobElement = ConstEmitter("OutVolKnob", volumeInitialValue)
    private val volumeModAdder = ValueAdder("VolumeAdder", clippingRange = volumeRange)
    private var volumeModScale: Float
        get() = volumeModAdder.scale
        set(value) {
            volumeModAdder.scale = value
        }
    private val outVolume = Volume("OutVolume", volumeInitialValue)

    init {
        freqModScale = frequencyRange.size
        volumeModScale = volumeRange.size
    }


    override val mainAudioOutputElement: Element get() = outVolume

    val freqModInput =
        DeviceInput(this, "Frequency Modulation", false, DeviceStreamType.Modulation) {
            it.provideOutPort(0).addRouteTo(freqModAdder.input2)
        }

    val volumeModInput =
        DeviceInput(this, "Out Volume Modulation", false, DeviceStreamType.Modulation) {
            it.provideOutPort(0).addRouteTo(volumeModAdder.input2)
        }

    val stereoOutput = DeviceOutput(this, "Audio", true, DeviceStreamType.StereoAudio) {
        when (it) {
            0 -> outVolume.outAudioL
            1 -> outVolume.outAudioR
            else -> throw RuntimeException("Invalid channel: $it")
        }
    }


    override val internalRoutes: List<Route>
        get() = listOf(
            freqKnobElement.outValue.connectionTo(freqModAdder.input1),
            freqModAdder.outValue.connectionTo(sawOSC.inFrequency),
            volumeKnobElement.outValue.connectionTo(volumeModAdder.input1),
            volumeModAdder.outValue.connectionTo(outVolume.inAmount),
            sawOSC.outAudioL.connectionTo(outVolume.inAudioL),
            sawOSC.outAudioR.connectionTo(outVolume.inAudioR)
        )
    override val allOutputs: List<DeviceOutput>
        get() = listOf(stereoOutput)
    override val allInputs: List<DeviceInput>
        get() = listOf(freqModInput, volumeModInput)
    override val allElements: List<Element>
        get() = listOf(
            sawOSC,
            freqKnobElement,
            freqModAdder,
            volumeKnobElement,
            volumeModAdder,
            outVolume,
        )


    @Composable
    override fun DeviceScreen(
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
                ModulationAcceptingKnob(
                    headerText = "Frequency",
                    modulationInput = freqModInput,
                    findConnection = { freqModInput.findConnection(allConnections) },
                    allDevices = allDevices,
                    initialValue = freqKnobElement.value,
                    valueRange = frequencyRange,
                    valueFormat = ValueFormat.Frequency,
                    onValueChange = { freqKnobElement.value = it },
                    getModScale = { freqModScale },
                    onModScaleChange = { freqModScale = it * frequencyRange.size }
                ) { onConnectChangeRequest(freqModInput, it) }

                ModulationAcceptingKnob(
                    headerText = "Volume",
                    modulationInput = volumeModInput,
                    findConnection = { volumeModInput.findConnection(allConnections) },
                    allDevices = allDevices,
                    initialValue = volumeKnobElement.value,
                    valueRange = volumeRange,
                    valueFormat = ValueFormat.NumericWithDecimals(1),
                    onValueChange = { volumeKnobElement.value = it },
                    getModScale = { volumeModScale },
                    onModScaleChange = { volumeModScale = it * volumeRange.size }
                ) { onConnectChangeRequest(volumeModInput, it) }
            }
        }
    }

}

