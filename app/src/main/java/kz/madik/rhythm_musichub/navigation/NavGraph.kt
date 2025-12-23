package kz.madik.rhythm_musichub.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kz.madik.rhythm_musichub.ui.main.HomeScreen
import kz.madik.rhythm_musichub.ui.main.LibraryScreen
import kz.madik.rhythm_musichub.ui.main.SearchScreen
import kz.madik.rhythm_musichub.viewmodel.MusicViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: MusicViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                padding = innerPadding,
                viewModel = viewModel
            )
        }

        composable(route = Screen.Search.route) {
            SearchScreen(
                padding = innerPadding,
                viewModel = viewModel
            )
        }

        composable(route = Screen.Library.route) {
            LibraryScreen(
                padding = innerPadding,
                viewModel = viewModel
            )
        }

        composable(route = Screen.Settings.route) {
            // Settings screen can be implemented later
        }
    }
}
