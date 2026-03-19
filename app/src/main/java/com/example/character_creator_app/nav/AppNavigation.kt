package com.example.character_creator_app.nav


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.character_creator_app.character_creation.character_creation_nav.CharacterCreation
import com.example.character_creator_app.character_creation.character_creation_nav.characterCreationGraph
import com.example.character_creator_app.character_info.details.Details
import com.example.character_creator_app.character_info.details.detailsNavigation
import com.example.character_creator_app.home.Home
import com.example.character_creator_app.home.homeNavigation
import com.example.character_creator_app.level_up.home.LevelUp
import com.example.character_creator_app.level_up.home.levelUpNavigation
import com.example.character_creator_app.nav.utils.safeNavigate
import com.example.character_creator_app.nav.utils.safePopBackStack


@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()

) {
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        homeNavigation(
            onNavigateToDetails = { id ->
                navController.safeNavigate(Details(characterId = id))
            },
            onNavigateToCreations = {navController.safeNavigate(CharacterCreation) }
        )

        detailsNavigation(
            onBack = { navController.safePopBackStack() },
            navigateToMainInfoUpdate = {
                navController.safeNavigate(LevelUp)
            }
        )

        levelUpNavigation(
            onBack = { navController.safePopBackStack() },
            navController = navController
        )
        characterCreationGraph(
            rootNavController = navController
        )

    }
}

