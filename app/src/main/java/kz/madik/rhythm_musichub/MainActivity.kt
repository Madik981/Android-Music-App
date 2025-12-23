package kz.madik.rhythm_musichub

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kz.madik.rhythm_musichub.navigation.NavGraph
import kz.madik.rhythm_musichub.navigation.Screen
import kz.madik.rhythm_musichub.navigation.navigateToHome
import kz.madik.rhythm_musichub.navigation.navigateToLibrary
import kz.madik.rhythm_musichub.navigation.navigateToSearch
import kz.madik.rhythm_musichub.ui.theme.Rhythm_MusicHubTheme
import kz.madik.rhythm_musichub.utils.LocaleHelper
import kz.madik.rhythm_musichub.viewmodel.MusicViewModel

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val languageCode = LocaleHelper.getLanguage(newBase)
        super.attachBaseContext(LocaleHelper.setLocale(newBase, languageCode))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Rhythm_MusicHubTheme {
                MusicHubApp()
            }
        }
    }
}

@Composable
fun MusicHubApp() {
    val navController = rememberNavController()
    val viewModel: MusicViewModel = viewModel()

    // Отслеживаем текущий маршрут для правильного отображения выбранной вкладки
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = Color(0xFF121212),
        bottomBar = {
            // Показываем навигационную панель только на основных экранах
            if (currentRoute in listOf(Screen.Home.route, Screen.Search.route, Screen.Library.route)) {
                NavigationBar(containerColor = Color(0xFF1E1E1E)) {

                    NavigationBarItem(
                        selected = currentRoute == Screen.Home.route,
                        onClick = { navController.navigateToHome() },
                        icon = {
                            Icon(Icons.Filled.Home, contentDescription = "Home")
                        },
                        label = { Text(stringResource(R.string.nav_home)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF1DB954),
                            selectedTextColor = Color(0xFF1DB954),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == Screen.Search.route,
                        onClick = { navController.navigateToSearch() },
                        icon = {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        },
                        label = { Text(stringResource(R.string.nav_search)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF1DB954),
                            selectedTextColor = Color(0xFF1DB954),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == Screen.Library.route,
                        onClick = { navController.navigateToLibrary() },
                        icon = {
                            Icon(Icons.Filled.Favorite, contentDescription = "Library")
                        },
                        label = { Text(stringResource(R.string.nav_library)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF1DB954),
                            selectedTextColor = Color(0xFF1DB954),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            innerPadding = innerPadding,
            viewModel = viewModel
        )
    }
}
