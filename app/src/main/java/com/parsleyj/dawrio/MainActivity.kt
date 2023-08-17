package com.parsleyj.dawrio

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.parsleyj.dawrio.daw.Component
import com.parsleyj.dawrio.daw.Device
import com.parsleyj.dawrio.daw.KnobComponent
import com.parsleyj.dawrio.daw.LFOComponent
import com.parsleyj.dawrio.daw.PushComponent
import com.parsleyj.dawrio.daw.SawOscComponent
import com.parsleyj.dawrio.daw.SquareOscComponent
import com.parsleyj.dawrio.daw.Voice
import com.parsleyj.dawrio.daw.VolumeComponent
import com.parsleyj.dawrio.daw.device.ConstEmitter
import com.parsleyj.dawrio.daw.device.LFO
import com.parsleyj.dawrio.daw.device.constEmitter
import com.parsleyj.dawrio.daw.device.lfo
import com.parsleyj.dawrio.daw.device.sawOsc
import com.parsleyj.dawrio.daw.device.volume
import com.parsleyj.dawrio.ui.composables.BottomLabeled
import com.parsleyj.dawrio.ui.composables.DeviceCard
import com.parsleyj.dawrio.ui.composables.KnobWithLabel
import com.parsleyj.dawrio.ui.composables.PushGateButton
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

        val components = mutableListOf<Component>()

        val voice = Voice().edit {
            listOf(
                KnobComponent(this.voice, range = 55f..880f),
                KnobComponent(this.voice, range = 0f..8f),
//                KnobComponent(this.voice, range = 0f..8f),
//                KnobComponent(this.voice, range = 0f..8f),
//                PushComponent(this.voice, range = 0f..1f),
                LFOComponent(this.voice),
                SquareOscComponent(this.voice),
                VolumeComponent(this.voice)
            ).forEach {
                components.add(it)
                addDevice(it.device)
            }
//            knob.device.outPort.connect(osc.device.inFrequency)
//            osc.device.outAudioL.connect(vol.device.inAudioL)
//            osc.device.outAudioR.connect(vol.device.inAudioR)


//            constEmitter = constEmitter(1.0f, "Lfo Frq")
//            val minConst = constEmitter(0f, "Min Vol")
//            val maxConst = constEmitter(1f, "Max Vol")
//            val lfoTypeConst = constEmitter(2f, "LFO type")
//            val oscFreq = constEmitter(220f, "Note:A2")
//            lfo = lfo("LFO")
//            val osc = sawOsc("MyOsc")
//            val volume = volume(1.0f, "Volume")
//            osc.outAudioL.connect(volume.inAudioL)
//            osc.outAudioR.connect(volume.inAudioR)
//            constEmitter.outPort.connect(lfo.inFrequency)
//            oscFreq.outPort.connect(osc.inFrequency)
//            minConst.outPort.connect(lfo.inMinimum)
//            maxConst.outPort.connect(lfo.inMaximum)
//            lfoTypeConst.outPort.connect(lfo.inType)
//            lfo.outValue.connect(volume.inAmount)
        }


        setVoice(voice.handle.toAddress)

        startEngine()

        setContent {
            DawrioTheme {
                MainBody(voice, components)
            }
        }
    }

    override fun onDestroy() {
        stopEngine()
        super.onDestroy()
    }

    @Composable
    fun MainBody(voice: Voice, components: List<Component>) {
        var playing by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                val scrollState = rememberScrollState()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(16.dp, 16.dp, 16.dp, 0.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(scrollState)
                    ) {
                        for (comp in components) {
                            comp.gui()
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Card(
                    modifier= Modifier
                        .fillMaxWidth()
                        .height(780.dp),
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 100.dp
                    )

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        PushGateButton(
                            onStartPush = {},
                            onStopPush = {
                                playing = !playing
                                beepEvent(playing)
                            }
                        ) {
                            Image(
                                imageVector = Icons.Outlined.PlayArrow,
                                modifier = Modifier.fillMaxSize(0.9f),
                                contentScale = ContentScale.Inside,
                                contentDescription = stringResource(id = R.string.gate_button),
                                colorFilter = ColorFilter.tint(LocalContentColor.current)
                            )
                        }
                    }
                }

            }
        }
    }

    @Composable
    fun TestCard(padding: Dp, voice: Voice, dev: Device) {
        val maxVal = 2.0f * 4f

        DeviceCard(
            voice = voice,
            device = dev
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(padding)
                ) {
                    KnobWithLabel(
                        onValueChange = { constEmitter.value = it },
                        valueRange = 0f..maxVal
                    )
                    BottomLabeled(label = "Hear") {
                        PushGateButton(
                            onStartPush = { beepEvent(true) },
                            onStopPush = { beepEvent(false) }
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(0.9f),
                                contentScale = ContentScale.Inside,
                                painter = painterResource(R.drawable.outline_fiber_manual_record_24),
                                contentDescription = stringResource(id = R.string.gate_button),
                                colorFilter = ColorFilter.tint(LocalContentColor.current)
                            )
                        }
                    }
                }
            }
        }
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