package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.optisol.optigeofencingandroid.model.Vehicle

class Vehicle : Parcelable {
    @SerializedName("icon")
    var iconUrl: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("type")
    var type: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
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
                return com.optisol.optigeofencingandroid.model.Vehicle(`in`)
            }

            override fun newArray(size: Int): Array<Vehicle?> {
                return arrayOfNulls(size)
            }
        }
    }
}