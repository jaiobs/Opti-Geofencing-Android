package com.optisol.optigeofencingandroid.core

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Color
import android.util.Property
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions
import com.optisol.optigeofencingandroid.ui.polyline.DrawPolylines
import com.optisol.optigeofencingandroid.ui.polyline.spherical
import java.util.ArrayList

class OverAllPolyline() : DrawPolylines {

    private lateinit var mMapp: GoogleMap
    private var routesItems = ArrayList<LatLng>()
    private val kDegreesToRadians = Math.PI / 180.0
    private val kRadiansToDegrees = 180.0 / Math.PI

    override fun animateMarker(
        context: Context,
        m: Marker?,
        mList: ArrayList<LatLng>,
        mMap: GoogleMap
    ) {
        routesItems = mList
        this.mMapp = mMap
        val evaluator = TypeEvaluator<LatLng> { fraction, startValue: LatLng, endValue ->
            val angle = getAngle(startValue, endValue)
            m!!.rotation = angle.toFloat()
            spherical.interpolate(fraction, startValue, endValue)
        }
        val property = Property.of(Marker::class.java, LatLng::class.java, "Position")
        val animator = ObjectAnimator.ofObject(m, property, evaluator, mList[0])
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                if (mList.size > 1) {
                    mList.removeAt(0)
                    animateMarker(context, m, mList, mMap)
                }
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        animator.duration = 100
        animator.start()
        val lineOptions = PolylineOptions()
        lineOptions.addAll(routesItems)
        lineOptions.width(10f)
        lineOptions.color(Color.RED)
        mMapp.addPolyline(lineOptions)
    }

    override fun getAngle(curretLatLng: LatLng, toLatLng: LatLng?): Double {
        val fromLong = curretLatLng.longitude * kDegreesToRadians
        val toLong = toLatLng?.longitude!! * kDegreesToRadians
        val fromLat = curretLatLng.latitude * kDegreesToRadians
        val toLat = toLatLng.latitude * kDegreesToRadians
        val dlon = toLong - fromLong
        val y = Math.sin(dlon) * Math.cos(toLat)
        val x =
            Math.cos(fromLat) * Math.sin(toLat) - Math.sin(fromLat) * Math.cos(toLat) * Math.cos(
                dlon
            )
        var direction = Math.atan2(y, x)
        // convert to degrees
        direction = direction * kRadiansToDegrees
        // normalize
        val fr = direction + 360.0
        val fr_long = fr.toLong()
        val fr_final = fr - fr_long
        direction += fr_final
        return direction
    }

    override fun decodePoly(encoded: String): Collection<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }
}