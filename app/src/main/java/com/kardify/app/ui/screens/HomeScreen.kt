package com.kardify.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToCreation: () -> Unit,
    onNavigateToReview: (Int) -> Unit


){


    Column(modifier = Modifier
        .fillMaxSize()
         ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                SimpleSearchBarSample()

                Spacer(Modifier.height(16.dp))

                DeckHistoryCarousel()

                Spacer(Modifier.height(16.dp))


                Button(
                    onClick = { onNavigateToCreation() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.fillMaxWidth(

                    )
                )                {
                    Text("Create a deck")
                }

                Spacer(Modifier.weight(1f))

                TestReviewEntry(onNavigateToReview)


            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "New knowledge awaits. Samo and Tibor",
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.secondary
            )

        }
    }


}

//placeholders, to be removed
data class CarouselItem(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector
)

val sampleFlashcardDecks =
    listOf(
        CarouselItem(0, "Spanish Vocab", "Essential verbs and daily conversational phrases", Icons.Default.Translate),
        CarouselItem(1, "MCAT Biology", "Cellular respiration, genetics, and organ systems", Icons.Default.Biotech),
        CarouselItem(2, "World Capitals", "Test your geography knowledge across 6 continents", Icons.Default.Public),
        CarouselItem(3, "Bar Exam Prep", "Constitutional law and torts deep-dive review", Icons.Default.Gavel),
        CarouselItem(4, "Japanese Kanji", "JLPT N5 level characters and stroke orders", Icons.Default.MenuBook),
        CarouselItem(5, "Python Basics", "Syntax, data structures, and OOP principles", Icons.Default.Terminal),
        CarouselItem(6, "Art History", "Famous masterpieces from the Renaissance to Pop Art", Icons.Default.Palette),
        CarouselItem(7, "Driving Theory", "Traffic signs, right-of-way rules, and safety", Icons.Default.DirectionsCar),
        CarouselItem(8, "Human Anatomy", "Skeletal and muscular systems study guide", Icons.Default.FitnessCenter),
        CarouselItem(9, "Mental Math", "Tricks for fast multiplication and percentages", Icons.Default.Functions)
    )



@Composable
fun DeckHistoryCarousel() {

    val items = sampleFlashcardDecks

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Recently Opened",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        HorizontalUncontainedCarousel(
            state = rememberCarouselState { items.count() },
            modifier = Modifier
                .width(412.dp)
                .height(221.dp),
            itemWidth = 186.dp,
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) { i ->
            val item = items[i]
            Box(
                modifier = Modifier
                    .height(205.dp)
                    .maskClip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon, // Dynamically changes per item
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBarSample() {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val scope = rememberCoroutineScope()
    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                textFieldState = textFieldState,
                searchBarState = searchBarState,
                onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
                placeholder = {
                    Text(modifier = Modifier.clearAndSetSemantics {}, text = "Search")
                },
                leadingIcon = { Icon(Icons.Default.Search, "Search")} ,
                trailingIcon = { Icon(Icons.Default.MoreVert, "More") },
            )
        }

    SearchBar(state = searchBarState, inputField = inputField)
    ExpandedFullScreenSearchBar(state = searchBarState, inputField = inputField) {
        SampleSearchResults(
            onResultClick = { result ->
                textFieldState.setTextAndPlaceCursorAtEnd(result)
                scope.launch { searchBarState.animateToCollapsed() }
            }
        )
    }
}

@Composable
private fun SampleSearchResults(onResultClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        sampleFlashcardDecks.forEach { item ->
            ListItem(
                headlineContent = { Text(item.title) },
                supportingContent = { Text(item.description) },
                leadingContent = { Icon(item.icon, contentDescription = null) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier
                    .clickable { onResultClick(item.title) }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }
    }
}


@Composable
fun TestReviewEntry(onNavigateToReview: (Int) -> Unit) {
    val state = rememberTextFieldState()
    var isError by rememberSaveable { mutableStateOf(false) }

    Column() {
        TextField(
            state = state,
            label = { Text(" Test -> Navigate by deckId") },
            isError = isError,
            lineLimits = TextFieldLineLimits.SingleLine,
            onKeyboardAction = {
                val inputString = state.text.toString()
        val targetId = inputString.toIntOrNull()

        if (targetId != null) {
            onNavigateToReview(targetId)
        } else {
            isError = true
        }
            }
        )
    }
}