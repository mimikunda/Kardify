package com.kardify.app.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CardReviewScreen(deckId: Int){
    Text(text = deckId.toString())
}