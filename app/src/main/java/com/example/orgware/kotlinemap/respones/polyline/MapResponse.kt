package com.example.orgware.kotlinapicall.data.respones.polyline

import com.google.gson.annotations.SerializedName

data class MapResponse(

	@field:SerializedName("routes")
	val routes: List<RoutesItem?>? = null,

	@field:SerializedName("geocoded_waypoints")
	val geocodedWaypoints: List<GeocodedWaypointsItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)