package kz.madik.rhythm_musichub.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kz.madik.rhythm_musichub.R

@Composable
fun HomeScreen(padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(padding)
            .padding(horizontal = 16.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Good evening",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            FeaturedPlaylists()
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Recently Played",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(dummyTracks) { track ->
            TrackRow(track)
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}


@Composable
fun FeaturedPlaylists() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(dummyPlaylists) { playlist ->
            PlaylistCard(playlist)
        }
    }
}


@Composable
fun PlaylistCard(title: String) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1DB954))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_music),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun TrackRow(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = track.artist,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

data class Track(
    val title: String,
    val artist: String
)

val dummyTracks = listOf(
    Track("Blinding Lights", "The Weeknd"),
    Track("As It Was", "Harry Styles"),
    Track("Levitating", "Dua Lipa"),
    Track("Shape of You", "Ed Sheeran"),
    Track("Save Your Tears", "The Weeknd")
)

val dummyPlaylists = listOf(
    "Top Hits",
    "Chill Vibes",
    "Workout",
    "Focus",
    "Daily Mix"
)
