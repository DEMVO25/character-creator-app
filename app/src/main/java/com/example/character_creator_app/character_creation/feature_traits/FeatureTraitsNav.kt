package com.example.character_creator_app.character_creation.feature_traits

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object FeatureTrait


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.featureTraitNavigation(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    composable<FeatureTrait> {
        FeatureTraitRoute (
            parentEntry = parentEntry,
            onBack =  onBack,
            onNext = onNext,
        )
    }
}

fun NavController.navigateToFeatureTrait(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(FeatureTrait, builder)
}