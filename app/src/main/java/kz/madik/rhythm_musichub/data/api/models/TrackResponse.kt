package kz.madik.rhythm_musichub.data.api.models

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("artist")
    val artist: ArtistResponse,
    @SerializedName("album")
    val album: AlbumResponse?,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("preview")
    val preview: String
)

