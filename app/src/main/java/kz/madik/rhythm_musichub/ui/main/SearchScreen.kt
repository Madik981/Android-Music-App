package kz.madik.rhythm_musichub.ui.main

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kz.madik.rhythm_musichub.PlayerActivity
import kz.madik.rhythm_musichub.R
import kz.madik.rhythm_musichub.data.db.entities.TrackEntity
import kz.madik.rhythm_musichub.viewmodel.MusicViewModel

@Composable
fun SearchScreen(
    padding: PaddingValues,
    viewModel: MusicViewModel,
    @Suppress("UNUSED_PARAMETER") navController: NavController
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(query) {
        if (query.isNotBlank()) {
            delay(500) // Debounce
            viewModel.searchTracks(query)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(padding)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 0.dp
                )
    ) {
        Text(
            text = stringResource(R.string.search_title),
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            value = query,
            onValueChange = { query = it },
            onClear = { query = "" }
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            query.isBlank() -> {
                Text(
                    text = stringResource(R.string.search_browse_all),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                BrowseCategories()
            }
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1DB954))
                }
            }
            searchResults.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.search_no_results),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(searchResults) { track ->
                        SearchResultRow(
                            track = track,
                            onTrackClick = {
                                val intent = Intent(context, PlayerActivity::class.java).apply {
                                    putExtra("track_id", track.id)
                                    putExtra("audio_url", track.audioUrl)
                                    putExtra("track_title", track.title)
                                    putExtra("track_artist", track.artist)
                                    putExtra("track_album", track.album)
                                    putExtra("cover_url", track.coverUrl)
                                    putExtra("track_duration", track.duration)
                                    putExtra("is_favorite", track.isFavorite)
                                }
                                context.startActivity(intent)
                            },
                            onFavoriteClick = { viewModel.toggleFavorite(track) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = stringResource(R.string.search_hint),
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color.Gray
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFF282828),
            focusedContainerColor = Color(0xFF282828),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color(0xFF1DB954),
            unfocusedBorderColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
fun SearchResultRow(
    track: TrackEntity,
    onTrackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTrackClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = track.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (track.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (track.isFavorite) Color(0xFF1DB954) else Color.Gray
            )
        }
    }
}

@Composable
fun BrowseCategories() {
    val categories = listOf(
        "Pop" to Color(0xFFE13300),
        "Hip-Hop" to Color(0xFFBA5D07),
        "Rock" to Color(0xFF8D67AB),
        "Jazz" to Color(0xFF1E3264),
        "Electronic" to Color(0xFF0D73EC),
        "Classical" to Color(0xFF477D95)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { (title, color) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable { /* Handle category click */ },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}
