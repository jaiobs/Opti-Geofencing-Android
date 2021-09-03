package com.example.orgware.kotlinemap.base


interface BasePresenter<in V : BaseView> {
    fun setView(view: V)

    fun destroyView()

    fun destroy()
}