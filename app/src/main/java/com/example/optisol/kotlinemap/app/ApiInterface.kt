package com.example.optisol.kotlinemap.app

import com.optisol.optigeofencingandroid.respones.gird.GirdResponse
import com.optisol.optigeofencingandroid.respones.polyline.MapResponse
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable


interface ApiInterface {


    @GET("directions/json?")
    fun mapvalues(@Query("origin") Origin: String,
                  @Query("destination") Destination: String,
                  @Query("sensor") Sensor: String,
                  @Query("walking") Walking: String,
                  @Query("key") key: String): Observable<MapResponse>


    @GET("serviceTypeImages")
    fun getGirdvalues(): Observable<com.optisol.optigeofencingandroid.respones.gird.GirdResponse>

}