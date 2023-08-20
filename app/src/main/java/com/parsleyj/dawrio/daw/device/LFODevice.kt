package com.parsleyj.dawrio.daw.device

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.ValueFormat
import com.parsleyj.dawrio.daw.element.ConstEmitter
import com.parsleyj.dawrio.daw.element.Element
import com.parsleyj.dawrio.daw.element.LFO
import com.parsleyj.dawrio.daw.element.LFOWaveTypes
import com.parsleyj.dawrio.daw.element.ValueAdder
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.ui.composables.KnobWithLabel
import com.parsleyj.dawrio.ui.composables.Meter
import com.parsleyj.dawrio.ui.composables.ModulationAcceptingKnob
import com.parsleyj.dawrio.ui.composables.refreshingState
import com.parsleyj.dawrio.util.size
import com.parsleyj.dawrio.util.toFloatRange


class LFODevice : Device("LFO", icon = Icons.Outlined.ExitToApp) {
    private val frequencyRange: ClosedFloatingPointRange<Float> = 0f..20f
    private val frequencyInitialValue: Float = 1f

    private val typeInitialValue: Float = LFOWaveTypes.Sin.ordinal.toFloat()

    private val maxValueRange: ClosedFloatingPointRange<Float> = 0f..1f
    private val maxValueInitialValue: Float = 1f

    private val freqKnobElement = ConstEmitter(frequencyInitialValue, "FreqKnob")
    private val freqModAdder = ValueAdder("FreqAdder", clippingRange = frequencyRange)
    private var freqModScale: Float
        get() = freqModAdder.scale
        set(value) {
            freqModAdder.scale = value
        }

    private val typeKnobElement = ConstEmitter(typeInitialValue, "TypeKnob")


    private val maxValueKnobElement = ConstEmitter(maxValueInitialValue, "MaxValKnob")
    private val maxValueModAdder = ValueAdder("MaxValKnob", clippingRange = maxValueRange)
    private var maxValueModScale: Float
        get() = maxValueModAdder.scale
        set(value) {
            maxValueModAdder.scale = value
        }

    private val lfo = LFO("LFO")


    init {
        freqModScale = frequencyRange.size
        maxValueModScale = maxValueRange.size
    }

    // LFO does not emit audio
    override val mainAudioOutputElement: Element? = null


    val freqModInput = DeviceInput(
        this,
        "Frequency Modulation",
        false,
        DeviceStreamType.Modulation
    ) {
        it.provideOutPort(0).addRouteTo(freqModAdder.input2)
    }

    val maxValueModInput = DeviceInput(
        this,
        "Max Value Modulation",
        false,
        DeviceStreamType.Modulation
    ) {
        it.provideOutPort(0).addRouteTo(maxValueModAdder.input2)
    }

    val modulationOutput = DeviceOutput(this, "Modulation", true, DeviceStreamType.Modulation) {
        when (it) {
            0 -> lfo.outValue
            else -> throw RuntimeException("Invalid channel: $it")
        }
    }


    override val internalRoutes: List<Route>
        get() = listOf(
            freqKnobElement.outValue.connectionTo(freqModAdder.input1),
            freqModAdder.outValue.connectionTo(lfo.inFrequency),
            typeKnobElement.outValue.connectionTo(lfo.inType),
            maxValueKnobElement.outValue.connectionTo(maxValueModAdder.input1),
            maxValueModAdder.outValue.connectionTo(lfo.inMaximum),
        )
    override val allOutputs: List<DeviceOutput>
        get() = listOf(modulationOutput)
    override val allInputs: List<DeviceInput>
        get() = listOf(freqModInput, maxValueModInput)
    override val allElements: List<Element>
        get() = listOf(
            lfo,
            freqKnobElement,
            freqModAdder,
            typeKnobElement,
            maxValueKnobElement,
            maxValueModAdder,
        )


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
                ModulationAcceptingKnob(
                    headerText = "Frequency",
                    getModScale = { freqModScale },
                    modulationInput = freqModInput,
                    findConnection = { freqModInput.findConnection(allConnections) },
                    allDevices = allDevices,
                    initialValue = frequencyInitialValue,
                    valueRange = frequencyRange,
                    valueFormat = ValueFormat.Frequency,

                    onValueChange = { freqKnobElement.value = it },
                    onModScaleChange = { freqModScale = it * frequencyRange.size },
                    refreshingOvervalue = { freqModAdder.outValue.readValue() },
                    onConnectChangeRequest = { onConnectChangeRequest(freqModInput, it) }
                )

                KnobWithLabel(
                    headerText = "Type",
                    onValueChange = { typeKnobElement.value = it },
                    valueRange = LFOWaveTypes.values().size.toFloatRange(),
                    initialValue = typeInitialValue,
                    format = ValueFormat.Options<LFOWaveTypes>().convertToString
                )

                ModulationAcceptingKnob(
                    headerText = "Max Value",
                    getModScale = { maxValueModScale },
                    modulationInput = maxValueModInput,
                    findConnection = { maxValueModInput.findConnection(allConnections) },
                    allDevices = allDevices,
                    initialValue = maxValueInitialValue,
                    valueRange = maxValueRange,
                    valueFormat = ValueFormat.NumericWithDecimals(1),
                    onValueChange = { maxValueKnobElement.value = it },
                    refreshingOvervalue = { maxValueModAdder.outValue.readValue() },
                    onModScaleChange = { maxValueModScale = it * maxValueRange.size },
                    onConnectChangeRequest = { onConnectChangeRequest(maxValueModInput, it) }
                )

                val outMeterValue by refreshingState(read = {
                    modulationOutput.provideOutPort(0).readValue()
                })

                //TODO move into custom component
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(text = "Output", textAlign = TextAlign.Center)
                    Spacer(Modifier.size(8.dp))
                    Meter(
                        modifier = Modifier.size(16.dp, 64.dp),
                        value = outMeterValue,
                        meterColor = MaterialTheme.colorScheme.secondary,
                        orientation = Orientation.Vertical,
                        showText = false,
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = ValueFormat.NumericWithDecimals(1).convertToString(outMeterValue),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }

}

