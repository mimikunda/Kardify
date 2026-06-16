package com.kardify.app.ui.navigation

import android.widget.MediaController
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.kardify.app.ui.screens.CardCreationScreen
import com.kardify.app.ui.screens.CardReviewScreen
import com.kardify.app.ui.screens.HomeScreen

@Serializable
object HomeRoute

@Serializable
object CardCreationRoute

@Serializable
data class CardReviewRoute(val deckId: Int)

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier.statusBarsPadding(),
    navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToCreation = {navController.navigate(CardCreationRoute)},
                onNavigateToReview = { deckId -> navController.navigate(CardReviewRoute(deckId = deckId))}
            )

        }

        composable<CardCreationRoute> {
            CardCreationScreen()
        }

        composable<CardReviewRoute> { backStackEntry ->
            val args: CardReviewRoute = backStackEntry.toRoute()
            CardReviewScreen(deckId = args.deckId)
        }
    }

}