package kz.madik.rhythm_musichub.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.madik.rhythm_musichub.R
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
        // Top App Bar
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
            // Theme Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SettingsSectionHeader(title = stringResource(R.string.settings_theme))
            }

            // Dark Theme
            item {
                ThemeItem(
                    themeName = stringResource(R.string.settings_theme_dark),
                    isSelected = currentTheme == ThemeHelper.THEME_DARK,
                    onClick = {
                        ThemeHelper.setTheme(context, ThemeHelper.THEME_DARK)
                        (context as? android.app.Activity)?.recreate()
                    }
                )
            }

            // Light Theme
            item {
                ThemeItem(
                    themeName = stringResource(R.string.settings_theme_light),
                    isSelected = currentTheme == ThemeHelper.THEME_LIGHT,
                    onClick = {
                        ThemeHelper.setTheme(context, ThemeHelper.THEME_LIGHT)
                        (context as? android.app.Activity)?.recreate()
                    }
                )
            }

            // System Theme
            item {
                ThemeItem(
                    themeName = stringResource(R.string.settings_theme_system),
                    isSelected = currentTheme == ThemeHelper.THEME_SYSTEM,
                    onClick = {
                        ThemeHelper.setTheme(context, ThemeHelper.THEME_SYSTEM)
                        (context as? android.app.Activity)?.recreate()
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Language Section
            item {
                SettingsSectionHeader(title = stringResource(R.string.settings_language))
            }

            // English
            item {
                LanguageItem(
                    languageName = stringResource(R.string.settings_language_english),
                    isSelected = currentLanguage == "en",
                    onClick = {
                        LocaleHelper.setLocale(context, "en")
                        (context as? android.app.Activity)?.recreate()
                    }
                )
            }

            // Russian
            item {
                LanguageItem(
                    languageName = stringResource(R.string.settings_language_russian),
                    isSelected = currentLanguage == "ru",
                    onClick = {
                        LocaleHelper.setLocale(context, "ru")
                        (context as? android.app.Activity)?.recreate()
                    }
                )
            }

            // Kazakh
            item {
                LanguageItem(
                    languageName = stringResource(R.string.settings_language_kazakh),
                    isSelected = currentLanguage == "kk",
                    onClick = {
                        LocaleHelper.setLocale(context, "kk")
                        (context as? android.app.Activity)?.recreate()
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun ThemeItem(
    themeName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = themeName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun LanguageItem(
    languageName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = languageName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
