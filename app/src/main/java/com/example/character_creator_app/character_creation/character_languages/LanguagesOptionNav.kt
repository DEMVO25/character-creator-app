package com.example.character_creator_app.character_creation.character_languages


import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LanguagesOptionScreen


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.languagesOptionScreenNavigation(
    onBack: ()-> Unit,
    onNavigateToEquipment: ()-> Unit,
    parentEntry: NavBackStackEntry
) {
    composable<LanguagesOptionScreen> {
        LanguagesOptionRoute(
            onBack = onBack,
            parentEntry = parentEntry,
            onNext = onNavigateToEquipment
        )
    }
}

fun NavController.navigateToLanguagesOption(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(LanguagesOptionScreen, builder)
}
