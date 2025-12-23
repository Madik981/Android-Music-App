package kz.madik.rhythm_musichub.data.api.models

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("cover")
    val cover: String?,
    @SerializedName("cover_small")
    val coverSmall: String?,
    @SerializedName("cover_medium")
    val coverMedium: String?,
    @SerializedName("cover_big")
    val coverBig: String?,
    @SerializedName("cover_xl")
    val coverXl: String?
)

