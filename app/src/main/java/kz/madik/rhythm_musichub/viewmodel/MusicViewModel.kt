package kz.madik.rhythm_musichub.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kz.madik.rhythm_musichub.data.api.RetrofitClient
import kz.madik.rhythm_musichub.data.db.MusicDatabase
import kz.madik.rhythm_musichub.data.db.entities.TrackEntity
import kz.madik.rhythm_musichub.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "MusicViewModel"
    }

    private val repository: MusicRepository

    private val _chartTracks = MutableStateFlow<List<TrackEntity>>(emptyList())
    val chartTracks: StateFlow<List<TrackEntity>> = _chartTracks.asStateFlow()

    private val _searchResults = MutableStateFlow<List<TrackEntity>>(emptyList())
    val searchResults: StateFlow<List<TrackEntity>> = _searchResults.asStateFlow()

    private val _favoriteTracks = MutableStateFlow<List<TrackEntity>>(emptyList())
    val favoriteTracks: StateFlow<List<TrackEntity>> = _favoriteTracks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        val database = MusicDatabase.getDatabase(application)
        repository = MusicRepository(database.trackDao(), RetrofitClient.deezerApiService)
        Log.d(TAG, "ViewModel initialized, loading chart tracks...")
        loadChartTracks()
        loadFavoriteTracks()
    }

    fun loadChartTracks() {
        viewModelScope.launch {
            Log.d(TAG, "loadChartTracks started")
            _isLoading.value = true
            repository.fetchChartTracks().fold(
                onSuccess = { tracks ->
                    Log.d(TAG, "fetchChartTracks SUCCESS: received ${tracks.size} tracks")
                    val trackEntities = tracks.map {
                        with(repository) { it.toEntity() }
                    }
                    Log.d(TAG, "Converted to ${trackEntities.size} entities")
                    trackEntities.take(5).forEachIndexed { index, track ->
                        Log.d(TAG, "Track $index: ${track.title} by ${track.artist}, cover: ${track.coverUrl}")
                    }
                    _chartTracks.value = trackEntities
                    Log.d(TAG, "Updated _chartTracks.value, size: ${_chartTracks.value.size}")
                    _isLoading.value = false
                },
                onFailure = { error ->
                    Log.e(TAG, "fetchChartTracks FAILURE: ${error.message}", error)
                    _errorMessage.value = error.message
                    _isLoading.value = false
                }
            )
        }
    }

    fun searchTracks(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            Log.d(TAG, "searchTracks started for query: $query")
            _isLoading.value = true
            repository.searchTracksRemote(query).fold(
                onSuccess = { tracks ->
                    Log.d(TAG, "searchTracksRemote SUCCESS: received ${tracks.size} tracks")
                    val trackEntities = tracks.map {
                        with(repository) { it.toEntity() }
                    }
                    Log.d(TAG, "Converted to ${trackEntities.size} search result entities")
                    trackEntities.take(5).forEachIndexed { index, track ->
                        Log.d(TAG, "Search result $index: ${track.title} by ${track.artist}, cover: ${track.coverUrl}")
                    }
                    _searchResults.value = trackEntities
                    Log.d(TAG, "Updated _searchResults.value, size: ${_searchResults.value.size}")
                    _isLoading.value = false
                },
                onFailure = { error ->
                    Log.e(TAG, "searchTracksRemote FAILURE: ${error.message}", error)
                    _errorMessage.value = error.message
                    _isLoading.value = false
                }
            )
        }
    }

    fun toggleFavorite(track: TrackEntity) {
        viewModelScope.launch {
            val updatedTrack = track.copy(isFavorite = !track.isFavorite)
            repository.insertTrack(updatedTrack)
            loadFavoriteTracks()

            // Обновляем трек в списке результатов поиска
            _searchResults.value = _searchResults.value.map {
                if (it.id == track.id) updatedTrack else it
            }

            // Обновляем трек в списке чартов
            _chartTracks.value = _chartTracks.value.map {
                if (it.id == track.id) updatedTrack else it
            }
        }
    }

    private fun loadFavoriteTracks() {
        viewModelScope.launch {
            repository.getFavoriteTracks().collect { tracks ->
                _favoriteTracks.value = tracks
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
