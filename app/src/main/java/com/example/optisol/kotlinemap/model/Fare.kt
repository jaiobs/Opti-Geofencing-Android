package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.optisol.optigeofencingandroid.model.Fare

class Fare : Parcelable {
    @SerializedName("currency")
    var currency: String? = null

    @SerializedName("value")
    var value: String? = null

    @SerializedName("text")
    var text: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        currency = `in`.readString()
        value = `in`.readString()
        text = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(currency)
        dest.writeString(value)
        dest.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Fare?> = object : Parcelable.Creator<Fare?> {
            override fun createFromParcel(`in`: Parcel): Fare? {
                return com.optisol.optigeofencingandroid.model.Fare(`in`)
            }

            override fun newArray(size: Int): Array<Fare?> {
                return arrayOfNulls(size)
            }
        }
    }
}