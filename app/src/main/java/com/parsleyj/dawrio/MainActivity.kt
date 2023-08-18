package com.parsleyj.dawrio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.parsleyj.dawrio.Engine.stopEngine
import com.parsleyj.dawrio.ui.composables.PushGateButton
import com.parsleyj.dawrio.ui.theme.DawrioTheme

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
        val viewModel: VoiceViewModel by viewModels()

        val playing by viewModel.playing.collectAsStateWithLifecycle()
        val components by viewModel.components.collectAsStateWithLifecycle()
        val elements by viewModel.elements.collectAsStateWithLifecycle()
        val routes by viewModel.routes.collectAsStateWithLifecycle()

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
                            comp.ComponentGUI(elements, routes, viewModel::pushRouteChange)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Card(
                    modifier = Modifier
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
                            onStopPush = { viewModel.setPlaying(!playing) }
                        ) {
                            Image(
                                imageVector = if (playing) {
                                    Icons.Outlined.Close
                                } else {
                                    Icons.Outlined.PlayArrow
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