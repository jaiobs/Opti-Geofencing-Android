package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel


class Waypoint : Parcelable {
    @SerializedName("location")
    var location: Coordination? = null

    @SerializedName("step_index")
    var index = 0

    @SerializedName("step_interpolation")
    var interpolation = 0.0

    constructor() {}
    protected constructor(`in`: Parcel) {
        location = `in`.readParcelable(Coordination::class.java.classLoader)
        index = `in`.readInt()
        interpolation = `in`.readDouble()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(location, flags)
        dest.writeInt(index)
        dest.writeDouble(interpolation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Waypoint?> = object : Parcelable.Creator<Waypoint?> {
            override fun createFromParcel(`in`: Parcel): Waypoint? {
                return Waypoint(`in`)
            }

            override fun newArray(size: Int): Array<Waypoint?> {
                return arrayOfNulls(size)
            }
        }
    }
}