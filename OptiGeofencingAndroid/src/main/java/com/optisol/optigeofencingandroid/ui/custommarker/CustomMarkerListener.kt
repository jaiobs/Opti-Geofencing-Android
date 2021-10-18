package com.optisol.optigeofencingandroid.ui.custommarker

import android.content.Context
import android.graphics.Bitmap
import com.google.android.gms.maps.GoogleMap

interface CustomMarkerListener {
    fun createMarker(
        context: Context,
        name: String,
        modelName: String,
        googleMap: GoogleMap?
    ): Bitmap?
}