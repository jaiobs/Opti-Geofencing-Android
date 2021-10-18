/*

Copyright 2015 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/
package com.optisol.optigeofencingandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel

/**
 * Created by Akexorcist on 11/29/15 AD.
 */
class Route : Parcelable {
    @SerializedName("bounds")
    var bound: Bound? = null

    @SerializedName("copyrights")
    var copyrights: String? = null

    @SerializedName("legs")
    var legList: List<Leg>? = null

    @SerializedName("overview_polyline")
    var overviewPolyline: RoutePolyline? = null

    @SerializedName("summary")
    var summary: String? = null

    @SerializedName("fare")
    var fare: Fare? = null

    @SerializedName("warnings")
    var warningList: List<String>? = null

    @SerializedName("waypoint_order")
    var waypointOrderList: List<Int>? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        bound = `in`.readParcelable(Bound::class.java.classLoader)
        copyrights = `in`.readString()
        overviewPolyline = `in`.readParcelable(RoutePolyline::class.java.classLoader)
        summary = `in`.readString()
        fare = `in`.readParcelable(Fare::class.java.classLoader)
        warningList = `in`.createStringArrayList()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeParcelable(bound, i)
        parcel.writeString(copyrights)
        parcel.writeParcelable(overviewPolyline, i)
        parcel.writeString(summary)
        parcel.writeParcelable(fare, i)
        parcel.writeStringList(warningList)
    }

    companion object {
        val CREATOR: Parcelable.Creator<Route?> = object : Parcelable.Creator<Route?> {
            override fun createFromParcel(`in`: Parcel): Route? {
                return Route(`in`)
            }

            override fun newArray(size: Int): Array<Route?> {
                return arrayOfNulls(size)
            }
        }
    }
}