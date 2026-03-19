package com.example.character_creator_app.level_up.home



import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LevelUp


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.levelUpNavigation(
    onBack: ()-> Unit,
    navController: NavController
) {
    composable<LevelUp> {
        LevelUpRoute(
            onBack = onBack,
            navController = navController,

            )
    }
}

fun NavController.navigateToLevelUp(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(LevelUp, builder)
}