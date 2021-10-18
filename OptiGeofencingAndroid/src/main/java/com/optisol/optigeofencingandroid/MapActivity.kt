package com.optisol.optigeofencingandroid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManager
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManagerImpl

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private var REQUEST_LOCATION_CODE = 101
    val locationManager: LocationManager =
        LocationManagerImpl()
    private var latLng: LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapFragment_one) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
//        googleMap?.clear()
        googleMap?.isMyLocationEnabled = true
        this.googleMap = googleMap!!
        startLocationMonitor()
    }

    private fun startLocationMonitor() {
         locationManager.getLastKnownPosition(
                 activity = this,
                 onLastLocationFound = {
                     Log.d("Location", "Last Location - found - lat: " + it.latitude + " lng:" + it.longitude)
                     val cameraPosition = CameraPosition.Builder()
                             .target(LatLng(it.latitude, it.longitude))
                             .zoom(12f)
                             .build()
                     googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                     setMarker( LatLng(it.latitude, it.longitude))
                     latLng = LatLng(it.latitude, it.longitude)
                 },
                 onNoLocationFound = {
                     Log.d("Location", "Last Location - no location found")
                 })
    }

    fun setMarker( latLng: LatLng) {
        googleMap.addMarker(MarkerOptions().anchor(0.5f, 0.5f).position(latLng))
    }

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE)
            }
            return false
        } else {
            return true
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(this@MapActivity, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}