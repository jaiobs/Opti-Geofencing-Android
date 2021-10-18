package com.optisol.optigeofencingandroid.core

import android.Manifest
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.optisol.optigeofencingandroid.app.AppConstants
import com.optisol.optigeofencingandroid.respones.georespones.GeoRespones
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManager
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManagerImpl
import com.optisol.optigeofencingandroid.ui.geo.OverallGeoFence
import java.util.*

open class SampleGeoFence : OverallGeoFence {

    private lateinit var mMap: GoogleMap
    private var circle: Circle? = null
    private val sampleList = ArrayList<GeoRespones>()
    private var builder: LatLngBounds.Builder? = null
    private var cameraUpdate: CameraUpdate? = null
    val locationManager: LocationManager = LocationManagerImpl()
    private val geofenceList = ArrayList<Geofence>()

    override fun setLocationInMap(mMap: GoogleMap, context: Context) {
        this.mMap = mMap

        sampleList.add(GeoRespones(12.961561, 80.256768, "Palavakkam"))
        sampleList.add(GeoRespones(12.961525, 80.256320, "Palavakkam"))
        sampleList.add(GeoRespones(12.9622153, 80.2568426, "Palavakkam"))

        for (i in sampleList.indices) {
            mMap!!.addMarker(
                MarkerOptions().position(
                    LatLng(
                        sampleList.get(i).Lat, sampleList[i].lon
                    )
                ).title(sampleList[i].name)
            )
            geofenceList.add(
                Geofence.Builder()
                    .setRequestId("unique" + i)
                    .setCircularRegion(
                        sampleList[i].Lat, sampleList[i].lon,
//                            entry.value.latitude,
//                            entry.value.longitude,
                        AppConstants.GEOFENCE_RADIUS_IN_METERS.toFloat()
                    )
                    .setExpirationDuration(100000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            )
            circle = mMap!!.addCircle(
                CircleOptions()
                    .center(LatLng(sampleList.get(i).Lat, sampleList.get(i).lon))
            )

            circle!!.radius = 5.1
            circle!!.strokeColor = Color.RED
            circle!!.fillColor = 0x220000FF
            circle!!.strokeWidth = 4f
            circle!!.zIndex = 20f
        }

        val vAnimator = ValueAnimator()
        vAnimator.repeatCount = ValueAnimator.INFINITE
        vAnimator.repeatMode = ValueAnimator.RESTART /* PULSE */
        vAnimator.setIntValues(0, 100)
        vAnimator.duration = 1000
        vAnimator.setEvaluator(IntEvaluator())
        vAnimator.interpolator = AccelerateDecelerateInterpolator()
        vAnimator.addUpdateListener { valueAnimator ->
            val animatedFraction = valueAnimator.animatedFraction
            // Log.e("", "" + animatedFraction);
            circle!!.radius = animatedFraction * 100.toDouble()
        }
        vAnimator.start()

        builder = LatLngBounds.Builder()
        for (m in sampleList) {
            builder!!.include(LatLng(m.Lat, m.lon))
        }
        val padding = 200
        val bounds = builder!!.build()

        cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap!!.setOnMapLoadedCallback(GoogleMap.OnMapLoadedCallback {
            mMap!!.animateCamera(
                cameraUpdate
            )
        })

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
//        geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
//            addOnSuccessListener {
//            }
//            addOnFailureListener {
//            }
//        }

//        geofencingClient?.removeGeofences(geofencePendingIntent)?.run {
//            addOnSuccessListener {
//            }
//            addOnFailureListener {
//            }
//        }


    }
}