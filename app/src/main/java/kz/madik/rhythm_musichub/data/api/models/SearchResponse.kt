package kz.madik.rhythm_musichub.data.api.models

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("data")
    val data: List<TrackResponse>,
    @SerializedName("total")
    val total: Int
)

