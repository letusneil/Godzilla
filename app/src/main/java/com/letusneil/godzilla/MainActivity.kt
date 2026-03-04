package com.letusneil.godzilla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.letusneil.godzilla.navigation.GodzillaNavHost
import com.letusneil.godzilla.ui.home.HOME_ROUTE
import com.letusneil.godzilla.ui.profile.PROFILE_ROUTE
import com.letusneil.godzilla.ui.theme.AccentBlue
import com.letusneil.godzilla.ui.theme.GodzillaTheme
import com.letusneil.godzilla.ui.theme.LocalThemeConfig
import com.letusneil.godzilla.ui.theme.LocalThemeController
import com.letusneil.godzilla.ui.theme.ThemeConfig
import com.letusneil.godzilla.ui.workout.WORKOUT_ROUTE
import com.letusneil.godzilla.theme.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GodzillaApp()
        }
    }
}

@Composable
fun GodzillaApp(themeViewModel: ThemeViewModel = koinViewModel()) {
    val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()
    val themeConfig = ThemeConfig(themeMode = themeMode)

    GodzillaTheme(themeConfig = themeConfig) {
        CompositionLocalProvider(
            LocalThemeConfig provides themeConfig,
            LocalThemeController provides { themeViewModel.setThemeMode(it) },
        ) {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            NavigationSuiteScaffold(
                navigationSuiteColors = NavigationSuiteDefaults.colors(
                    navigationBarContentColor = AccentBlue,
                ),
                navigationSuiteItems = {
                    AppDestinations.entries.forEach { destination ->
                        val selected = currentRoute == destination.route
                        item(
                            icon = {
                                Icon(
                                    if (selected) destination.selectedIcon else destination.unselectedIcon,
                                    contentDescription = destination.label,
                                )
                            },
                            label = { Text(destination.label) },
                            selected = selected,
                            onClick = { navController.navigateToDestination(destination) },
                        )
                    }
                },
            ) {
                GodzillaNavHost(
                    navController = navController,
                    modifier = Modifier.statusBarsPadding(),
                )
            }
        }
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
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
) {
    HOME("Home", Icons.Default.Home, Icons.Outlined.Home, HOME_ROUTE),
    WORKOUT("Workout", Icons.Default.FitnessCenter, Icons.Outlined.FitnessCenter, WORKOUT_ROUTE),
    PROFILE("Profile", Icons.Default.Person, Icons.Outlined.Person, PROFILE_ROUTE),
}
