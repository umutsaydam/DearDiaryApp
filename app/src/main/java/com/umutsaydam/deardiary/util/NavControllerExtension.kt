package com.umutsaydam.deardiary.util

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

fun NavController.safeNavigate(route: String) {
    if (currentBackStackEntry?.destination?.route != route) {
        navigate(route)
    }
}

fun NavController.popBackStackOrIgnore() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}