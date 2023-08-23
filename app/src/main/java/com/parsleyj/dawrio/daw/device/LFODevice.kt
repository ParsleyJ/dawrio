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
import com.parsleyj.dawrio.daw.element.LFO
import com.parsleyj.dawrio.daw.element.LFOWaveTypes
import com.parsleyj.dawrio.daw.element.ValueAdder
import com.parsleyj.dawrio.daw.elementroute.Route
import com.parsleyj.dawrio.ui.composables.KnobWithLabel
import com.parsleyj.dawrio.ui.composables.ModulationAcceptingKnob
import com.parsleyj.dawrio.ui.composables.VerticalLabeledMeter
import com.parsleyj.dawrio.ui.composables.refreshingState
import com.parsleyj.dawrio.util.size
import com.parsleyj.dawrio.util.toFloatRange


val lfoIcon =R.drawable.baseline_waves_24

val LFOCreator =
    DeviceCreator("LFO", lfoIcon) {
        LFODevice()
    }

class LFODevice : Device("LFO", icon = lfoIcon) {
    private val frequencyRange: ClosedFloatingPointRange<Float> = 0f..20f
    private val frequencyInitialValue: Float = 1f

    private val typeInitialValue: Float = LFOWaveTypes.Sin.ordinal.toFloat()

    private val maxValueRange: ClosedFloatingPointRange<Float> = 0f..1f
    private val maxValueInitialValue: Float = 1f

    private val freqKnobElement = ConstEmitter("FreqKnob", frequencyInitialValue)
    private val freqModAdder = ValueAdder("FreqAdder", clippingRange = frequencyRange)
    private var freqModScale: Float
        get() = freqModAdder.scale
        set(value) {
            freqModAdder.scale = value
        }

    private val typeKnobElement = ConstEmitter("TypeKnob", typeInitialValue)


    private val maxValueKnobElement = ConstEmitter("MaxValKnob", maxValueInitialValue)
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

                KnobWithLabel(
                    headerText = "Type",
                    onValueChange = { typeKnobElement.value = it },
                    valueRange = LFOWaveTypes.values().size.toFloatRange(),
                    initialValue = typeKnobElement.value,
                    format = ValueFormat.Options<LFOWaveTypes>().convertToString
                )

                ModulationAcceptingKnob(
                    headerText = "Max Value",
                    modulationInput = maxValueModInput,
                    findConnection = { maxValueModInput.findConnection(allConnections) },
                    allDevices = allDevices,
                    initialValue = maxValueKnobElement.value,
                    valueRange = maxValueRange,
                    valueFormat = ValueFormat.NumericWithDecimals(1),
                    onValueChange = { maxValueKnobElement.value = it },
                    getModScale = { maxValueModScale },
                    onModScaleChange = { maxValueModScale = it * maxValueRange.size }
                ) { onConnectChangeRequest(maxValueModInput, it) }

                val outMeterValue by refreshingState(read = {
                    modulationOutput.provideOutPort(0).readValue()
                })


                VerticalLabeledMeter("Output", outMeterValue, MaterialTheme.colorScheme.secondary)

            }
        }
    }

}


