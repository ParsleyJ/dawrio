package com.parsleyj.dawrio.ui.editvoice

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import com.parsleyj.dawrio.data.MemoryDataSource
import com.parsleyj.dawrio.data.MemoryProjectRepository
import com.parsleyj.dawrio.daw.Voice


@Composable
fun EditVoiceScreen(
    @StringRes topBarTitle: Int,
    voice: Voice,
    onVoiceUpdate: ()->Unit,
    modifier: Modifier = Modifier,
    viewModel: EditVoiceViewModel = EditVoiceViewModel(voice.handle,
        //TODO use factory
        MemoryProjectRepository(
            MemoryDataSource()
        )
    ),
    state: EditVoiceState = rememberEditVoiceState(voice.handle, viewModel, onVoiceUpdate)
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        val loading by viewModel.dataLoading.observeAsState(initial = false)

        EditVoiceContent(
            loading = loading,
        )

    }
}

@Composable
fun EditVoiceContent(
    loading:Boolean,
) {
    if(loading){
        //TODO show loading animation
    }else{
        //TODO populate with devices
    }
}