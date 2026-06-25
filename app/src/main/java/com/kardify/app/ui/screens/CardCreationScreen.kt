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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kardify.app.db.Question
import com.kardify.app.db.QuestionDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CardCreationScreen(modifier: Modifier = Modifier) {

    val db = QuestionDatabase.getDatabase(LocalContext.current)
    val dao = db.questionDao()
    val scope = rememberCoroutineScope()


    var frontSideEntryState = rememberTextFieldState()
    var backSideEntryState = rememberTextFieldState()

    var isFrontSideEntryError by rememberSaveable { mutableStateOf(false) }
    var isBackSideEntryError by rememberSaveable { mutableStateOf(false) }
    var isError by rememberSaveable { mutableStateOf(false) }
    var canRecall by rememberSaveable { mutableStateOf(true) }

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
        canRecall = true

        if (!isError) {
            scope.launch {
                val newEntry = Question(frontSide = frontSide, backSide = backSide)
                dao.insert(newEntry)
            }
        }


    }


    fun recallTextFieldEntry(id: Int) {
        scope.launch {
            frontSideEntryState.setTextAndSelectAll(dao.getFrontSideById(id) ?: "ERROR (Q)")
            backSideEntryState.setTextAndSelectAll(dao.getBacktSideById(id) ?: "ERROR (A)")
            dao.deletetPairById(id)
            canRecall = false
        }
    }

    fun recallLastTextFieldEntry() {
        scope.launch {
            val id: Int = dao.getLastId()
            recallTextFieldEntry(id)
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    )
    {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Create new deck", textAlign = TextAlign.Center, fontSize = 40.sp)
            Row() {
                Button(
                    onClick = {},
                    enabled = false
                ) {
                    Row() {
                        Icon(Icons.Default.Add, null)
                        Text("Import deck")
                    }
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
                Button(onClick = {}, enabled = false) {
                    Text("Create")
                }
            }
            Column() {
                Column(
                    Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        state = frontSideEntryState,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        label = { Text(if (isFrontSideEntryError) "Field empty" else "Question") },
                        isError = isFrontSideEntryError,
                        onKeyboardAction = { validate(frontSideEntryState.text, 1) },

                        )
                    Spacer(Modifier.height(2.dp))
                    OutlinedTextField(
                        state = backSideEntryState,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        label = { Text(if (isBackSideEntryError) "Field empty" else "Answer") },
                        isError = isBackSideEntryError,
                        onKeyboardAction = { validate(backSideEntryState.text, 2) },

                        )
                    Spacer(Modifier.height(2.dp))
                    Row() {
                        Button(onClick = {
                            recallLastTextFieldEntry()
                        }, enabled = canRecall) {
                            Text("Previous")
                        }
                        Button(
                            onClick = {
                                submitPair(
                                    frontSideEntryState.text.toString(),
                                    backSideEntryState.text.toString()
                                )
                            }
                        )
                        {
                            Text("Resubmit")
                        }
                        Button(onClick = {
                            submitPair(
                                frontSideEntryState.text.toString(),
                                backSideEntryState.text.toString()
                            )
                            frontSideEntryState.clearText()
                            backSideEntryState.clearText()

                        }) {
                            Text("New")
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))
                Divider()

                LazyColumn(
                    Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    item {
                        questionList.forEach { item ->
                            Row() {
                                Text(item.id.toString(), fontSize = 10.sp)
                                Button(
                                    onClick = { recallTextFieldEntry(item.id) },
                                    enabled = canRecall
                                ) {
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

            }

        }
    }
}

