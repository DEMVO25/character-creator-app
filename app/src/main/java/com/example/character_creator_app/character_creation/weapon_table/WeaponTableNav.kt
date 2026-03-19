package com.example.character_creator_app.character_creation.weapon_table

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object WeaponTable


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.weaponTableNavigation(
    onBack: () -> Unit,
    onNext: () -> Unit,
    parentEntry: NavBackStackEntry
) {
    composable<WeaponTable> {
        WeaponTableScreenRoute(
            onBack = onBack,
            onNext = onNext,
            parentEntry = parentEntry,

        )
    }
}

fun NavController.navigateToWeaponTable(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(WeaponTable, builder)
}
