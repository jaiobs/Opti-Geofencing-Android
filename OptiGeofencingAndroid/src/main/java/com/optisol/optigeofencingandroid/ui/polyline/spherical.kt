package com.optisol.optigeofencingandroid.ui.polyline

import com.google.android.gms.maps.model.LatLng

object spherical {
    fun  interpolate(fraction: Float, from: LatLng, to: LatLng): LatLng {
        // http://en.wikipedia.org/wiki/Slerp
        val fromLat = Math.toRadians(from.latitude)
        val fromLng = Math.toRadians(from.longitude)
        val toLat = Math.toRadians(to.latitude)
        val toLng = Math.toRadians(to.longitude)
        val cosFromLat = Math.cos(fromLat)
        val cosToLat = Math.cos(toLat)

        // Computes Spherical interpolation coefficients.
        val angle = computeAngleBetween(fromLat, fromLng, toLat, toLng)
        val sinAngle = Math.sin(angle)
        if (sinAngle < 1E-6) {
            return from
        }
        val a = Math.sin((1 - fraction) * angle) / sinAngle
        val b = Math.sin(fraction * angle) / sinAngle

        // Converts from polar to vector and interpolate.
        val x = a * cosFromLat * Math.cos(fromLng) + b * cosToLat * Math.cos(toLng)
        val y = a * cosFromLat * Math.sin(fromLng) + b * cosToLat * Math.sin(toLng)
        val z = a * Math.sin(fromLat) + b * Math.sin(toLat)

        // Converts interpolated vector back to polar.
        val lat = Math.atan2(z, Math.sqrt(x * x + y * y))
        val lng = Math.atan2(y, x)
        return LatLng(Math.toDegrees(lat), Math.toDegrees(lng))
    }

    private fun computeAngleBetween(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double): Double {
        // Haversine's formula
        val dLat = fromLat - toLat
        val dLng = fromLng - toLng
        return 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(dLat / 2), 2.0) + Math.cos(fromLat) * Math.cos(toLat) * Math.pow(Math.sin(dLng / 2), 2.0)))
    }
}