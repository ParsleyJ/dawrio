package com.parsleyj.dawrio.ui.editvoice

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import com.parsleyj.dawrio.daw.VoiceHandle
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberEditVoiceState(
    voiceHandle: VoiceHandle,
    viewModel: EditVoiceViewModel,
    onVoiceUpdate: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) : EditVoiceState {
    val currentOnVoiceUpdateState by rememberUpdatedState(onVoiceUpdate)

    return remember(voiceHandle, viewModel, lifecycleOwner, context, coroutineScope) {
        EditVoiceState(
            viewModel,
            voiceHandle,
            currentOnVoiceUpdateState,
            lifecycleOwner,
            context,
            coroutineScope
        )
    }
}

class EditVoiceState(
    private val viewModel: EditVoiceViewModel,
    val voiceHandle: VoiceHandle,
    onVoiceUpdate: () -> Unit,
    lifecycleOwner: LifecycleOwner,
    context: Context,
    coroutineScope: CoroutineScope
) {

    init {
//        viewModel.voiceUpdatedEvent.observe(lifecycleOwner) { voiceUpdated ->
//            if (voiceUpdated) {
//                onVoiceUpdate()
//            }
//        }

        viewModel.start()
    }


    //TODO functions for CRUD in viewModel (writing on devices and routes)
}