package kz.madik.rhythm_musichub.viewmodel

import android.app.Application
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
        loadChartTracks()
        loadFavoriteTracks()
    }

    fun loadChartTracks() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.fetchChartTracks().fold(
                onSuccess = { tracks ->
                    val trackEntities = tracks.map {
                        with(repository) { it.toEntity() }
                    }
                    _chartTracks.value = trackEntities
                    _isLoading.value = false
                },
                onFailure = { error ->
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
            _isLoading.value = true
            repository.searchTracksRemote(query).fold(
                onSuccess = { tracks ->
                    val trackEntities = tracks.map {
                        with(repository) { it.toEntity() }
                    }
                    _searchResults.value = trackEntities
                    _isLoading.value = false
                },
                onFailure = { error ->
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

