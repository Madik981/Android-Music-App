package kz.madik.rhythm_musichub.ui.main

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.madik.rhythm_musichub.R
import kz.madik.rhythm_musichub.ui.components.settings.LanguageItem
import kz.madik.rhythm_musichub.ui.components.settings.SettingsSectionHeader
import kz.madik.rhythm_musichub.ui.components.settings.ThemeItem
import kz.madik.rhythm_musichub.utils.LocaleHelper
import kz.madik.rhythm_musichub.utils.ThemeHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    padding: PaddingValues,
    navController: NavController
) {
    val context = LocalContext.current
    val currentLanguage = LocaleHelper.getLanguage(context)
    val currentTheme = ThemeHelper.getTheme(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.settings_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.settings_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SettingsSectionHeader(title = stringResource(R.string.settings_theme))
            }

            item {
                ThemeItem(
                    themeName = stringResource(R.string.settings_theme_dark),
                    isSelected = currentTheme == ThemeHelper.THEME_DARK,
                    onClick = {
                        ThemeHelper.setTheme(context, ThemeHelper.THEME_DARK)
                        (context as? Activity)?.recreate()
                    }
                )
            }

            item {
                ThemeItem(
                    themeName = stringResource(R.string.settings_theme_light),
                    isSelected = currentTheme == ThemeHelper.THEME_LIGHT,
                    onClick = {
                        ThemeHelper.setTheme(context, ThemeHelper.THEME_LIGHT)
                        (context as? Activity)?.recreate()
                    }
                )
            }

            item {
                ThemeItem(
                    themeName = stringResource(R.string.settings_theme_system),
                    isSelected = currentTheme == ThemeHelper.THEME_SYSTEM,
                    onClick = {
                        ThemeHelper.setTheme(context, ThemeHelper.THEME_SYSTEM)
                        (context as? Activity)?.recreate()
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SettingsSectionHeader(title = stringResource(R.string.settings_language))
            }

            item {
                LanguageItem(
                    languageName = stringResource(R.string.settings_language_english),
                    isSelected = currentLanguage == "en",
                    onClick = {
                        LocaleHelper.setLocale(context, "en")
                        (context as? Activity)?.recreate()
                    }
                )
            }

            item {
                LanguageItem(
                    languageName = stringResource(R.string.settings_language_russian),
                    isSelected = currentLanguage == "ru",
                    onClick = {
                        LocaleHelper.setLocale(context, "ru")
                        (context as? Activity)?.recreate()
                    }
                )
            }

            item {
                LanguageItem(
                    languageName = stringResource(R.string.settings_language_kazakh),
                    isSelected = currentLanguage == "kk",
                    onClick = {
                        LocaleHelper.setLocale(context, "kk")
                        (context as? Activity)?.recreate()
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
