package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.example.optisol.kotlinemap.config.RequestResult
import com.optisol.optigeofencingandroid.model.Direction
import com.optisol.optigeofencingandroid.model.GeocodedWaypoint
import com.optisol.optigeofencingandroid.model.Route


class Direction : Parcelable {
    @SerializedName("geocoded_waypoints")
    var geocodedWaypointList: List<GeocodedWaypoint>? = null

    @SerializedName("routes")
    var routeList: List<Route>? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("error_message")
    var errorMessage: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        status = `in`.readString()
        errorMessage = `in`.readString()
    }

    val isOK: Boolean
        get() = RequestResult.OK == status

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(status)
        dest.writeString(errorMessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Direction?> = object : Parcelable.Creator<Direction?> {
            override fun createFromParcel(`in`: Parcel): Direction? {
                return com.optisol.optigeofencingandroid.model.Direction(`in`)
            }

            override fun newArray(size: Int): Array<Direction?> {
                return arrayOfNulls(size)
            }
        }
    }
}