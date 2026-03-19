package com.example.character_creator_app.character_creation.equipment


import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Equipment


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.equipmentNavigation(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    composable<Equipment> {
        EquipmentScreenRoute  (
            parentEntry = parentEntry,
            onBack =  onBack,
            onNext = onNext,
        )
    }
}

fun NavController.navigateToEquipment(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(Equipment, builder)
}
