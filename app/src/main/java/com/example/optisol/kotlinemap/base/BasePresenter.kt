package com.example.optisol.kotlinemap.base


interface BasePresenter<in V : BaseView> {
    fun setView(view: V)

    fun destroyView()

    fun destroy()
}