package com.example.optisol.kotlinemap.base

import com.example.optisol.kotlinemap.app.AppRepo
import com.optisol.optigeofencingandroid.base.BasePresenter
import com.optisol.optigeofencingandroid.base.BaseView
import com.optisol.optigeofencingandroid.base.ResponseHandler


open class AbstractBasePresenter<in V : BaseView> : BasePresenter<V>, ResponseHandler<Any> {


    private var view: V? = null
    protected var appRepo: AppRepo? = null

    override fun setView(view: V) {
        this.view = view

    }

    override fun destroyView() {
        view = null

    }

    override fun destroy() {

    }

    override fun onResponse(responesApi: Any) {

    }

    override fun onFailure(message: String) {

    }


}
