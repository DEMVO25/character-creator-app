package com.example.character_creator_app.sign_in

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object SignIn


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.signInNavigation(
    onNavigateToHomeScreen: ()-> Unit,
    onNavigateToRegisterScreen: ()-> Unit,
    ) {
    composable<SignIn> {
        SignInScreenRoute(
            onNavigateToHomeScreen = onNavigateToHomeScreen,
            onNavigateToRegisterScreen = onNavigateToRegisterScreen,
        )
    }
}

fun NavController.navigateToSignIn(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(SignIn, builder)
}