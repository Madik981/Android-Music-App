package kz.madik.rhythm_musichub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kz.madik.rhythm_musichub.ui.player.PlayerScreen
import kz.madik.rhythm_musichub.ui.theme.Rhythm_MusicHubTheme
import kz.madik.rhythm_musichub.viewmodel.MusicViewModel

class PlayerActivity : ComponentActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var viewModel: MusicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel = ViewModelProvider(this)[MusicViewModel::class.java]

        val trackId = intent.getStringExtra("track_id") ?: ""
        val audioUrl = intent.getStringExtra("audio_url")
            ?: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        val trackTitle = intent.getStringExtra("track_title") ?: "Demo Track"
        val trackArtist = intent.getStringExtra("track_artist") ?: "Unknown Artist"
        val coverUrl = intent.getStringExtra("cover_url")
        val trackDuration = intent.getLongExtra("track_duration", 180L)
        val isFavorite = intent.getBooleanExtra("is_favorite", false)

        player = ExoPlayer.Builder(this).build().apply {
            setMediaItem(MediaItem.fromUri(audioUrl))
            prepare()
            playWhenReady = true
        }

        setContent {
            Rhythm_MusicHubTheme {
                PlayerScreen(
                    onBack = { finish() },
                    trackId = trackId,
                    trackTitle = trackTitle,
                    trackArtist = trackArtist,
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
