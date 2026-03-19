package com.example.character_creator_app.character_creation.ability

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object AbilityScore


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.abilityScoreNavigation(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onNavigateToSkill: () -> Unit
) {
    composable<AbilityScore> {
        AbilityScoreScreenRoute (
            parentEntry = parentEntry,
            onBack = onBack,
            onNavigateToSkill = onNavigateToSkill,
        )
    }
}

fun NavController.navigateToAbilityScore(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(AbilityScore, builder)
}
