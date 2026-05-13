package com.example.character_creator_app.register

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.character_creator_app.sign_in.SignInScreenRoute
import kotlinx.serialization.Serializable


@Serializable
data object Register


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.registerNavigation(
    onNavigateToHomeScreen: ()-> Unit,
    onNavigateToSignInScreen: ()-> Unit,
) {
    composable<Register> {
        RegisterScreenRoute (
            onNavigateToHomeScreen = onNavigateToHomeScreen,
            onNavigateToSignInScreen = onNavigateToSignInScreen,
        )
    }
}

fun NavController.navigateToRegister(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(Register, builder)
}