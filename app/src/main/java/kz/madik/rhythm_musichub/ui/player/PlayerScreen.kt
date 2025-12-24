package kz.madik.rhythm_musichub.ui.player

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kz.madik.rhythm_musichub.data.db.entities.TrackEntity
import kz.madik.rhythm_musichub.ui.components.player.*
import kz.madik.rhythm_musichub.viewmodel.MusicViewModel

@Composable
fun PlayerScreen(
    onBack: () -> Unit,
    trackId: String = "",
    trackTitle: String = "Demo Track",
    trackArtist: String = "Unknown Artist",
    trackAlbum: String? = null,
    coverUrl: String? = null,
    trackDuration: Int = 180,
    initialIsFavorite: Boolean = false,
    player: ExoPlayer? = null,
    viewModel: MusicViewModel? = null
) {
    var isPlaying by remember { mutableStateOf(true) }
    var isFavorite by remember { mutableStateOf(initialIsFavorite) }
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var currentPosition by remember { mutableIntStateOf(0) }

    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF121212)

    LaunchedEffect(player) {
        while (isActive && player != null) {
            currentPosition = (player.currentPosition / 1000).toInt()
            currentProgress = if (trackDuration > 0) {
                currentPosition.toFloat() / trackDuration.toFloat()
            } else 0f
            isPlaying = player.isPlaying
            delay(100)
        }
    }

    PlayerBackground(isDarkTheme = isDarkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerTopBar(onBack = onBack)

            Spacer(modifier = Modifier.height(48.dp))

            PlayerAlbumArt(coverUrl = coverUrl)

            Spacer(modifier = Modifier.height(32.dp))

            PlayerTrackInfo(
                trackTitle = trackTitle,
                trackArtist = trackArtist,
                isFavorite = isFavorite,
                onFavoriteClick = {
                    val trackToToggle = TrackEntity(
                        id = trackId,
                        title = trackTitle,
                        artist = trackArtist,
                        album = trackAlbum,
                        duration = trackDuration.toLong(),
                        audioUrl = player?.currentMediaItem?.localConfiguration?.uri?.toString() ?: "",
                        coverUrl = coverUrl,
                        isFavorite = isFavorite
                    )
                    viewModel?.toggleFavorite(trackToToggle)
                    isFavorite = !isFavorite
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PlayerProgressBar(
                currentProgress = currentProgress,
                currentPosition = currentPosition,
                trackDuration = trackDuration,
                isDarkTheme = isDarkTheme,
                onProgressChange = { newProgress ->
                    currentProgress = newProgress
                    player?.seekTo((newProgress * trackDuration * 1000).toLong())
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PlayerControls(
                isPlaying = isPlaying,
                isDarkTheme = isDarkTheme,
                onPreviousClick = {
                    player?.seekTo(0)
                },
                onPlayPauseClick = {
                    if (player?.isPlaying == true) {
                        player.pause()
                    } else {
                        player?.play()
                    }
                }
            )
        }
    }
}
