package com.example.orgware.kotlinemap.helper.runtime

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class PermissionHelper : ActivityCompat.OnRequestPermissionsResultCallback {
    private var requestCode: Int
    private var fragment: FragmentActivity? = null
    private var activity: AppCompatActivity? = null
    private var context: Context?
    private var listener: PermissionListener
    private var permissionArray: Array<String>?

    constructor(requestCode: Int, fragment: FragmentActivity?, listener: PermissionListener) {
        this.requestCode = requestCode
        this.fragment = fragment
        this.listener = listener
        context = getContext()
        permissionArray = getPermissionArray(requestCode)
    }

    constructor(requestCode: Int, activity: AppCompatActivity?, listener: PermissionListener) {
        this.requestCode = requestCode
        this.activity = activity
        this.listener = listener
        context = getContext()
        permissionArray = getPermissionArray(requestCode)
    }

    private fun getContext(): Context? {
        if (activity != null) {
            return activity
        } else if (fragment != null) {
            return fragment!!
        }
        return null
    }

    private fun getPermissionArray(requestCode: Int): Array<String>? {
        when (requestCode) {
            141 ->
                if (Build.VERSION.SDK_INT >= 30) {
                    return STORAGE_PERMISSION_NEW
                } else {
                    return STORAGE_PERMISSION
                }
        }
        return null
    }

    fun isPermissionGranted(requestCode: Int): Boolean {
        val permissionArray = getPermissionArray(requestCode)
        var allPermissionsGranted = true
        var i = 0
        val mPermissionLength = permissionArray!!.size
        while (i < mPermissionLength) {
            val permission = permissionArray[i]
            if (ActivityCompat.checkSelfPermission(context!!, permission)
                    != PackageManager.PERMISSION_GRANTED
            ) {
                allPermissionsGranted = false
                break
            }
            i++
        }
        return allPermissionsGranted
    }

    fun openPermissionDialog() {
        if (permissionArray == null) {
            return
        }
        var allPermissionsGranted = true
        var i = 0
        val mPermissionLength = permissionArray!!.size
        while (i < mPermissionLength) {
            val permission = permissionArray!![i]
            if (ActivityCompat.checkSelfPermission(context!!, permission)
                    != PackageManager.PERMISSION_GRANTED
            ) {
                allPermissionsGranted = false
                break
            }
            i++
        }
        if (!allPermissionsGranted) {
            if (activity != null) {
                ActivityCompat.requestPermissions(activity!!, permissionArray!!, requestCode)
            } else if (fragment != null) {
                ActivityCompat.requestPermissions(fragment!!, permissionArray!!, requestCode)
            }
        } else {
            listener.onPermissionGranted()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode == this.requestCode) {
            var allPermissionGranted = true
            if (grantResults.size == permissions.size) {
                for (i in permissions.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                        allPermissionGranted = false;
                        break
                    }
                }
            } else {
                allPermissionGranted = false
            }
            if (allPermissionGranted) {
                listener.onPermissionGranted()
            } else {
                listener.onPermissionDenied()
            }
        }
    }

    interface PermissionListener {
        fun onPermissionGranted()
        fun onPermissionDenied()
    }

    companion object {
        private val STORAGE_PERMISSION = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        private val STORAGE_PERMISSION_NEW = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
