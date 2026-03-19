package com.example.character_creator_app.character_creation.skills


import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SkillsScreen


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.skillsScreenNavigation(
    onBack: ()-> Unit,
    onNavigateToPersonality:() -> Unit ,
    parentEntry: NavBackStackEntry
) {
    composable<SkillsScreen> {
        SkillsScreenRoute(
            onBack = onBack,
            parentEntry = parentEntry,
            navigateToPersonality = onNavigateToPersonality
        )
    }
}

fun NavController.navigateToSkills(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(SkillsScreen, builder)
}
