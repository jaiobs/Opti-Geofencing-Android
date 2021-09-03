package com.example.orgware.kotlinemap

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.orgware.kotlinemap.base.BaseActivity
import com.example.orgware.kotlinemap.helper.location.LocationHelper
import com.example.orgware.kotlinemap.helper.location.LocationManagers
import com.example.orgware.kotlinemap.helper.runtime.PermissionHelper
import com.example.orgware.kotlinemap.ui.four.FourActivity
import com.example.orgware.kotlinemap.ui.geomap.GeoActivity
import com.example.orgware.kotlinemap.ui.one.OneActivity
import com.example.orgware.kotlinemap.ui.tthree.ThreeActivity
import com.example.orgware.kotlinemap.ui.two.TwoActivity
import com.example.orgware.kotlinemap.utils.UiUtils
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
        option.setOnClickListener(this)
        option1.setOnClickListener(this)
        option2.setOnClickListener(this)
        option3.setOnClickListener(this)
        option4.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.option -> {
                val intent = Intent(this, OneActivity::class.java)
                startActivity(intent)
            }
            R.id.option1 -> {
                val intent = Intent(this, TwoActivity::class.java)
                startActivity(intent)
            }
            R.id.option2 -> {
                val intent = Intent(this, ThreeActivity::class.java)
                startActivity(intent)
            }
            R.id.option3 -> {
                val intent = Intent(this, FourActivity::class.java)
                startActivity(intent)
            }
            R.id.option4 -> {
                val intent = Intent(this, GeoActivity::class.java)
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