package com.optisol.optigeofencingandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel

class Bound : Parcelable {
    @SerializedName("northeast")
    var northeastCoordination: Coordination? = null
        private set

    @SerializedName("southwest")
    var southwestCoordination: Coordination? = null
        private set

    constructor() {}
    constructor(`in`: Parcel) {
        northeastCoordination = `in`.readParcelable(Coordination::class.java.classLoader)
        southwestCoordination = `in`.readParcelable(Coordination::class.java.classLoader)
    }

    fun setNortheast(northeast: Coordination?) {
        northeastCoordination = northeast
    }

    fun setSouthwest(southwest: Coordination?) {
        southwestCoordination = southwest
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(northeastCoordination, flags)
        dest.writeParcelable(southwestCoordination, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Bound?> = object : Parcelable.Creator<Bound?> {
            override fun createFromParcel(`in`: Parcel): Bound? {
                return Bound(`in`)
            }

            override fun newArray(size: Int): Array<Bound?> {
                return arrayOfNulls(size)
            }
        }
    }
}