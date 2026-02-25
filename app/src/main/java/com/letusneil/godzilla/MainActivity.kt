package com.letusneil.godzilla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.letusneil.godzilla.navigation.GodzillaNavHost
import com.letusneil.godzilla.ui.home.HOME_ROUTE
import com.letusneil.godzilla.ui.profile.PROFILE_ROUTE
import com.letusneil.godzilla.ui.workout.WORKOUT_ROUTE
import com.letusneil.godzilla.ui.theme.GodzillaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GodzillaTheme {
                GodzillaApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun GodzillaApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            destination.icon,
                            contentDescription = destination.label,
                        )
                    },
                    label = { Text(destination.label) },
                    selected = currentRoute == destination.route,
                    onClick = { navController.navigateToDestination(destination) },
                )
            }
        },
    ) {
        GodzillaNavHost(navController = navController)
    }
}

private fun NavHostController.navigateToDestination(destination: AppDestinations) {
    navigate(destination.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val route: String,
) {
    HOME("Home", Icons.Default.Home, HOME_ROUTE),
    WORKOUT("Workout", Icons.Default.Favorite, WORKOUT_ROUTE),
    PROFILE("Profile", Icons.Default.AccountBox, PROFILE_ROUTE),
}