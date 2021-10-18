package com.optisol.optigeofencingandroid.ui.directions

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.optisol.optigeofencingandroid.model.Direction

interface GetDirectionsAPI {
    fun getDirectionSuccess(direction: Direction, context: Context, map: GoogleMap?)
    fun setGoogleMapScreen(context: Context, map: GoogleMap?)
}