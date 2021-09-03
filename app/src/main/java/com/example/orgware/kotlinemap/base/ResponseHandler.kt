package com.example.orgware.kotlinemap.base


interface ResponseHandler<in T> {

    fun onResponse(responseParser: T)

    fun onFailure(message: String)




}
