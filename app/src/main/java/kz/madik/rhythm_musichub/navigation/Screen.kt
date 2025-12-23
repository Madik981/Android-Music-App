package kz.madik.rhythm_musichub.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Library : Screen("library")
    object Settings : Screen("settings")
}

