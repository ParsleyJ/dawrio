package com.parsleyj.dawrio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.parsleyj.dawrio.Engine.stopEngine
import com.parsleyj.dawrio.ui.composables.AddDeviceButton
import com.parsleyj.dawrio.ui.composables.PushGateButton
import com.parsleyj.dawrio.ui.theme.DawrioTheme
import java.util.UUID

class MainActivity : ComponentActivity() {

    /* TODO
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
        setContent {
            DawrioTheme {
                val viewModel: VoiceViewModel by viewModels()
                val playing by viewModel.playing.collectAsStateWithLifecycle()
                val devices by viewModel.devices.collectAsStateWithLifecycle()
                val connections by viewModel.connections.collectAsStateWithLifecycle()


                val deviceMap = remember(devices) {
                    mutableMapOf<UUID, @Composable (Modifier) -> Unit>()
                }

                fun movableDevices(): List<@Composable (Modifier) -> Unit> {
                    return devices.map { dev ->
                        deviceMap.getOrPut(dev.id) {
                            movableContentOf { modifier: Modifier ->
                                dev.DeviceGUI(
                                    modifier,
                                    devices,
                                    connections,
                                    viewModel::pushConnectionChange
                                )
                            }
                        }
                    }
                }


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
//                        val scrollState = rememberScrollState()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.9f)
                                .padding(16.dp, 16.dp, 16.dp, 0.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            LazyColumn(
                                modifier = Modifier.animateContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                items(
                                    count = devices.size,
                                    key = { devices[it].id }
                                ) { index ->
                                    AddDeviceButton(
                                        modifier = Modifier
                                            .animateItemPlacement(tween(600)),
                                        deviceCreators = Engine.deviceCreators
                                    ) {
                                        viewModel.pushNewDevice(index, it)
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(8.dp)
                                            .animateItemPlacement(tween(600))
                                    )
                                    movableDevices()[index](
                                        Modifier.animateItemPlacement(tween(600))
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(8.dp)
                                            .animateItemPlacement(tween(600))
                                    )
                                }
                            }
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            )

                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {

                                PushGateButton(
                                    onStartPush = {},
                                    onStopPush = { viewModel.setPlaying(!playing) }
                                ) {
                                    Image(
                                        imageVector = if (playing) {
                                            ImageVector.vectorResource(
                                                id = R.drawable.baseline_stop_24
                                            )
                                        } else {
                                            Icons.Filled.PlayArrow
                                        },
                                        modifier = Modifier.fillMaxSize(0.9f),
                                        contentScale = ContentScale.Inside,
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(LocalContentColor.current)
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        stopEngine()
        super.onDestroy()
    }


}