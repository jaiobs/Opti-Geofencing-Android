package com.optisol.optigeofencingandroid

import com.optisol.optigeofencingandroid.core.CustomMarker
import com.optisol.optigeofencingandroid.core.DirectionsApi
import com.optisol.optigeofencingandroid.core.OverAllPolyline
import com.optisol.optigeofencingandroid.core.SampleGeoFence
import com.optisol.optigeofencingandroid.ui.directions.GetDirectionsAPI
import com.optisol.optigeofencingandroid.ui.geo.OverallGeoFence
import com.optisol.optigeofencingandroid.ui.polyline.DrawPolylines

object OptiGeoFence {
    fun createPolyLine(): DrawPolylines {
        return OverAllPolyline()
    }

    fun getDirectionsApi(): GetDirectionsAPI {
        return DirectionsApi()
    }

    fun getCustomMarker(): CustomMarker {
        return CustomMarker()
    }

    fun getGeoFenceList(): SampleGeoFence {
        return SampleGeoFence()
    }
}