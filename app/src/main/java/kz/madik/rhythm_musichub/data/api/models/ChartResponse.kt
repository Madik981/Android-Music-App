package kz.madik.rhythm_musichub.data.api.models

import com.google.gson.annotations.SerializedName

data class ChartResponse(
    @SerializedName("tracks")
    val tracks: TracksData
)

