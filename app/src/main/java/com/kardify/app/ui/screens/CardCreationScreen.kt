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
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.kardify.app.db.Question
import com.kardify.app.db.QuestionDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CardCreationScreen(modifier: Modifier = Modifier) {

    val db = QuestionDatabase.getDatabase(LocalContext.current)
    val dao = db.questionDao()
    val scope = rememberCoroutineScope()


    val frontSideEntryState = rememberTextFieldState()
    val backSideEntryState = rememberTextFieldState()

    var isFrontSideEntryError by rememberSaveable { mutableStateOf(false) }
    var isBackSideEntryError by rememberSaveable { mutableStateOf(false) }
    var isError by rememberSaveable { mutableStateOf(false) }

    val questionList by dao.getAllQuestions().collectAsState(initial = emptyList())


    fun validate(text: CharSequence, index: Int) {
        isError = text.isBlank()
        if (isError)
            when (index) {
                1 -> isFrontSideEntryError = true
                2 -> isBackSideEntryError = true
            }

    }

    fun submitPair(frontSide: String, backSide: String) {
        validate(frontSideEntryState.text, 1)
        validate(backSideEntryState.text, 2)

        if (!isError) {
            scope.launch {
                val newEntry = Question(frontSide = frontSide, backSide = backSide)
                dao.insert(newEntry)
            }
        }


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
            Column() {
                Card {
                    Column(
                        Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                        Row() {
                            Button(
                                onClick = {
                                    submitPair(
                                        frontSideEntryState.text.toString(),
                                        backSideEntryState.text.toString()
                                    )
                                }
                            )
                            {
                                Text("Submit")
                            }
                            Button(
                                onClick = {
                                    scope.launch {
                                        val list = dao.getAllIds()
                                        for (item in list) {
                                            dao.delete(Question(item, null, null))
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            )
                            {
                                Text("Delete all")
                            }
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                Card {
                    Column(
                        Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        questionList.forEach { item ->
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.frontSide ?: "empty")
                                Text(item.backSide ?: "empty")
                            }
                        }


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

