package com.example.orgware.kotlinemap.ui.four

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.example.orgware.kotlinapicall.base.BaseActivity
import com.example.orgware.kotlinemap.R
import com.example.orgware.kotlinemap.respones.georespones.GeoRespones
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.grumpyshoe.module.locationmanager.LocationManager
import com.grumpyshoe.module.locationmanager.impl.LocationManagerImpl
import java.util.*


class FourActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var REQUEST_LOCATION_CODE = 101
    val locationManager: LocationManager = LocationManagerImpl()
    private var latLng: LatLng? = null
    lateinit var geofencingClient: GeofencingClient



    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_one)
        geofencingClient = LocationServices.getGeofencingClient(this)



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.fragment2) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun startLocationMonitor() {

        locationManager.getLastKnownPosition(
                activity = this,
                onLastLocationFound = {
                    Log.d("Location", "Last Location - found - lat: " + it.latitude + " lng:" + it.longitude)
                    latLng = LatLng(it.latitude, it.longitude)

                    addPolyLine()
                },
                onNoLocationFound = {
                    Log.d("Location", "Last Location - no location found")

                })
    }

    fun setMarker( latLng: LatLng) {
        mMap.addMarker(MarkerOptions().anchor(0.5f, 0.5f).position(latLng))
        addPolyLine()
    }

    fun addPolyLine(){


        val cameraPosition = CameraPosition.Builder()
                .target(LatLng(12.961561, 80.256768 ))
                .zoom(12f)
                .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        val sampleList = ArrayList<LatLng>()

        sampleList.add(LatLng(12.961561, 80.256768 ))
        sampleList.add(LatLng(13.016201, 80.225611))


        val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
        for (z in 0 until sampleList.size) {
            val point: LatLng = sampleList.get(z)
            options.add(point)
        }
        mMap.addPolyline(options)

    }



    override fun onMapReady(mMap: GoogleMap?) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mMap!!.clear()
        mMap!!.setMyLocationEnabled(true)
        this.mMap = mMap

        startLocationMonitor()


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
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                        mMap.isMyLocationEnabled = true
                    }

                } else {
                    Toast.makeText(this@FourActivity, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}


