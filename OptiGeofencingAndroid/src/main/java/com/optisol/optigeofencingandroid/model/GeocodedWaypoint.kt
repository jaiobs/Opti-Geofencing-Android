package com.optisol.optigeofencingandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel

class GeocodedWaypoint : Parcelable {
    @SerializedName("geocoder_status")
    var status: String? = null

    @SerializedName("place_id")
    var placeId: String? = null

    @SerializedName("types")
    var typeList: List<String>? = null

    constructor() {}
    constructor(`in`: Parcel) {
        status = `in`.readString()
        placeId = `in`.readString()
        typeList = `in`.createStringArrayList()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(status)
        dest.writeString(placeId)
        dest.writeStringList(typeList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<GeocodedWaypoint?> =
            object : Parcelable.Creator<GeocodedWaypoint?> {
                override fun createFromParcel(`in`: Parcel): GeocodedWaypoint? {
                    return GeocodedWaypoint(`in`)
                }

                override fun newArray(size: Int): Array<GeocodedWaypoint?> {
                    return arrayOfNulls(size)
                }
            }
    }
}