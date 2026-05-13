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
    onNavigateToDetails: (String) -> Unit,
    onNavigateToCreations: () -> Unit,
    onLogout: ()-> Unit,

    ) {
    composable<Home> {
        HomeScreenRoute(
            onNavigateToCreations = onNavigateToCreations,
            onCharacterClick = { id -> onNavigateToDetails(id) },
            onLogout =  onLogout ,
        )
    }
}

fun NavController.navigateToHome(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(Home, builder)
}
