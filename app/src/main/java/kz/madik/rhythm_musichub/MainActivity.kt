package kz.madik.rhythm_musichub

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
import kz.madik.rhythm_musichub.ui.screens.HomeScreen
import kz.madik.rhythm_musichub.ui.screens.LibraryScreen
import kz.madik.rhythm_musichub.ui.screens.SearchScreen
import kz.madik.rhythm_musichub.ui.theme.Rhythm_MusicHubTheme

class MainActivity : ComponentActivity() {
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

    Scaffold(
        containerColor = Color(0xFF121212),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF121212)) {

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    },
                    label = { Text("Search") }
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(Icons.Filled.Favorite, contentDescription = "Library")
                    },
                    label = { Text("Library") }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> HomeScreen(innerPadding)
            1 -> SearchScreen(innerPadding)
            2 -> LibraryScreen(innerPadding)
        }
    }
}
