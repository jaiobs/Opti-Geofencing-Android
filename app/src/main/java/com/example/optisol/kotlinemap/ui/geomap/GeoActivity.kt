package com.example.optisol.kotlinemap.ui.geomap

import android.Manifest
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.optisol.optigeofencingandroid.app.AppConstants
import com.optisol.optigeofencingandroid.base.BaseActivity
import com.example.optisol.kotlinemap.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.optisol.optigeofencingandroid.OptiGeoFence
import com.optisol.optigeofencingandroid.respones.georespones.GeoRespones
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManager
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManagerImpl
import com.optisol.optigeofencingandroid.ui.geo.OverallGeoFence
import java.util.*


class GeoActivity : BaseActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mMap: GoogleMap
    private var REQUEST_LOCATION_CODE = 101
    private var circle: Circle? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private val sampleList = ArrayList<GeoRespones>()
    private var builder: LatLngBounds.Builder? = null
    private var cameraUpdate: CameraUpdate? = null
    val locationManager: LocationManager = LocationManagerImpl()
    private var latLng: LatLng? = null
    lateinit var geofencingClient: GeofencingClient
    private val geofenceList = ArrayList<Geofence>()
    private lateinit var overallGeoFence: OverallGeoFence

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_geo)
        overallGeoFence = OptiGeoFence.getGeoFenceList()
        geofencingClient = LocationServices.getGeofencingClient(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment2) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(bundle: Bundle?) {
        startLocationMonitor();
    }

    private fun startLocationMonitor() {

        locationManager.getLastKnownPosition(
            activity = this,
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
                setMarker(R.drawable.hatch, LatLng(it.latitude, it.longitude))
                latLng = LatLng(it.latitude, it.longitude)
            },
            onNoLocationFound = {
                Log.d("Location", "Last Location - no location found")

            })
    }

    fun setMarker(marker: Int, latLng: LatLng) {
        mMap.addMarker(
            MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(marker))
                .position(latLng)
        )
    }


    override fun onConnectionSuspended(p0: Int) {
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
        mMap!!.setMyLocationEnabled(true)

        overallGeoFence.setLocationInMap(mMap, this)
    }

    @Synchronized
    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mGoogleApiClient!!.connect()
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceTransitionsIntentService::class.java)
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
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

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(this@GeoActivity, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}


