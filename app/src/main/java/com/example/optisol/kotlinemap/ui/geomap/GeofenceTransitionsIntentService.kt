package com.example.optisol.kotlinemap.ui.geomap

import android.app.IntentService
import android.content.Intent

class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIntentService") {


    override fun onHandleIntent(intent: Intent?) {
       /* val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "error")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
        geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Toast.makeText(applicationContext,"Enter",Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext,"Error",Toast.LENGTH_LONG).show()
        }*/
    }
}
