package kz.madik.rhythm_musichub.utils

import android.content.Context
import android.content.SharedPreferences

object ThemeHelper {
    private const val PREFS_NAME = "app_preferences"
    private const val KEY_THEME = "app_theme"

    const val THEME_DARK = "dark"
    const val THEME_LIGHT = "light"
    const val THEME_SYSTEM = "system"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getTheme(context: Context): String {
        return getPreferences(context).getString(KEY_THEME, THEME_DARK) ?: THEME_DARK
    }

    fun setTheme(context: Context, theme: String) {
        getPreferences(context).edit().putString(KEY_THEME, theme).apply()
    }
}

