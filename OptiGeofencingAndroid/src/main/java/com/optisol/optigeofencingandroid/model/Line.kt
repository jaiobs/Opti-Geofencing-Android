package com.optisol.optigeofencingandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel


class Line : Parcelable {
    @SerializedName("agencies")
    var agencyList: List<Agency>? = null

    @SerializedName("color")
    var color: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("short_name")
    var shortName: String? = null

    @SerializedName("text_color")
    var textColor: String? = null

    @SerializedName("vehicle")
    var vehicle: Vehicle? = null

    constructor() {}
    constructor(`in`: Parcel) {
        agencyList = `in`.createTypedArrayList(Agency.CREATOR) as ArrayList<Agency>
        color = `in`.readString()
        name = `in`.readString()
        shortName = `in`.readString()
        textColor = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(agencyList)
        dest.writeString(color)
        dest.writeString(name)
        dest.writeString(shortName)
        dest.writeString(textColor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Line?> = object : Parcelable.Creator<Line?> {
            override fun createFromParcel(`in`: Parcel): Line? {
                return Line(`in`)
            }

            override fun newArray(size: Int): Array<Line?> {
                return arrayOfNulls(size)
            }
        }
    }
}