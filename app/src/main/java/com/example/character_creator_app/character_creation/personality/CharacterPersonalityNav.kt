package com.example.character_creator_app.character_creation.personality


import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Personality


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.personalityNavigation(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    composable<Personality> {
        PersonalityRoute (
            parentEntry = parentEntry,
            onBack =  onBack,
            onNext = onNext,
        )
    }
}

fun NavController.navigateToPersonality(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(Personality, builder)
}
