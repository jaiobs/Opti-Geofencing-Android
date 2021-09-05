package com.example.optisol.kotlinemap.base


interface ResponseHandler<in T> {
    fun onResponse(responseParser: T)
    fun onFailure(message: String)
}
