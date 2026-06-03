package com.kardify.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(name: String, modifier: Modifier = Modifier) {

    val frontSideEntryState = rememberTextFieldState()
    val backSideEntryState = rememberTextFieldState()

    var isFrontSideEntryError by rememberSaveable { mutableStateOf(false) }
    var isBackSideEntryError by rememberSaveable { mutableStateOf(false) }
    var isError by rememberSaveable { mutableStateOf(false) }

    var testDisplayText by rememberSaveable { mutableStateOf("test")}
    var testDisplayText2 by rememberSaveable { mutableStateOf("test2")}

    fun validate(text: CharSequence, index: Int) {
        isError = text.isBlank()
        if (isError)
            when (index) {
                1 -> isFrontSideEntryError = true
                2 -> isBackSideEntryError = true
            }

    }
    
    fun submitPair(frontSide: String, backSide: String){ // need to append to something instead of just displaying
        testDisplayText = frontSide
        testDisplayText2 = backSide
        
    }

    LaunchedEffect(Unit) {
        snapshotFlow { frontSideEntryState.text }
            .collect {
                isFrontSideEntryError = false
            }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { backSideEntryState.text }
            .collect {
                isBackSideEntryError = false
            }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card {
                Column(
                    Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                   Row() {
                        Text(testDisplayText)
                       Spacer(Modifier.width(10.dp))
                       Text(testDisplayText2)
                       
                    }
                    Spacer(Modifier.height(2.dp))
                    TextField(
                        state = frontSideEntryState,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        label = { Text(if (isFrontSideEntryError) "Field empty" else "Question") },
                        isError = isFrontSideEntryError,
                        onKeyboardAction = { validate(frontSideEntryState.text, 1) },

                    )
                    Spacer(Modifier.height(2.dp))
                    TextField(
                        state = backSideEntryState,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        label = { Text(if (isBackSideEntryError) "Field empty" else "Answer") },
                        isError = isBackSideEntryError,
                        onKeyboardAction = { validate(backSideEntryState.text, 2) },

                        )
                    Spacer(Modifier.height(2.dp))
                    Button(
                        onClick = {
                            submitPair(frontSideEntryState.text.toString(), backSideEntryState.text.toString())
                        }
                    )
                    {
                        Text("Submit")
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "New knowledge awaits. Samo in Tibor",
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.secondary
            )

        }
    }
}

