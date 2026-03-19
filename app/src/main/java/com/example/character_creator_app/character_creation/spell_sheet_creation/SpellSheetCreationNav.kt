package com.example.character_creator_app.character_creation.spell_sheet_creation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SpellSheetCreation


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.spellSheetCreationNavigation(
    onBack: ()-> Unit,
    onNext:() -> Unit ,
    parentEntry: NavBackStackEntry
) {
    composable<SpellSheetCreation> {
        SpellSheetCreationRoute(
            onBack = onBack,
            parentEntry = parentEntry,
            onNext = onNext
        )
    }
}

fun NavController.navigateToSpellSheetCreation(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(SpellSheetCreation, builder)
}
