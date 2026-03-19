package com.example.character_creator_app.home

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Home


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.homeNavigation(
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToCreations: () -> Unit,

    ) {
    composable<Home> {
        HomeScreenRoute(
            onNavigateToCreations = onNavigateToCreations,
            onCharacterClick = {id -> onNavigateToDetails(id)},
        )
    }
}

fun NavController.navigateToHome(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(Home, builder)
}
