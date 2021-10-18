package com.example.optisol.kotlinemap.ui.directions

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.optisol.optigeofencingandroid.config.Constants
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.optisol.optigeofencingandroid.apiservices.DirectionService
import com.optisol.optigeofencingandroid.model.Direction
import com.optisol.optigeofencingandroid.model.Leg
import com.optisol.optigeofencingandroid.model.Step
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson
import com.google.maps.android.SphericalUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.android.gms.maps.model.MarkerOptions
import java.lang.StringBuilder
import com.google.android.gms.maps.model.PolylineOptions
import com.optisol.optigeofencingandroid.config.DirectionConverter
import com.optisol.optigeofencingandroid.model.Route
import com.google.android.gms.maps.model.LatLngBounds
import androidx.core.app.ActivityCompat

import android.content.DialogInterface
import android.app.AlertDialog

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat
import android.view.animation.LinearInterpolator

import com.google.android.gms.maps.model.BitmapDescriptorFactory

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point


import android.os.Handler
import android.os.SystemClock
import android.view.animation.Interpolator
import com.example.optisol.kotlinemap.R
import com.optisol.optigeofencingandroid.base.BaseActivity
import com.example.optisol.kotlinemap.config.GoogleDirectionConfiguration

import com.google.android.gms.maps.model.BitmapDescriptor
import com.optisol.optigeofencingandroid.OptiGeoFence
import com.optisol.optigeofencingandroid.ui.directions.GetDirectionsAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.*


class DirectionActivity : BaseActivity(), OnMapReadyCallback {
    var googleMap: GoogleMap? = null
    private var fromLatLng: LatLng? = null
    private var toLatLng: LatLng? = null
    private var service: DirectionService? = null
    var legsList: ArrayList<Leg>? = null
    var polylineList: ArrayList<Polyline>? = null
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    private var selectedIndex = -1
    private var selectedPolyLine: Polyline? = null
    private var stepList: ArrayList<Step>? = null
    var provider: String? = null
    var trackMarker: Marker? = null
    private lateinit var getDirectionsAPI: GetDirectionsAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction)

        getDirectionsAPI = OptiGeoFence.getDirectionsApi()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //madurai to chennai
        fromLatLng = LatLng(9.9252, 78.1198)
        toLatLng = LatLng(13.0827, 80.2707)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        provider = LocationManager.GPS_PROVIDER;
        locationListener = LocationListener {
            if (stepList != null && stepList!!.isNotEmpty()) {
                val originLat: Double = stepList!![0].startLocation?.latitude!!
                val originLon: Double = stepList!![0].startLocation?.longitude!!
                val destinyLat: Double = stepList!![0].endLocation?.latitude!!
                val destinyLon: Double = stepList!![0].endLocation?.longitude!!
                val rotationDegrees =
                    Math.toDegrees(atan2(originLat - destinyLat, originLon - destinyLon))
                //method three working fine
                val rotation = SphericalUtil.computeHeading(
                    LatLng(originLat, originLon),
                    LatLng(destinyLat, destinyLon)
                )

                trackMarker!!.rotation = rotation.toFloat()
                animateMarker(trackMarker!!, LatLng(originLat, originLon), false)

                stepList!!.removeAt(0)
            } else {
                animateMarker(trackMarker!!, toLatLng!!, true);
            }
        }
        checkLocationPermission()
        stepList = ArrayList()
        service = getDirectionService()
    }

    override fun onMapReady(map: GoogleMap?) {
        this.googleMap = map;
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL;

        getDirectionsAPI.setGoogleMapScreen(this, map)
    }

//    private fun setGoogleMapScreen(map: GoogleMap?) {
//        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(fromLatLng, 12f));
//
//        // Origin of route
//        val startFrom = fromLatLng!!.latitude.toString() + "," + fromLatLng!!.longitude
//        // Destination of route
//        val endsTo = toLatLng!!.latitude.toString() + "," + toLatLng!!.longitude
//        // Sensor enabled
//        val sensor = "sensor=false" //no longer required
//        val mode = "driving"
//        val alternatives = true
//        val getDirection: Call<Direction?>? = service?.getDirection(
//            startFrom,
//            endsTo, mode, alternatives, Constants.MAP_API_KEY
//        )
//        getDirection?.enqueue(object : Callback<Direction?> {
//            override fun onResponse(call: Call<Direction?>?, response: Response<Direction?>) {
//                response.body()?.let {
//                    getDirectionsAPI.getDirectionSuccess(
//                        it, this@DirectionActivity,
//                        map
//                    )
//                }
//            }
//
//            override fun onFailure(call: Call<Direction?>, t: Throwable) {
//                Log.e("@@@", "get direction failure -> " + t.localizedMessage)
//            }
//        })
//
//        googleMap?.setOnPolylineClickListener { polyline ->
//            polyline.color = Color.RED
//            polyline.zIndex = 2f
//            selectedPolyLine = polyline
//            var index = -1
//            for (i in polylineList!!.indices) {
//                if (polylineList!![i] == polyline) {
//                    index = i
//                } else {
//                    polylineList!![i].color = Color.GRAY
//                    polylineList!![i].zIndex = 1f
//                }
//            }
//            selectedIndex = index
//            stepList?.clear()
//            legsList!![index].stepList?.let { stepList?.addAll(it) }
//            //tvDistance.setText(legsList!![index].duration!!.text + "(" + legsList!![index].distance!!.text + ")")
//        }
//    }

    fun getDirectionService(): DirectionService? {
        if (service == null) {
            val retrofit: Retrofit = Retrofit.Builder()
                .client(getClient())
                .baseUrl(Constants.MAPS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            service = retrofit.create(DirectionService::class.java)
        }
        return service
    }

    private fun getClient(): OkHttpClient {
        val client: OkHttpClient? = GoogleDirectionConfiguration.getInstance().getCustomClient()
        return client ?: createDefaultClient()
    }

    private fun createDefaultClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        //if (GoogleDirectionConfiguration.getInstance().isLogEnabled()) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        // }
        return builder.build()
    }

