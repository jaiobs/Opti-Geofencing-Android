package com.example.optisol.kotlinemap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel


class Step : Parcelable {
    @SerializedName("distance")
    var distance: Info? = null

    @SerializedName("duration")
    var duration: Info? = null

    @SerializedName("end_location")
    var endLocation: Coordination? = null

    @SerializedName("html_instructions")
    var htmlInstruction: String? = null

    @SerializedName("maneuver")
    var maneuver: String? = null

    @SerializedName("start_location")
    var startLocation: Coordination? = null

    @SerializedName("transit_details")
    var transitDetail: TransitDetail? = null

    @SerializedName("steps")
    var stepList: List<Step>? = null

    @SerializedName("polyline")
    var polyline: RoutePolyline? = null

    @SerializedName("travel_mode")
    var travelMode: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        distance = `in`.readParcelable(Info::class.java.classLoader)
        duration = `in`.readParcelable(Info::class.java.classLoader)
        endLocation = `in`.readParcelable(Coordination::class.java.classLoader)
        htmlInstruction = `in`.readString()
        maneuver = `in`.readString()
        startLocation = `in`.readParcelable(Coordination::class.java.classLoader)
        stepList = `in`.createTypedArrayList(CREATOR)  as List<Step>
        travelMode = `in`.readString()
    }

    val isContainStepList: Boolean
        get() = stepList != null && stepList!!.size > 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(distance, flags)
        dest.writeParcelable(duration, flags)
        dest.writeParcelable(endLocation, flags)
        dest.writeString(htmlInstruction)
        dest.writeString(maneuver)
        dest.writeParcelable(startLocation, flags)
        dest.writeTypedList(stepList)
        dest.writeString(travelMode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Step?> = object : Parcelable.Creator<Step?> {
            override fun createFromParcel(`in`: Parcel): Step? {
                return Step(`in`)
            }

            override fun newArray(size: Int): Array<Step?> {
                return arrayOfNulls(size)
            }
        }
    }
}