package com.parsleyj.dawrio.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BottomLabeled(
   label:String,
   padding: Dp = 8.dp,
   content: @Composable () -> Unit
) {
    val text = remember { mutableStateOf(label) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(padding)
    ){
        content()
        Spacer(Modifier.size(padding))
        val textInLabel by text
        Text(
            text = textInLabel,
            textAlign = TextAlign.Center
        )
    }
}