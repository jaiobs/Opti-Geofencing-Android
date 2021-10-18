package com.example.optisol.kotlinemap.base

import com.optisol.optigeofencingandroid.base.BaseView


interface BasePresenter<in V : BaseView> {
    fun setView(view: V)

    fun destroyView()

    fun destroy()
}