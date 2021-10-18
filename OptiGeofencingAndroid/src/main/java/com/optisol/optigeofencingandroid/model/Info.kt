package com.optisol.optigeofencingandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel


class Info : Parcelable {
    @SerializedName("text")
    var text: String? = null

    @SerializedName("value")
    var value: String? = null

    constructor() {}
    constructor(`in`: Parcel) {
        text = `in`.readString()
        value = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(text)
        dest.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Info?> = object : Parcelable.Creator<Info?> {
            override fun createFromParcel(`in`: Parcel): Info? {
                return Info(`in`)
            }

            override fun newArray(size: Int): Array<Info?> {
                return arrayOfNulls(size)
            }
        }
    }
}