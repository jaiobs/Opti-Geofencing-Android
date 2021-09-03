package com.example.orgware.kotlinapicall.app

import com.example.orgware.kotlinapicall.data.respones.polyline.MapResponse
import com.example.orgware.kotlinemap.respones.gird.GirdResponse
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
    fun getGirdvalues(): Observable<GirdResponse>

}