package com.example.orgware.kotlinapicall.app

import com.example.orgware.kotlinapicall.data.respones.polyline.MapResponse
import com.example.orgware.kotlinapicall.utils.RxJavaUtils
import com.example.orgware.kotlinemap.respones.gird.GirdResponse
import io.realm.RealmConfiguration
import rx.Observable


class AppRepo(appApi: ApiInterface,realmConfiguration: RealmConfiguration?) {


    private var appApi: ApiInterface
    private var realmConfiguration: RealmConfiguration


//    init {
//        this.appApi = appApi
//    }

    init {
        this.realmConfiguration = realmConfiguration!!
        this.appApi = appApi!!
    }


//    fun getmap(origin: String, destination: String, sensor: String, walking: String): Observable<MapResponse> {
//        return appApi.mapvalues(origin, destination, sensor, walking)
//                .compose(RxJavaUtils.applyErrorTransformer())
//                .map(Func1 { return@Func1 it  })
//    }

        fun getmap(origin: String, destination: String, sensor: String, walking: String,key:String): Observable<MapResponse> {
            return appApi.mapvalues(origin, destination, sensor, walking,key)
                    .compose(RxJavaUtils.applyErrorTransformer())
                    .map { responseMap -> responseMap }
        }

    fun getGird():Observable<GirdResponse> {
        return appApi.getGirdvalues().compose(RxJavaUtils.applyErrorTransformer())
                .map{ girdResponse -> girdResponse}
    }
}