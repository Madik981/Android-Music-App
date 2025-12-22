package kz.madik.rhythm_musichub.data.api

import kz.madik.rhythm_musichub.data.api.models.ChartResponse
import kz.madik.rhythm_musichub.data.api.models.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerApiService {

    @GET("chart")
    suspend fun getChart(): ChartResponse

    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String
    ): SearchResponse
}

