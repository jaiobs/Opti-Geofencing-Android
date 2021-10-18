package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.optisol.optigeofencingandroid.model.Agency


class Agency : Parcelable {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("url")
    var url: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        name = `in`.readString()
        url = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Agency?> = object : Parcelable.Creator<Agency?> {
            override fun createFromParcel(`in`: Parcel): Agency? {
                return com.optisol.optigeofencingandroid.model.Agency(`in`)
            }

            override fun newArray(size: Int): Array<Agency?> {
                return arrayOfNulls(size)
            }
        }
    }
}