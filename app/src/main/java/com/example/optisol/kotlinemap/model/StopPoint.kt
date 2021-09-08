package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel


class StopPoint : Parcelable {
    @SerializedName("location")
    var location: Coordination? = null

    @SerializedName("name")
    var name: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        location = `in`.readParcelable(Coordination::class.java.classLoader)
        name = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(location, flags)
        dest.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<StopPoint?> = object : Parcelable.Creator<StopPoint?> {
            override fun createFromParcel(`in`: Parcel): StopPoint? {
                return StopPoint(`in`)
            }

            override fun newArray(size: Int): Array<StopPoint?> {
                return arrayOfNulls(size)
            }
        }
    }
}