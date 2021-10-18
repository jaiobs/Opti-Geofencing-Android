package com.example.optisol.kotlinemap.ui.distancecalculate

import android.os.Bundle
import com.example.optisol.kotlinemap.R
import com.optisol.optigeofencingandroid.base.BaseActivity
import kotlinx.android.synthetic.main.activity_distance_calculation.*

class DistanceCalculationActivity : BaseActivity() {
    private var distance: Float = 0.0F
    private var distanceConversion: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distance_calculation)
        distance = distanceBetweenTwoLatLng(9.82292F, 77.676095F, 9.966006F, 77.786208F)
        showToast("Distance between My location to Usilampatti is$distance")
        txt_total_distance.text = "Distance between My location to Usilampatti is$distance"
    }

    private fun distanceBetweenTwoLatLng(
        lat_a: Float,
        lng_a: Float,
        lat_b: Float,
        lng_b: Float,
    ): Float {
        val earthRadius = 3958.75
        val latDiff = Math.toRadians((lat_b - lat_a).toDouble())
        val lngDiff = Math.toRadians((lng_b - lng_a).toDouble())
        val a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a.toDouble())) * Math.cos(Math.toRadians(lat_b.toDouble())) *
                Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = earthRadius * c
        val meterConversion = 1609
        return (distance * meterConversion.toFloat()).toFloat()
    }


}