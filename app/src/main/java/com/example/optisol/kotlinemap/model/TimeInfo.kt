package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel


class TimeInfo : Parcelable {
    @SerializedName("text")
    var text: String? = null

    @SerializedName("time_zone")
    var timeZone: String? = null

    @SerializedName("value")
    var value: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        text = `in`.readString()
        timeZone = `in`.readString()
        value = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(text)
        dest.writeString(timeZone)
        dest.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<TimeInfo?> = object : Parcelable.Creator<TimeInfo?> {
            override fun createFromParcel(`in`: Parcel): TimeInfo? {
                return TimeInfo(`in`)
            }

            override fun newArray(size: Int): Array<TimeInfo?> {
                return arrayOfNulls(size)
            }
        }
    }
}