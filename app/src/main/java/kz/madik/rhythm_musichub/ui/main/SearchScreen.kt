package kz.madik.rhythm_musichub.ui.main

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kz.madik.rhythm_musichub.PlayerActivity
import kz.madik.rhythm_musichub.R
import kz.madik.rhythm_musichub.ui.components.search.BrowseCategories
import kz.madik.rhythm_musichub.ui.components.search.SearchResultRow
import kz.madik.rhythm_musichub.ui.components.search.SearchBar
import kz.madik.rhythm_musichub.viewmodel.MusicViewModel

@Composable
fun SearchScreen(
    padding: PaddingValues,
    viewModel: MusicViewModel
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(query) {
        if (query.isNotBlank()) {
            delay(500)
            viewModel.searchTracks(query)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            color = MaterialTheme.colorScheme.onBackground,
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
                    color = MaterialTheme.colorScheme.onBackground,
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
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
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
