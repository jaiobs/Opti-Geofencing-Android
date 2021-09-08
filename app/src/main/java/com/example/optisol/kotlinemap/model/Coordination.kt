package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.google.android.gms.maps.model.LatLng


class Coordination : Parcelable {
    @SerializedName("lat")
    var latitude = 0.0

    @SerializedName("lng")
    var longitude = 0.0

    constructor() {}
    protected constructor(`in`: Parcel) {
        latitude = `in`.readDouble()
        longitude = `in`.readDouble()
    }

    val coordination: LatLng
        get() = LatLng(latitude, longitude)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(latitude)
        dest.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Coordination?> = object : Parcelable.Creator<Coordination?> {
            override fun createFromParcel(`in`: Parcel): Coordination? {
                return Coordination(`in`)
            }

            override fun newArray(size: Int): Array<Coordination?> {
                return arrayOfNulls(size)
            }
        }
    }
}