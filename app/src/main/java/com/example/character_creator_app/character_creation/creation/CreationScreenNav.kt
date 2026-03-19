package com.example.character_creator_app.character_creation.creation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Creation


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.creationNavigation(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onNavigateToClassOption: () -> Unit,
) {
    composable<Creation> {
        CreationScreenRoute (
            parentEntry = parentEntry,
            onBack =  onBack,
            onNavigateToClassOption = onNavigateToClassOption,
        )
    }
}

fun NavController.navigateToCreation(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(Creation, builder)
}
