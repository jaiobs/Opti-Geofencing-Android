package com.optisol.optigeofencingandroid.core

import android.content.Context
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.optisol.optigeofencingandroid.apiservices.DirectionService
import com.optisol.optigeofencingandroid.config.Constants
import com.optisol.optigeofencingandroid.config.DirectionConverter
import com.optisol.optigeofencingandroid.model.Direction
import com.optisol.optigeofencingandroid.model.Leg
import com.optisol.optigeofencingandroid.model.Route
import com.optisol.optigeofencingandroid.model.Step
import com.optisol.optigeofencingandroid.ui.directions.GetDirectionsAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class DirectionsApi : GetDirectionsAPI {

    var googleMap: GoogleMap? = null
    private var fromLatLng: LatLng? = null
    private var toLatLng: LatLng? = null
    var legsList: ArrayList<Leg>? = null
    var polylineList: ArrayList<Polyline>? = null
    var locationManager: LocationManager? = null
    private var selectedPolyLine: Polyline? = null
    private var stepList: ArrayList<Step>? = null
    private var service: DirectionService? = null
    private var selectedIndex = -1

    override fun getDirectionSuccess(direction: Direction, context: Context, map: GoogleMap?) {
        this.googleMap = map
        val directBuilder = StringBuilder()
        for (i in 0 until direction.routeList?.get(0)?.legList?.size!!) {
            directBuilder.append(
                Gson().toJson(
                    direction.routeList?.get(0)?.legList?.get(i)
                )
            )
        }
        val direct: String = Gson().toJson(direction)
        val maxLogSize = 3000
        for (i in 0..direct.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > direct.length) direct.length else end
        }
        legsList = ArrayList<Leg>()
        val polylineOptionsList: ArrayList<PolylineOptions> = ArrayList()
        polylineList = ArrayList()
        if (direction.isOK) {
            if (direction.routeList != null) {
                for (i in 0 until direction.routeList?.size!!) {
                    legsList!!.addAll(direction.routeList!![i].legList!!)
                }
            }
            for (i in legsList!!.indices) {
                var polylineOptions: PolylineOptions
                val directionPositionList: ArrayList<LatLng> = legsList!![i].directionPoint
                polylineOptions = if (i == 0) {
                    DirectionConverter.createPolyline(context, directionPositionList, 5, Color.RED)
                } else {
                    DirectionConverter.createPolyline(context, directionPositionList, 5, Color.GRAY)
                }
                polylineOptionsList.add(polylineOptions)
            }
            //progressBar.setVisibility(View.GONE)

            //  polylineOptionsList.get( 0 ).clickable( true );
            //  polylineList.add( googleMap.addPolyline( polylineOptionsList.get( 0 ) ));
            setCameraWithCoordinationBounds(direction.routeList!![0])
            for (i in polylineOptionsList.indices) {
                val polylineOptions: PolylineOptions = polylineOptionsList[i]
                polylineOptions.clickable(true)
                if (i == 0) {
                    polylineOptions.zIndex(2f)
                } else {
                    polylineOptions.zIndex(1f)
                }
                if (i == 0) {
                    selectedPolyLine = googleMap!!.addPolyline(polylineOptions)
                    polylineList!!.add(selectedPolyLine!!)
                } else {
                    (polylineList as ArrayList<Polyline>).add(
                        googleMap!!.addPolyline(
                            polylineOptions
                        )
                    )
                }
            }
            stepList!!.clear()
            legsList!![0].stepList?.let { stepList!!.addAll(it) }
            googleMap!!.addMarker(MarkerOptions().position(fromLatLng!!))
            googleMap!!.addMarker(MarkerOptions().position(toLatLng!!))
            /*llMapParent.setVisibility(View.VISIBLE)
            btnStart.setVisibility(View.VISIBLE)
            tvFrom.setText(legsList.get(0).startAddress)
            tvTo.setText(legsList.get(0).endAddress)
            tvDistance.setText(legsList.get(0).duration!!.text + "(" + legsList.get(0).distance!!.text + ")")*/
        }
    }


    private fun setCameraWithCoordinationBounds(route: Route) {
        val southwest: LatLng = route.bound?.southwestCoordination?.coordination!!
        val northeast: LatLng = route.bound?.northeastCoordination?.coordination!!
        val bounds = LatLngBounds(southwest, northeast)
        googleMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    override fun setGoogleMapScreen(context: Context, map: GoogleMap?) {
        this.googleMap = map
        fromLatLng = LatLng(9.9252, 78.1198)
        toLatLng = LatLng(13.0827, 80.2707)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(fromLatLng, 12f));

        // Origin of route
        val startFrom = fromLatLng!!.latitude.toString() + "," + fromLatLng!!.longitude
        // Destination of route
        val endsTo = toLatLng!!.latitude.toString() + "," + toLatLng!!.longitude
        // Sensor enabled
        val sensor = "sensor=false" //no longer required
        val mode = "driving"
        val alternatives = true
        val getDirection: Call<Direction?>? = service?.getDirection(
            startFrom,
            endsTo, mode, alternatives, Constants.MAP_API_KEY
        )
        getDirection?.enqueue(object : Callback<Direction?> {
            override fun onResponse(call: Call<Direction?>?, response: Response<Direction?>) {
                response.body()?.let {
                    getDirectionSuccess(
                        it, context,
                        map
                    )
                }
            }

            override fun onFailure(call: Call<Direction?>, t: Throwable) {
                Log.e("@@@", "get direction failure -> " + t.localizedMessage)
            }
        })

        googleMap?.setOnPolylineClickListener { polyline ->
            polyline.color = Color.RED
            polyline.zIndex = 2f
            selectedPolyLine = polyline
            var index = -1
            for (i in polylineList!!.indices) {
                if (polylineList!![i] == polyline) {
                    index = i
                } else {
                    polylineList!![i].color = Color.GRAY
                    polylineList!![i].zIndex = 1f
                }
            }
            selectedIndex = index
            stepList?.clear()
            legsList!![index].stepList?.let { stepList?.addAll(it) }
            //tvDistance.setText(legsList!![index].duration!!.text + "(" + legsList!![index].distance!!.text + ")")
        }
    }
}