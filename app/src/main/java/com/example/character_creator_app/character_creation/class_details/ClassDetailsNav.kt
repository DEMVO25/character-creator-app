package com.example.character_creator_app.character_creation.class_details

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class ClassDetails(val className: String)


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.classDetailsNavigation(
    onBack: () -> Unit,

    onNavigateToAbilityScore: () -> Unit,
    parentEntry: NavBackStackEntry,
) {
    composable<ClassDetails> { backStackEntry ->
        val classDetails = backStackEntry.toRoute<ClassDetails>()
        ClassDetailsScreenRoute(
            parentEntry = parentEntry,

            onBack = onBack,
            className = classDetails.className,
            onNavigateToAbilityScore = onNavigateToAbilityScore,
        )
    }
}

fun NavController.navigateToClassDetails(
    className: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(ClassDetails(className), builder)
}