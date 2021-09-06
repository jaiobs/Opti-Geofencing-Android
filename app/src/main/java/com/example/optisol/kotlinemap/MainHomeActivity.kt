package com.example.optisol.kotlinemap

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.optisol.kotlinemap.base.BaseActivity
import com.example.optisol.kotlinemap.helper.location.LocationHelper
import com.example.optisol.kotlinemap.helper.location.LocationManagers
import com.example.optisol.kotlinemap.helper.runtime.PermissionHelper
import com.example.optisol.kotlinemap.ui.geomap.GeoActivity
import com.example.optisol.kotlinemap.ui.currentlocation.CurrentLocationActivity
import com.example.optisol.kotlinemap.ui.polyline.PolylineActivity
import com.example.optisol.kotlinemap.ui.geofence.GeofenceActivity
import com.example.optisol.kotlinemap.ui.custommarker.CustomMarkerActivity
import com.example.optisol.kotlinemap.ui.distancecalculate.DistanceCalculationActivity
import com.example.optisol.kotlinemap.utils.UiUtils
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainHomeActivity : BaseActivity(), View.OnClickListener, LocationManagers,
    PermissionHelper.PermissionListener {
    private var locationHelper: LocationHelper? = null
    private var permissionHelper: PermissionHelper? = null
    private val REQUEST_CHECK_SETTINGS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionHelper = PermissionHelper(141, this, this)
        permissionHelper?.openPermissionDialog()
        initView()
    }

    private fun initView() {
        btn_current_location.setOnClickListener(this)
        btn_custom_marker.setOnClickListener(this)
        btn_geo_fencing.setOnClickListener(this)
        btn_polyline.setOnClickListener(this)
        btn_multi_marker.setOnClickListener(this)
        btn_distance_calculate.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_current_location -> {
                val intent = Intent(this, CurrentLocationActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_custom_marker -> {
                val intent = Intent(this, CustomMarkerActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_geo_fencing -> {
                val intent = Intent(this, GeofenceActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_polyline -> {
                val intent = Intent(this, PolylineActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_multi_marker -> {
                val intent = Intent(this, GeoActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_distance_calculate -> {
                val intent = Intent(this, DistanceCalculationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        onRequestPermissionsResult(requestCode, grantResults)
        permissionHelper!!.onRequestPermissionsResult(
            requestCode,
            permissions as Array<String>,
            grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (locationHelper != null) {
            locationHelper!!.onActivityResult(requestCode, resultCode, data)
        }
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> enableGPS()
                Activity.RESULT_CANCELED ->/*enableGPS()*/ //keep asking if imp or do whatever
                    Log.e("enabled", "success")
            }
        }
    }

    protected fun enableGPS() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000 * 10.toLong()
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener(this) { locationSettingsResponse ->
        }
        task.addOnFailureListener(this) { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(this, sendEx.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun getLastKnownLocation(lastLocation: Location?) {
        UiUtils.showToast(this, lastLocation.toString())
    }

    override fun onLocationChanged(location: Location?) {
    }

    override fun onPermissionGranted() {
        locationHelper = LocationHelper(this, this)
        locationHelper!!.enableLocationSettings()
    }

    override fun onPermissionDenied() {
        permissionHelper?.openPermissionDialog()
    }
}