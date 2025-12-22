package kz.madik.rhythm_musichub.data.repository

import kz.madik.rhythm_musichub.data.api.DeezerApiService
import kz.madik.rhythm_musichub.data.api.models.TrackResponse
import kz.madik.rhythm_musichub.data.db.dao.TrackDao
import kz.madik.rhythm_musichub.data.db.entities.TrackEntity
import kotlinx.coroutines.flow.Flow

class MusicRepository(
    private val trackDao: TrackDao,
    private val apiService: DeezerApiService
) {

    // Local Database operations
    fun getAllTracks(): Flow<List<TrackEntity>> = trackDao.getAllTracks()

    fun getFavoriteTracks(): Flow<List<TrackEntity>> = trackDao.getFavoriteTracks()

    fun searchTracksLocal(query: String): Flow<List<TrackEntity>> = trackDao.searchTracks(query)

    suspend fun getTrackById(trackId: String): TrackEntity? = trackDao.getTrackById(trackId)

    suspend fun insertTrack(track: TrackEntity) = trackDao.insertTrack(track)

    suspend fun updateFavorite(trackId: String, isFavorite: Boolean) =
        trackDao.updateFavorite(trackId, isFavorite)

    suspend fun deleteTrack(track: TrackEntity) = trackDao.deleteTrack(track)

    // API operations
    suspend fun fetchChartTracks(): Result<List<TrackResponse>> {
        return try {
            val response = apiService.getChart()
            Result.success(response.tracks.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchTracksRemote(query: String): Result<List<TrackResponse>> {
        return try {
            val response = apiService.searchTracks(query)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Convert API model to Entity
    suspend fun TrackResponse.toEntity(): TrackEntity {
        val trackId = this.id.toString()
        val existingTrack = trackDao.getTrackById(trackId)

        return TrackEntity(
            id = trackId,
            title = this.title,
            artist = this.artist.name,
            album = this.album?.title,
            duration = this.duration.toLong(),
            audioUrl = this.preview,
            coverUrl = this.album?.coverMedium
                ?: this.album?.coverBig
                ?: this.album?.cover
                ?: this.album?.coverSmall,
            isFavorite = existingTrack?.isFavorite ?: false
        )
    }
}
