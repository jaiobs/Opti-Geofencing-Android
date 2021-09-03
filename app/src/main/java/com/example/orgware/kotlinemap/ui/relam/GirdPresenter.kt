package com.example.orgware.kotlinemap.ui.relam

import com.example.orgware.kotlinapicall.app.AppController
import com.example.orgware.kotlinapicall.base.AbstractBasePresenter
import com.example.orgware.kotlinapicall.utils.RxJavaUtils
import com.example.orgware.kotlinemap.respones.gird.GirdResponse
import rx.functions.Action1

class GirdPresenter : AbstractBasePresenter<GirdView>() {
    var girdView: GirdView? = null

    override fun setView(view: GirdView) {
        girdView = view
        appRepo = AppController.getInstanse()!!.getappRepo()
    }


    fun gird() {
        girdView!!.showLoading()
        appRepo!!.getGird()
                .compose(RxJavaUtils.applyObserverSchedulers())
                .subscribe(Action1<Any> { girdRespons ->
                    if (girdRespons != null)
                        girdView!!.onSucess(girdRespons as GirdResponse)
                    girdView!!.hideLoading()

                }, Action1<Throwable> { throwable ->
                    girdView!!.onFailure("failure")
                    girdView!!.hideLoading()
                    girdView!!.showError(throwable.message!!)

                })
    }

}