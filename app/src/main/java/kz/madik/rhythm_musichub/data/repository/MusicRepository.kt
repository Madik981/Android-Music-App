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

    fun getFavoriteTracks(): Flow<List<TrackEntity>> = trackDao.getFavoriteTracks()

    suspend fun insertTrack(track: TrackEntity) = trackDao.insertTrack(track)

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
