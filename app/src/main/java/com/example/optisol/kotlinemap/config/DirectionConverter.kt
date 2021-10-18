package com.example.optisol.kotlinemap.config

import android.content.Context
import android.util.DisplayMetrics
import com.optisol.optigeofencingandroid.model.Step
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import java.util.*

object DirectionConverter {
    fun getDirectionPoint(stepList: List<Step>?): ArrayList<LatLng> {
        val directionPointList = ArrayList<LatLng>()
        if (stepList != null && stepList.isNotEmpty()) {
            for (step in stepList) {
                convertStepToPosition(step, directionPointList)
            }
        }
        return directionPointList
    }

    private fun convertStepToPosition(step: Step, directionPointList: ArrayList<LatLng>) {
        // Get start location
        directionPointList.add(step.startLocation!!.coordination)

        // Get encoded points location
        if (step.polyline != null) {
            val decodedPointList = step.polyline!!.pointList
            if (decodedPointList != null && decodedPointList.isNotEmpty()) {
                for (position in decodedPointList) {
                    directionPointList.add(position)
                }
            }
        }

        // Get end location
        directionPointList.add(step.endLocation!!.coordination)
    }

    fun getSectionPoint(stepList: List<Step>?): ArrayList<LatLng> {
        val directionPointList = ArrayList<LatLng>()
        if (stepList != null && stepList.isNotEmpty()) {
            // Get start location only first position
            directionPointList.add(stepList[0].startLocation!!.coordination)
            for (step in stepList) {
                // Get end location
                directionPointList.add(step.endLocation!!.coordination)
            }
        }
        return directionPointList
    }

    fun createPolyline(
        context: Context,
        locationList: ArrayList<LatLng>,
        width: Int,
        color: Int
    ): PolylineOptions {
        val rectLine =
            PolylineOptions().width(dpToPx(context, width).toFloat()).color(color).geodesic(true)
        for (location in locationList) {
            rectLine.add(location)
        }
        return rectLine
    }

    fun createTransitPolyline(
        context: Context,
        stepList: List<Step>?,
        transitWidth: Int,
        transitColor: Int,
        walkingWidth: Int,
        walkingColor: Int
    ): ArrayList<PolylineOptions> {
        val polylineOptionsList = ArrayList<PolylineOptions>()
        if (stepList != null && stepList.isNotEmpty()) {
            for (step in stepList) {
                val directionPointList = ArrayList<LatLng>()
                convertStepToPosition(step, directionPointList)
                if (step.isContainStepList) {
                    polylineOptionsList.add(
                        createPolyline(
                            context,
                            directionPointList,
                            walkingWidth,
                            walkingColor
                        )
                    )
                } else {
                    polylineOptionsList.add(
                        createPolyline(
                            context,
                            directionPointList,
                            transitWidth,
                            transitColor
                        )
                    )
                }
            }
        }
        return polylineOptionsList
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round((dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toFloat())
    }
}