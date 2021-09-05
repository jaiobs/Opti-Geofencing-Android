package com.example.optisol.kotlinemap.helper.location

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task

class LocationHelper(
    private val activity: Activity,
    private val locationManager: LocationManagers?
) {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var locationRequest: LocationRequest? = null
    private var task: Task<LocationSettingsResponse>? = null
    private fun createLocationCallBack() {
        if (locationCallback == null) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    if (locationResult != null) {
                        for (location in locationResult.locations) {
                            if (location != null && locationManager != null) {
                                if (locationManager != null) {
                                    locationManager.onLocationChanged(location)
                                }
                            }
                        }
                    }
                }
            }
        }
        startLocationUpdates()
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest!!.interval = INTERVAL
//        locationRequest!!.fastestInterval = FAST_INTERVAL
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest!!.maxWaitTime = MAX_WAIT_TIME
    }

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, 1234)
            return
        }
        fusedLocationClient!!.requestLocationUpdates(
            locationRequest,locationCallback,
            Looper.myLooper()
        )
        lastKnownLocation
    }

//    private val pendingIntent: PendingIntent
//        private get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val intent = Intent(activity, LocationUpdatesBroadcastReceiver::class.java)
//            intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
//            PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        } else {
//            val intent = Intent(activity, LocationUpdatesIntentService::class.java)
//            intent.action = LocationUpdatesIntentService.ACTION_PROCESS_UPDATES
//            PendingIntent.getService(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        }

    fun enableLocationSettings() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(
            locationRequest!!
        )
        val client = LocationServices.getSettingsClient(activity)
        task = client.checkLocationSettings(builder.build())
        task!!.addOnSuccessListener(
            activity,
            OnSuccessListener { // All location settings are satisfied. The client can initialize
                // location requests here.
                createLocationCallBack()
            })
        task?.addOnFailureListener(activity, OnFailureListener { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        activity,
                        1234
                    )
                } catch (sendEx: SendIntentException) {
                    // Ignore the error.
                }
            }
        })
    }

    fun stopLocationUpdates() {
        fusedLocationClient!!.removeLocationUpdates(locationCallback)
    }

    private val lastKnownLocation: Unit
        private get() {
            if (fusedLocationClient == null) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            }
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    locationManager?.getLastKnownLocation(location)
                }
            }
        }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1234) {
            if (resultCode == Activity.RESULT_OK) {
                createLocationCallBack()
            } else {
                enableLocationSettings()
            }
        }
    }

    companion object {
        private const val INTERVAL: Long = 100
//        private const val FAST_INTERVAL: Long = 500000000
//        private const val MAX_WAIT_TIME: Long = 500000000
        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }

    init {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        }
        createLocationRequest()
        createLocationCallBack()
    }
}