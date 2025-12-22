package kz.madik.rhythm_musichub.data.api.models

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("artist")
    val artist: ArtistResponse,
    @SerializedName("album")
    val album: AlbumResponse?,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("preview")
    val preview: String,
    @SerializedName("cover")
    val cover: String?,
    @SerializedName("cover_medium")
    val coverMedium: String?
)

data class ArtistResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class AlbumResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("cover")
    val cover: String?
)

data class SearchResponse(
    @SerializedName("data")
    val data: List<TrackResponse>,
    @SerializedName("total")
    val total: Int
)

data class ChartResponse(
    @SerializedName("tracks")
    val tracks: TracksData
)

data class TracksData(
    @SerializedName("data")
    val data: List<TrackResponse>
)

