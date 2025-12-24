package kz.madik.rhythm_musichub.ui.components.player

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kz.madik.rhythm_musichub.utils.formatTime

@Composable
fun PlayerProgressBar(
    currentProgress: Float,
    currentPosition: Int,
    trackDuration: Int,
    isDarkTheme: Boolean,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Slider(
            value = currentProgress,
            onValueChange = onProgressChange,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = if (isDarkTheme) Color.White else Color(0xFF1DB954),
                activeTrackColor = if (isDarkTheme) Color.White else Color(0xFF1DB954),
                inactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPosition),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = formatTime(trackDuration),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

