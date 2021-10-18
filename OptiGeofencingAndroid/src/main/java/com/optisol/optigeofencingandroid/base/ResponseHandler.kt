package com.optisol.optigeofencingandroid.base


interface ResponseHandler<in T> {
    fun onResponse(responseParser: T)
    fun onFailure(message: String)
}
