package kz.madik.rhythm_musichub.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun PlayerBackground(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDarkTheme) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF282828),
                            Color(0xFF121212),
                            Color(0xFF000000)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFCBCBCB),
                            Color(0xFFFFFFFF),
                            Color(0xFFFFFFFF)
                        )
                    )
                }
            )
            .statusBarsPadding()
    ) {
        content()
    }
}

