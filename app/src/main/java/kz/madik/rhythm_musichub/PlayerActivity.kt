package kz.madik.rhythm_musichub

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kz.madik.rhythm_musichub.ui.player.PlayerScreen
import kz.madik.rhythm_musichub.ui.theme.Rhythm_MusicHubTheme
import kz.madik.rhythm_musichub.utils.LocaleHelper
import kz.madik.rhythm_musichub.utils.ThemeHelper
import kz.madik.rhythm_musichub.viewmodel.MusicViewModel

class PlayerActivity : ComponentActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var viewModel: MusicViewModel

    override fun attachBaseContext(newBase: Context) {
        val languageCode = LocaleHelper.getLanguage(newBase)
        super.attachBaseContext(LocaleHelper.setLocale(newBase, languageCode))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel = ViewModelProvider(this)[MusicViewModel::class.java]

        val trackId = intent.getStringExtra("track_id") ?: ""
        val audioUrl = intent.getStringExtra("audio_url")
            ?: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        val trackTitle = intent.getStringExtra("track_title") ?: "Demo Track"
        val trackArtist = intent.getStringExtra("track_artist") ?: "Unknown Artist"
        val trackAlbum = intent.getStringExtra("track_album") ?: "Unknown Album"
        val coverUrl = intent.getStringExtra("cover_url")
        val trackDuration = intent.getLongExtra("track_duration", 180L)
        val isFavorite = intent.getBooleanExtra("is_favorite", false)

        player = ExoPlayer.Builder(this).build().apply {
            setMediaItem(MediaItem.fromUri(audioUrl))
            prepare()
            playWhenReady = true
        }

        setContent {
            val context = LocalContext.current
            val themePreference = remember { ThemeHelper.getTheme(context) }
            val isSystemInDarkTheme = isSystemInDarkTheme()

            val darkTheme = when (themePreference) {
                ThemeHelper.THEME_LIGHT -> false
                ThemeHelper.THEME_DARK -> true
                else -> isSystemInDarkTheme
            }

            Rhythm_MusicHubTheme(darkTheme = darkTheme) {
                PlayerScreen(
                    onBack = { finish() },
                    trackId = trackId,
                    trackTitle = trackTitle,
                    trackArtist = trackArtist,
                    trackAlbum = trackAlbum,
                    coverUrl = coverUrl,
                    trackDuration = trackDuration.toInt(),
                    initialIsFavorite = isFavorite,
                    player = player,
                    viewModel = viewModel
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
