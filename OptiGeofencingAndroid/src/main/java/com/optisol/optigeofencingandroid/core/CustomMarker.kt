package com.optisol.optigeofencingandroid.core

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.optisol.optigeofencingandroid.R
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManager
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManagerImpl
import com.optisol.optigeofencingandroid.ui.custommarker.CustomMarkerListener
import de.hdodenhof.circleimageview.CircleImageView

class CustomMarker : CustomMarkerListener {

    private var mMap: GoogleMap? = null
    val locationManager: LocationManager =
        LocationManagerImpl()

    override fun createMarker(
        context: Context,
        imageUrl: String,
        name: String,
        googleMap: GoogleMap?
    ): Bitmap? {
        this.mMap = googleMap
        val marker: View =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.custom_marker_layout,
                null
            )
        val markerImage = marker.findViewById<View>(R.id.user_dp) as CircleImageView
        Glide.with(context)
            .load(imageUrl)
            .thumbnail(0.5f)
            .error(R.drawable.ic_circle_userimage)
            .placeholder(R.drawable.ic_circle_userimage)
            .into(markerImage)

        mMap?.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener {
            val selectedUserId = it?.title
            val selectedLatLng = it?.snippet
            if (selectedLatLng == null) {
//                showToast("empty")
            } else {
                val namesList: List<String> = selectedLatLng.split(",")
                val lat1 = namesList[0]
                val lon1 = namesList[1]
                val latLng = LatLng(lat1.toDouble(), lon1.toDouble())
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                mMap?.animateCamera(cameraUpdate)
            }
            true
        })

        val txt_name = marker.findViewById<View>(R.id.name) as TextView
        txt_name.text = name
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)
        return bitmap
    }
}