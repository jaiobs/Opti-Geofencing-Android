package com.optisol.optigeofencingandroid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManager
import com.optisol.optigeofencingandroid.respones.locationmanager.LocationManagerImpl


/**
 * map fragment --
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    private var rootView: View? = null
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private var latLng: LatLng? = null
    val locationManager: LocationManager =
        LocationManagerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map, container, false)
        rootView?.let { initView(it, savedInstanceState) }
        return rootView
    }

    private fun initView(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.mapFragment)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.clear()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        googleMap?.isMyLocationEnabled = true
        this.googleMap = googleMap
        startLocationMonitor()
        Log.e("@@@","onMapReady")
    }

    private fun startLocationMonitor() {
         locationManager.getLastKnownPosition(
                 activity = requireActivity(),
                 onLastLocationFound = {
                     Log.d("Location", "Last Location - found - lat: " + it.latitude + " lng:" + it.longitude)
                     val cameraPosition = CameraPosition.Builder()
                             .target(LatLng(it.latitude, it.longitude))
                             .zoom(12f)
                             .build()
                     googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                     setMarker( LatLng(it.latitude, it.longitude))
                     latLng = LatLng(it.latitude, it.longitude)
                 },
                 onNoLocationFound = {
                     Log.d("Location", "Last Location - no location found")
                 })
    }

    fun setMarker( latLng: LatLng) {
        googleMap?.addMarker(MarkerOptions().anchor(0.5f, 0.5f).position(latLng))
    }
}