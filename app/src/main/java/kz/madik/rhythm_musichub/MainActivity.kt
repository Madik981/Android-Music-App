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
import androidx.navigation.compose.rememberNavController
import kz.madik.rhythm_musichub.ui.main.HomeScreen
import kz.madik.rhythm_musichub.ui.main.LibraryScreen
import kz.madik.rhythm_musichub.ui.main.SearchScreen
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
    var selectedTab by remember { mutableIntStateOf(0) }
    val navController = rememberNavController()
    val viewModel: MusicViewModel = viewModel()

    Scaffold(
        containerColor = Color(0xFF121212),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1E1E1E)) {

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
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
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
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
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
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
    ) { innerPadding ->
        when (selectedTab) {
            0 -> HomeScreen(innerPadding, viewModel, navController)
            1 -> SearchScreen(innerPadding, viewModel, navController)
            2 -> LibraryScreen(innerPadding, viewModel, navController)
        }
    }
}
