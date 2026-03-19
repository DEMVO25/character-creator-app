package com.example.character_creator_app.character_info.details

import android.R.attr.id
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class Details(val characterId: Int)



@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.detailsNavigation(
    onBack: () -> Unit,
    navigateToMainInfoUpdate: () -> Unit
) {
    composable<Details> { backStackEntry ->

        val detailsArgs = backStackEntry.toRoute<Details>()

        DetailsScreenRoute(
            characterId = detailsArgs.characterId,
            onBack = onBack,
            navigateToMainInfoUpdate = navigateToMainInfoUpdate
        )
    }
}

fun NavController.navigateToDetails(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(Details(characterId = id), builder)
}
