package com.example.optisol.kotlinemap.app

import com.example.optisol.kotlinemap.utils.RxJavaUtils
import com.optisol.optigeofencingandroid.respones.gird.GirdResponse
import com.optisol.optigeofencingandroid.respones.polyline.MapResponse
import rx.Observable


class AppRepo(appApi: ApiInterface) {

    private var appApi: ApiInterface = appApi!!

    fun getmap(
        origin: String,
        destination: String,
        sensor: String,
        walking: String,
        key: String
    ): Observable<MapResponse> {
        return appApi.mapvalues(origin, destination, sensor, walking, key)
            .compose(RxJavaUtils.applyErrorTransformer())
            .map { responseMap -> responseMap }
    }

    fun getGird(): Observable<GirdResponse> {
        return appApi.getGirdvalues().compose(RxJavaUtils.applyErrorTransformer())
            .map { girdResponse -> girdResponse }
    }
}