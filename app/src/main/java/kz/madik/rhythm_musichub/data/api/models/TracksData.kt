package kz.madik.rhythm_musichub.data.api.models

import com.google.gson.annotations.SerializedName

data class TracksData(
    @SerializedName("data")
    val data: List<TrackResponse>
)

