package com.example.orgware.kotlinemap.ui.polyline

import com.example.orgware.kotlinemap.app.AppController
import com.example.orgware.kotlinemap.base.AbstractBasePresenter
import com.example.orgware.kotlinemap.respones.polyline.MapResponse
import com.example.orgware.kotlinemap.utils.RxJavaUtils
import rx.functions.Action1

class PolylinePresenter: AbstractBasePresenter<Polylineview>(){
    private var polylineview: Polylineview? =null

    override fun setView(view: Polylineview) {
        polylineview=view
        appRepo = AppController.getInstanse()!!.getappRepo()

    }

//    fun sample(){
//        polylineview!!.showLoading()
    //        appRepo!!.getmap(" 13.067439, 80.237617" ,"12.9682126,80.25994270000001","false","WALKING")
//                .compose(RxJavaUtils.applyObserverSchedulers())
//                .subscribe(Action1<MapResponse>(){
//
//                        polylineview!!.onSucess(it)
//                        polylineview!!.hideLoading()
//
//                }, Action1<Throwable> {
//                    polylineview!!.onFailure("Failure")
//                    polylineview!!.hideLoading()
//                })
//    }


    fun sample() {
        polylineview!!.showLoading()
        appRepo!!.getmap("9.8175973,77.6752658", "9.9580185,77.775624", "false", "WALKING","AIzaSyAlxiG4-jCSUIkrasvK0dzgEJEfghvSSCQ")
                .compose(RxJavaUtils.applyObserverSchedulers())
                .subscribe(Action1<Any> { mapRespons ->
                        if (mapRespons != null)
                            polylineview!!.onSucess(mapRespons as MapResponse)
                    polylineview!!.hideLoading()

                }, Action1<Throwable> { throwable ->
        polylineview!!.onFailure("failure")
        polylineview!!.hideLoading()
        polylineview!!.showError(throwable.message!!)

                })

    }

}