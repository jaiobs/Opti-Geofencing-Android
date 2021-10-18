package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.optisol.optigeofencingandroid.model.Line
import com.optisol.optigeofencingandroid.model.StopPoint
import com.optisol.optigeofencingandroid.model.TimeInfo
import com.optisol.optigeofencingandroid.model.TransitDetail


class TransitDetail : Parcelable {
    @SerializedName("arrival_stop")
    var arrivalStopPoint: StopPoint? = null

    @SerializedName("arrival_time")
    var arrivalTime: TimeInfo? = null

    @SerializedName("departure_stop")
    var departureStopPoint: StopPoint? = null

    @SerializedName("departure_time")
    var departureTime: TimeInfo? = null

    @SerializedName("line")
    var line: Line? = null

    @SerializedName("headsign")
    var headsign: String? = null

    @SerializedName("num_stops")
    var stopNumber: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        arrivalStopPoint = `in`.readParcelable(StopPoint::class.java.classLoader)
        arrivalTime = `in`.readParcelable(TimeInfo::class.java.classLoader)
        departureStopPoint = `in`.readParcelable(StopPoint::class.java.classLoader)
        departureTime = `in`.readParcelable(TimeInfo::class.java.classLoader)
        line = `in`.readParcelable(Line::class.java.classLoader)
        headsign = `in`.readString()
        stopNumber = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(arrivalStopPoint, flags)
        dest.writeParcelable(arrivalTime, flags)
        dest.writeParcelable(departureStopPoint, flags)
        dest.writeParcelable(departureTime, flags)
        dest.writeParcelable(line, flags)
        dest.writeString(headsign)
        dest.writeString(stopNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<TransitDetail?> =
            object : Parcelable.Creator<TransitDetail?> {
                override fun createFromParcel(`in`: Parcel): TransitDetail? {
                    return com.optisol.optigeofencingandroid.model.TransitDetail(`in`)
                }

                override fun newArray(size: Int): Array<TransitDetail?> {
                    return arrayOfNulls(size)
                }
            }
    }
}