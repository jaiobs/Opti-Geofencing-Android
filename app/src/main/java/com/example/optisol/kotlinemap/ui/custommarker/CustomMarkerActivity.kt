package com.example.optisol.kotlinemap.ui.custommarker

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.optisol.optigeofencingandroid.base.BaseActivity
import com.example.optisol.kotlinemap.R
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.optisol.optigeofencingandroid.OptiGeoFence
import com.optisol.optigeofencingandroid.core.CustomMarker
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManager
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManagerImpl
import com.optisol.optigeofencingandroid.ui.custommarker.CustomMarkerListener
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class CustomMarkerActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var REQUEST_LOCATION_CODE = 101
    val locationManager: LocationManager =
        LocationManagerImpl()
    private var latLng: LatLng? = null
    lateinit var geofencingClient: GeofencingClient
    private var newMarker: Marker? = null
    private lateinit var customMarker: CustomMarker

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_custom_marker)
        customMarker = OptiGeoFence.getCustomMarker()
        geofencingClient = LocationServices.getGeofencingClient(this)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment2) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    fun setMarker(latLng: LatLng) {
        newMarker = mMap.addMarker(
            MarkerOptions().anchor(0.5f, 0.5f).icon(
                BitmapDescriptorFactory.fromBitmap(
                    customMarker.createMarker(
                        this,
                        "url",
                        "model.name",mMap
                    )
                )
            ).title("Username")
                .snippet("${latLng.latitude},${latLng.longitude}")
                .position(latLng)
        )
    }

    override fun onMapReady(mMap: GoogleMap?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap!!.clear()
        mMap!!.isMyLocationEnabled = true
        this.mMap = mMap


        mMap.setOnMarkerClickListener { marker ->
            Toast.makeText(applicationContext, "Click marker", Toast.LENGTH_LONG).show()
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            true
        }
        startLocationMonitor(this)
    }

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
                )
            }
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //mMap.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(
                        this@CustomMarkerActivity,
                        "permission denied",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    fun startLocationMonitor(context: Context) {
        locationManager.getLastKnownPosition(
            activity = context as Activity,
            onLastLocationFound = {
                Log.d(
                    "Location",
                    "Last Location - found - lat: " + it.latitude + " lng:" + it.longitude
                )
                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(it.latitude, it.longitude))
                    .zoom(12f)
                    .build()

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                setMarker(LatLng(it.latitude, it.longitude))
                latLng = LatLng(it.latitude, it.longitude)
            },
            onNoLocationFound = {
                Log.d("Location", "Last Location - no location found")

            })
    }
}


