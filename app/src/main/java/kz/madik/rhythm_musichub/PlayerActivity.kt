package kz.madik.rhythm_musichub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kz.madik.rhythm_musichub.ui.player.PlayerScreen
import kz.madik.rhythm_musichub.ui.theme.Rhythm_MusicHubTheme

class PlayerActivity : ComponentActivity() {

    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val audioUrl = intent.getStringExtra("audio_url")
            ?: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        val trackTitle = intent.getStringExtra("track_title") ?: "Demo Track"
        val trackArtist = intent.getStringExtra("track_artist") ?: "Unknown Artist"
        val coverUrl = intent.getStringExtra("cover_url")

        player = ExoPlayer.Builder(this).build().apply {
            setMediaItem(MediaItem.fromUri(audioUrl))
            prepare()
            playWhenReady = true
        }

        setContent {
            Rhythm_MusicHubTheme {
                PlayerScreen(
                    onBack = { finish() },
                    trackTitle = trackTitle,
                    trackArtist = trackArtist,
                    coverUrl = coverUrl
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