//    private fun getDirectionSuccess(
//        direction: Direction,
//        directionActivity: DirectionActivity,
//        map: GoogleMap?
//    ) {
//        val directBuilder = StringBuilder()
//        for (i in 0 until direction.routeList?.get(0)?.legList?.size!!) {
//            directBuilder.append(
//                Gson().toJson(
//                    direction.routeList?.get(0)?.legList?.get(i)
//                )
//            )
//        }
//        val direct: String = Gson().toJson(direction)
//        val maxLogSize = 3000
//        for (i in 0..direct.length / maxLogSize) {
//            val start = i * maxLogSize
//            var end = (i + 1) * maxLogSize
//            end = if (end > direct.length) direct.length else end
//        }
//        legsList = ArrayList<Leg>()
//        val polylineOptionsList: ArrayList<PolylineOptions> = ArrayList()
//        polylineList = ArrayList()
//        if (direction.isOK) {
//            if (direction.routeList != null) {
//                for (i in 0 until direction.routeList?.size!!) {
//                    legsList!!.addAll(direction.routeList!![i].legList!!)
//                }
//            }
//            for (i in legsList!!.indices) {
//                var polylineOptions: PolylineOptions
//                val directionPositionList: ArrayList<LatLng> = legsList!![i].directionPoint
//                polylineOptions = if (i == 0) {
//                    DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED)
//                } else {
//                    DirectionConverter.createPolyline(this, directionPositionList, 5, Color.GRAY)
//                }
//                polylineOptionsList.add(polylineOptions)
//            }
//            //progressBar.setVisibility(View.GONE)
//
//            //  polylineOptionsList.get( 0 ).clickable( true );
//            //  polylineList.add( googleMap.addPolyline( polylineOptionsList.get( 0 ) ));
//            setCameraWithCoordinationBounds(direction.routeList!![0])
//            for (i in polylineOptionsList.indices) {
//                val polylineOptions: PolylineOptions = polylineOptionsList[i]
//                polylineOptions.clickable(true)
//                if (i == 0) {
//                    polylineOptions.zIndex(2f)
//                } else {
//                    polylineOptions.zIndex(1f)
//                }
//                if (i == 0) {
//                    selectedPolyLine = googleMap!!.addPolyline(polylineOptions)
//                    polylineList!!.add(selectedPolyLine!!)
//                } else {
//                    (polylineList as ArrayList<Polyline>).add(
//                        googleMap!!.addPolyline(
//                            polylineOptions
//                        )
//                    )
//                }
//            }
//            stepList!!.clear()
//            legsList!![0].stepList?.let { stepList!!.addAll(it) }
//            googleMap!!.addMarker(MarkerOptions().position(fromLatLng!!))
//            googleMap!!.addMarker(MarkerOptions().position(toLatLng!!))
//            /*llMapParent.setVisibility(View.VISIBLE)
//            btnStart.setVisibility(View.VISIBLE)
//            tvFrom.setText(legsList.get(0).startAddress)
//            tvTo.setText(legsList.get(0).endAddress)
//            tvDistance.setText(legsList.get(0).duration!!.text + "(" + legsList.get(0).distance!!.text + ")")*/
//        }
//    }

