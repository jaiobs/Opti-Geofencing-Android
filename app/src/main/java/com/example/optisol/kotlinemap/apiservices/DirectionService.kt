package com.example.optisol.kotlinemap.apiservices

import com.example.optisol.kotlinemap.config.Constants
import com.example.optisol.kotlinemap.model.Direction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface DirectionService {
    @GET(Constants.DIRECTION_API_URL)
    fun getDirection(
        @Query("origin") origin: String?,
        @Query("destination") destination: String?,
        @Query("mode") mode: String?,
        @Query("alternatives") alternatives: Boolean?,
        @Query("key") key: String?
    ): Call<Direction?>?
}