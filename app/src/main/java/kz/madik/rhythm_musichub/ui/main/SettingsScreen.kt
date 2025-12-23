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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.madik.rhythm_musichub.R
import kz.madik.rhythm_musichub.utils.LocaleHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    padding: PaddingValues,
    navController: NavController
) {
    val context = LocalContext.current
    val currentLanguage = LocaleHelper.getLanguage(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.settings_title),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.settings_back),
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF121212)
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            // Language Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
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
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Theme Section (placeholder for future)
            item {
                SettingsSectionHeader(title = stringResource(R.string.settings_theme))
            }

            item {
                SettingsItem(
                    title = stringResource(R.string.settings_theme_dark),
                    subtitle = stringResource(R.string.settings_theme_coming_soon),
                    onClick = { /* TODO: Implement later */ }
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
        color = Color.Gray,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
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
            .background(if (isSelected) Color(0xFF1E1E1E) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = languageName,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color(0xFF1DB954),
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
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
