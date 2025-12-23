package kz.madik.rhythm_musichub.data.api.models

import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)

