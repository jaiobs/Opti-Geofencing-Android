package com.optisol.optigeofencingandroid.ui.geo

import android.content.Context
import com.google.android.gms.maps.GoogleMap

interface OverallGeoFence  {
    fun setLocationInMap(mMap: GoogleMap, context: Context)
}