//    private fun setCameraWithCoordinationBounds(route: Route) {
//        val southwest: LatLng = route.bound?.southwestCoordination?.coordination!!
//        val northeast: LatLng = route.bound?.northeastCoordination?.coordination!!
//        val bounds = LatLngBounds(southwest, northeast)
//        googleMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
//    }

    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Access Location Permission")
                    .setMessage("Give Permission to access location")
                    .setPositiveButton(R.string.ok,
                        DialogInterface.OnClickListener { dialogInterface, i -> //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                this@DirectionActivity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                Constants.LOCATION_PERMISSION_REQUEST_INT
                            )
                        })
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Constants.LOCATION_PERMISSION_REQUEST_INT
                )
            }
            false
        } else {
            true
        }
    }

    override fun onPause() {
        super.onPause()
        /*if ( ContextCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_FINE_LOCATION )
            == PackageManager.PERMISSION_GRANTED ) {
            locationListener?.let { locationManager.removeUpdates(it) };
        }*/
    }

    private fun removePolyLine() {
        for (polyline in polylineList!!) {
            if (polyline !== selectedPolyLine) {
                polyline.remove()
            }
        }
    }

    private fun trackRoute() {
        /*btnStart.setVisibility(View.GONE)
        llMapParent.setVisibility(View.GONE)*/
        if (checkLocationPermission()) {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10f,
                locationListener!!
            )
        }

        //edited 28 march 2019
        val lat1 = selectedPolyLine!!.points[0].latitude
        val lng1 = selectedPolyLine!!.points[0].longitude

        // destination
        val lat2 = selectedPolyLine!!.points[1].latitude
        val lng2 = selectedPolyLine!!.points[1].longitude

        //midpoint
        val lat = (lat1 + lat2) / 2
        val lng = (lng1 + lng2) / 2
        val dLon = lng2 - lng1
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
        val brng = Math.toDegrees(atan2(y, x))
        val trackerMarker = MarkerOptions().position(selectedPolyLine!!.points[10])
        trackerMarker.anchor(0.5f, 0.5f)
        trackerMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_bw))
        trackerMarker.rotation(brng.toFloat())
        trackerMarker.flat(true)
        trackMarker = googleMap!!.addMarker(trackerMarker)
    }

    /** calculates the distance between two locations in MILES  */
    private fun distance(
        lat1: Double,
        lng1: Double,
        lat2: Double,
        lng2: Double
    ): Double {
        val earthRadius = 3958.75 // in miles, change to 6371 for kilometer output
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val sindLat = sin(dLat / 2)
        val sindLng = sin(dLng / 2)
        val a = sindLat.pow(2.0) + (sindLng.pow(2.0)
                * cos(Math.toRadians(lat1)) * cos(
            Math.toRadians(
                lat2
            )
        ))
        val c =
            2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c // output distance, in MILES
    }

    fun animateMarker(
        marker: Marker, toPosition: LatLng,
        hideMarker: Boolean
    ) {
        val handler = Handler()
        val start: Long = SystemClock.uptimeMillis()
        val proj = googleMap!!.projection
        val startPoint: Point = proj.toScreenLocation(marker.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration: Long = 500
        val interpolator: Interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed: Long = SystemClock.uptimeMillis() - start
                val t: Float = interpolator.getInterpolation(
                    elapsed.toFloat()
                            / duration
                )
                val lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude
                val lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude
                marker.position = LatLng(lat, lng)
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    marker.isVisible = !hideMarker
                }
            }
        })
    }

    /**
     * Return a BitmapDescriptor of an arrow endcap icon for the passed color.
     *
     * @param context - a valid context object
     * @param color   - the color to make the arrow icon
     * @return BitmapDescriptor - the new endcap icon
     */
    fun getEndCapIcon(context: Context?, color: Int): BitmapDescriptor? {

        // mipmap icon - white arrow, pointing up, with point at center of image
        // you will want to create:  mdpi=24x24, hdpi=36x36, xhdpi=48x48, xxhdpi=72x72, xxxhdpi=96x96
        val drawable = ContextCompat.getDrawable(context!!, R.drawable.back_arrow_map)

        // set the bounds to the whole image (may not be necessary ...)
        drawable!!.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

        // overlay (multiply) your color over the white icon
        // drawable.setColorFilter( color, PorterDuff.Mode.MULTIPLY);

        // create a bitmap from the drawable
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )

        // render the bitmap on a blank canvas
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)

        // create a BitmapDescriptor from the new bitmap
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    //get bearing for auto rotating arrow marker
    private fun GetBearing(from: LatLng, to: LatLng): Double {
        val degreesPerRadian = 180.0 / Math.PI
        val lat1 = from.latitude * Math.PI / 180.0
        val lon1 = from.longitude * Math.PI / 180.0
        val lat2 = to.latitude * Math.PI / 180.0
        val lon2 = to.longitude * Math.PI / 180.0

        // Compute the angle.
        var angle = -atan2(
            sin(lon1 - lon2) * cos(lat2),
            cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lon1 - lon2)
        )
        if (angle < 0.0) angle += Math.PI * 2.0

        // And convert result to degrees.
        angle *= degreesPerRadian
        return angle
    }

}