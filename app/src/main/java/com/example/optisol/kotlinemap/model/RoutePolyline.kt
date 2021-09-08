package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

class RoutePolyline : Parcelable {
    @SerializedName("points")
    var rawPointList: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        rawPointList = `in`.readString()
    }

    val pointList: List<LatLng>
        get() = PolyUtil.decode(rawPointList)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(rawPointList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<RoutePolyline?> =
            object : Parcelable.Creator<RoutePolyline?> {
                override fun createFromParcel(`in`: Parcel): RoutePolyline? {
                    return RoutePolyline(`in`)
                }

                override fun newArray(size: Int): Array<RoutePolyline?> {
                    return arrayOfNulls(size)
                }
            }
    }
}