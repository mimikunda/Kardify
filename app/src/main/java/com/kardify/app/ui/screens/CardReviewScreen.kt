package com.kardify.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardReviewScreen(deckId: Int){

val deckIdStr = deckId.toString()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    )
{
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.weight(1f))
        Row() {
            Text(
                text = "1/1                                  0 right, 0 wrong",
                textAlign = TextAlign.Center
            )
        } //yeahhh
        Spacer(Modifier.weight(0.2f))
        Card(
            modifier = Modifier
                .height(590.dp)
                .width(355.dp)
        ) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp), contentAlignment = Alignment.Center
            ) {
                Column() {
                    Text(
                        text = "This is the first question for deck $deckIdStr",
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Row(modifier = Modifier.width(340.dp)) {
            Button(onClick = {}) { Icon(Icons.Default.PsychologyAlt, null) }
            Spacer(Modifier.weight(0.1f))
            Button(onClick = {}) { Icon(Icons.Default.ArrowBack, null) }
            Spacer(Modifier.weight(1f))
            Button(onClick = {}) { Icon(Icons.Default.ArrowForward, null) }
            Spacer(Modifier.weight(0.1f))
            Button(onClick = {}) { Icon(Icons.Default.ThumbUp, null) }
        }
        Spacer(Modifier.weight(1f))
    }
}

}