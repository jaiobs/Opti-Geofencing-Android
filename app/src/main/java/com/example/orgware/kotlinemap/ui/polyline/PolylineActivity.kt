package com.example.orgware.kotlinapicall.ui.polyline

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.example.orgware.kotlinapicall.base.BaseActivity
import com.example.orgware.kotlinemap.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.util.Property
import com.example.orgware.kotlinapicall.data.respones.polyline.MapResponse
import com.example.orgware.kotlinemap.ui.polyline.spherical
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import java.util.ArrayList


class PolylineActivity : BaseActivity(), Polylineview, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var service: LocationManager? = null
    private var enabled: Boolean? = false
    private var mLocationRequest: LocationRequest? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null
    private lateinit var mMap: GoogleMap
    private var REQUEST_LOCATION_CODE = 101
    private var polylinePresenter: PolylinePresenter? = null
    private val routesItems = ArrayList<LatLng>()
    private val startlist = ArrayList<LatLng>()
    private val startLocations = ArrayList<LatLng>()
    private val endLocations = ArrayList<LatLng>()
    private val kDegreesToRadians = Math.PI / 180.0
    private val kRadiansToDegrees = 180.0 / Math.PI

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_map)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        polylinePresenter = PolylinePresenter()
        polylinePresenter!!.setView(this)
        polylinePresenter!!.sample()

    }

    override fun onLocationChanged(location: Location?) {
//        mLastLocation = location
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker!!.remove()
//        }
//
//        val latLng = LatLng(location!!.latitude, location.longitude)
//        val markerOptions = MarkerOptions()
//        markerOptions.position(latLng)
//        markerOptions.title(latLng.toString())
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
//        mCurrLocationMarker = mMap.addMarker(markerOptions)
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (!enabled!!) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showRetry() {
    }

    override fun hideRetry() {
    }

    override fun showError(message: String) {
    }

    override fun context(): Context {
        return this
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                buildGoogleApiClient()
                mMap.isMyLocationEnabled = true
            } else {
                checkLocationPermission()
            }
        } else {
//            buildGoogleApiClient()
            mMap.isMyLocationEnabled = true
        }
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






    override fun onSucess(mapResponse: MapResponse) {
        for (i in mapResponse.routes!!.indices) {
            for (j in mapResponse.routes!!.get(i)!!.legs!!.indices) {
                for (m in mapResponse.routes!!.get(i)!!.legs!!.get(j)!!.steps!!.indices) {
                    routesItems.addAll(decodePoly(mapResponse.routes!!.get(i)!!.legs!!.get(j)!!.steps!!.get(m)!!.polyline!!.points!!))
//                    val lat = mapResponse.routes.get(i)!!.legs!!.get(j)!!.steps!!.get(m)!!.startLocation!!.lat
//                    val lon = mapResponse.routes.get(i)!!.legs!!.get(j)!!.steps!!.get(m)!!.startLocation!!.lng
//                    startLocations.add(LatLng(lat!!, lon!!))
//                    val lati = mapResponse.routes.get(i)!!.legs!!.get(j)!!.steps!!.get(m)!!.polyline!!.pointslat
//                    val long = mapResponse.routes.get(i)!!.legs!!.get(j)!!.steps!!.get(m)!!.endLocation!!.lng
//                    endLocations.add(LatLng(lati!!, long!!))
                }
            }
        }
//            for (k in 0..startLocations!!.size) {
//            val startlat = startLocations.get(k).latitude
//            val startlon = startLocations.get(k).longitude
//            startlist.add(LatLng(startlat!!, startlon!!))
//
//            val endlat = endLocations.get(k).latitude
//            val endlon = endLocations.get(k).longitude
//            startlist.add(LatLng(endlat!!, endlon!!))
//        }
//            for (l in 0.. startLocations.size) {
//                val start = LatLng(startLocations[l].longitude, startLocations[l].latitude)
//                startlist.add(start)
//                val ends = LatLng(endLocations[l].longitude, endLocations[l].latitude)
//                startlist.add(ends)
//            }


                val options = MarkerOptions().position(routesItems[0]).icon(BitmapDescriptorFactory.fromResource(R.drawable.hatch))
                val m = mMap.addMarker(options)
                m.setAnchor(0.5f, 0.5f)

                animateMarker(m, routesItems)

            }


    override fun onFailure(Messages: String) {
        Toast.makeText(this@PolylineActivity, ""+Messages, Toast.LENGTH_SHORT).show()

    }




    fun getAngle(curretLatLng: LatLng, toLatLng: LatLng): Double {

        val fromLong = curretLatLng.longitude * kDegreesToRadians
        val toLong = toLatLng.longitude * kDegreesToRadians

        val fromLat = curretLatLng.latitude * kDegreesToRadians
        val toLat = toLatLng.latitude * kDegreesToRadians

        val dlon = toLong - fromLong
        val y = Math.sin(dlon) * Math.cos(toLat)
        val x = Math.cos(fromLat) * Math.sin(toLat) - Math.sin(fromLat) * Math.cos(toLat) * Math.cos(dlon)

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

    private fun animateMarker(m: Marker, mList: MutableList<LatLng>) {
        val evaluator = TypeEvaluator<LatLng> { fraction, startValue: LatLng, endValue ->
            val angle = this@PolylineActivity.getAngle(startValue, endValue)
            m.rotation = angle.toFloat()
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
                    animateMarker(m, mList)
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
        mMap.addPolyline(lineOptions)
    }

    private fun decodePoly(encoded: String): List<LatLng> {

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

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
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
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap.isMyLocationEnabled = true
                    }

                } else {
                    Toast.makeText(this@PolylineActivity, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}