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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(modifier: Modifier = Modifier){


    Column(modifier = Modifier
        .fillMaxSize()

        .statusBarsPadding()) { //add padding in navhost later and remove here!
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
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.fillMaxWidth(

                    )
                )                {
                    Text("Create a deck")
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
                text = "New knowledge awaits. Samo and Tibor",
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.secondary
            )

        }
    }


}

@Composable
fun DeckHistoryCarousel() {

    data class CarouselItem(
        val id: Int,
        val title: String,
        val description: String,
    )

    val items = listOf(
        CarouselItem(0, "Deck 1", "Description for Deck 1"),
        CarouselItem(1, "Deck 2", "Description for Deck 2"),
        CarouselItem(2, "Deck 3", "Description for Deck 3"),
        CarouselItem(3, "Deck 4", "Description for Deck 4"),
        CarouselItem(4, "Deck 5", "Description for Deck 5"),
    )

    Column(){
        Text("  Recently Opened") //yeah... I know...

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
                            imageVector = Icons.Default.Image,
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
private fun SampleSearchResults(onResultClick: (String) -> Unit, modifier: Modifier = Modifier) { //this is a placeholder from an example. To remove later
    Column(modifier.verticalScroll(rememberScrollState())) {
        repeat(10) { idx ->
            val resultText = "Suggestion $idx"
            ListItem(
                headlineContent = { Text(resultText) },
                supportingContent = { Text("Additional info") },
                leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier =
                    Modifier
                        .clickable { onResultClick(resultText) }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }
    }
}
