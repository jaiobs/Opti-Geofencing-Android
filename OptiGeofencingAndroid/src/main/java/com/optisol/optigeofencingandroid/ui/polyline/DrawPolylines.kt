package com.optisol.optigeofencingandroid.ui.polyline

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import java.util.ArrayList

interface DrawPolylines {
    fun animateMarker(context: Context, m: Marker?, routesItems: ArrayList<LatLng>, mMap: GoogleMap)
    fun getAngle(startValue: LatLng, endValue: LatLng?): Any
    fun decodePoly(points: String): Collection<LatLng>
}