package com.example.optisol.kotlinemap.helper.location

import android.location.Location

interface LocationManagers {
    fun getLastKnownLocation(lastLocation: Location?)
    fun onLocationChanged(location: Location?)
}