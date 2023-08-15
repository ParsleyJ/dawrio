package com.parsleyj.dawrio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.device.ConstEmitter
import com.parsleyj.dawrio.daw.device.LFO
import com.parsleyj.dawrio.daw.device.SawOsc
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.device.SinOsc
import com.parsleyj.dawrio.daw.device.SquareOsc
import com.parsleyj.dawrio.daw.device.Volume
import com.parsleyj.dawrio.ui.Knob
import com.parsleyj.dawrio.ui.theme.DawrioTheme

class MainActivity : ComponentActivity() {

    lateinit var constEmitter: ConstEmitter

    /* TODO
        - Volume
        - Mixer
        - Envelope
        - Manual Gate
        - Group
        - Clock
        - Visual Feedback
        - Haptic Feedback
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val voice = Voice()
        constEmitter = ConstEmitter(1.0f, "Lfo Frq")
        val minConst = ConstEmitter(0f, "Min Vol")
        val maxConst = ConstEmitter(1f, "Max Vol")
        val lfoTypeConst = ConstEmitter(2f, "LFO type")
        val oscFreq = ConstEmitter(220f, "Note:A2")
        val lfo = LFO("LFO")
        val osc = SawOsc("MyOsc")
        val volume = Volume(1.0f, "Volume")
        val conn0L = osc.outAudioL.connectionTo(volume.inAudioL)
        val conn0R = osc.outAudioR.connectionTo(volume.inAudioR)
        val conn1 = constEmitter.outPort.connectionTo(lfo.inFrequency)
        val conn2 = oscFreq.outPort.connectionTo(osc.inFrequency)
        val conn3 = minConst.outPort.connectionTo(lfo.inMinimum)
        val conn4 = maxConst.outPort.connectionTo(lfo.inMaximum)
        val conn5 = lfoTypeConst.outPort.connectionTo(lfo.inType)
        val conn6 = lfo.outValue.connectionTo(volume.inAmount)
        voice.setLayout(
            listOf(volume, osc, constEmitter, lfoTypeConst, maxConst, minConst, lfo),
            listOf(conn0L, conn0R, conn1, conn2, conn3, conn4, conn5, conn6)
        )

        setVoice(voice.handle.toAddress)

        startEngine()

        setContent {
            DawrioTheme {
                MainBody()
            }
        }
    }

    override fun onDestroy() {
        stopEngine()
        super.onDestroy()
    }

    @Composable
    fun MainBody() {
        val padding = 16.dp
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                DeviceCard(padding)
            }
        }
    }

    @Composable
    private fun DeviceCard(padding: Dp) {
        val interactionSource = remember { MutableInteractionSource() }
        val text = remember { mutableStateOf(". Hz") }
        val maxVal = 2.0f * 4f

        Card(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(2.5.dp, MaterialTheme.colorScheme.primary)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(padding)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(padding)
                    ) {
                        Knob(
                            modifier = Modifier.size(64.dp),
                            valueRange = 0f..maxVal,
                            onValueChange = { f ->
                                constEmitter.value = f
                                text.value = String.format("%.1f Hz", f)
                            },
                        )
                        Spacer(Modifier.size(padding))
                        val textInLabel by text
                        Text(
                            text = textInLabel,
                            textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = {},
                        interactionSource = interactionSource
                    ) {
                        Text(text = "BEEP")
                    }

                }
            }
        }

        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect {
                when (it) {
                    is PressInteraction.Press -> {
                        beepEvent(true)
                    }

                    is PressInteraction.Release, is PressInteraction.Cancel -> {
                        beepEvent(false)
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainBodyPreview() {
        MainBody()
    }

    /**
     * A native method that is implemented by the 'dawrio' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun setVoice(address: Long)

    external fun startEngine()

    external fun stopEngine()

    external fun beepEvent(on: Boolean)

    companion object {
        // Used to load the 'dawrio' library on application startup.
        init {
            System.loadLibrary("dawrio")
        }
    }
}