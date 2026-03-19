package com.example.character_creator_app.nav.utils

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder


fun NavController.safeNavigate(
    route: Any,
    builder: NavOptionsBuilder.() -> Unit = {}
) {

    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        navigate(route, builder)
    }
}


fun NavController.safePopBackStack(): Boolean {
    return if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    } else false
}