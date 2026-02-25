package com.letusneil.godzilla.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.letusneil.godzilla.ui.home.HOME_ROUTE
import com.letusneil.godzilla.ui.home.homeRoute
import com.letusneil.godzilla.ui.profile.profileRoute
import com.letusneil.godzilla.ui.workout.workoutRoute

@Composable
fun GodzillaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        modifier = modifier,
    ) {
        homeRoute()
        workoutRoute()
        profileRoute()
    }
}