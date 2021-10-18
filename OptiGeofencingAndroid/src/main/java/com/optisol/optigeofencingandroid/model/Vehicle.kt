package com.optisol.optigeofencingandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel

class Vehicle : Parcelable {
    @SerializedName("icon")
    var iconUrl: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("type")
    var type: String? = null

    constructor() {}
    constructor(`in`: Parcel) {
        iconUrl = `in`.readString()
        name = `in`.readString()
        type = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(iconUrl)
        dest.writeString(name)
        dest.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Vehicle?> = object : Parcelable.Creator<Vehicle?> {
            override fun createFromParcel(`in`: Parcel): Vehicle? {
                return Vehicle(`in`)
            }

            override fun newArray(size: Int): Array<Vehicle?> {
                return arrayOfNulls(size)
            }
        }
    }
}