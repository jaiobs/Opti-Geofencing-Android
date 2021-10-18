package com.optisol.optigeofencingandroid.model

import com.optisol.optigeofencingandroid.config.DirectionConverter
import com.google.gson.annotations.SerializedName
import com.google.android.gms.maps.model.LatLng
import java.util.ArrayList


class Leg {
    @SerializedName("arrival_time")
    var arrivalTime: TimeInfo? = null

    @SerializedName("departure_time")
    var departureTime: TimeInfo? = null

    @SerializedName("distance")
    var distance: Info? = null

    @SerializedName("duration")
    var duration: Info? = null

    @SerializedName("duration_in_traffic")
    var durationInTraffic: Info? = null

    @SerializedName("end_address")
    var endAddress: String? = null

    @SerializedName("end_location")
    var endLocation: Coordination? = null

    @SerializedName("start_address")
    var startAddress: String? = null

    @SerializedName("start_location")
    var startLocation: Coordination? = null

    @SerializedName("steps")
    var stepList: List<Step>? = null

    @SerializedName("via_waypoint")
    var viaWaypointList: List<Waypoint>? = null
    val directionPoint: ArrayList<LatLng>
        get() = DirectionConverter.getDirectionPoint(stepList)
    val sectionPoint: ArrayList<LatLng>
        get() = DirectionConverter.getSectionPoint(stepList)
}