package com.example.character_creator_app.character_creation.character_class_option


import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ClassOption


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.classOptionNavigation(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onClassSelected: (String) -> Unit
) {
    composable<ClassOption> {
        ClassOptionScreenRoute (
            parentEntry = parentEntry,
            onBack = onBack,
            onClassSelected = onClassSelected
        )
    }
}

fun NavController.navigateToClassOption(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(ClassOption, builder)
}
