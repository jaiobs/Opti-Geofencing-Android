package com.example.optisol.kotlinemap.config

import okhttp3.OkHttpClient


public class GoogleDirectionConfiguration {

    companion object {
        fun getInstance(): GoogleDirectionConfiguration {
            return GoogleDirectionConfiguration()
        }
    }

    private var customClient: OkHttpClient? = null
    private var isLogEnabled = false

    fun getCustomClient(): OkHttpClient? {
        return customClient
    }

    fun setCustomClient(customClient: OkHttpClient?) {
        this.customClient = customClient
    }

    fun isLogEnabled(): Boolean {
        return isLogEnabled
    }

    fun setLogEnabled(logEnabled: Boolean) {
        isLogEnabled = logEnabled
    }
}