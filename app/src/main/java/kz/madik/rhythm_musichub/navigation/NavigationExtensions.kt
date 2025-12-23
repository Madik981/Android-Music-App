package kz.madik.rhythm_musichub.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

/**
 * Extension функции для упрощения навигации
 */

/**
 * Навигация на главный экран с очисткой back stack
 */
fun NavController.navigateToHome() {
    navigate(Screen.Home.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * Навигация на экран поиска
 */
fun NavController.navigateToSearch() {
    navigate(Screen.Search.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * Навигация на экран библиотеки
 */
fun NavController.navigateToLibrary() {
    navigate(Screen.Library.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * Навигация на экран настроек
 */
fun NavController.navigateToSettings() {
    navigate(Screen.Settings.route)
}
