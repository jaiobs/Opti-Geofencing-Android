package com.example.orgware.kotlinapicall.data.respones.polyline

import com.google.gson.annotations.SerializedName

data class StartLocation (

	@field:SerializedName("lng")
	var lng: Double? = null,

	@field:SerializedName("lat")
	var lat: Double? = null


)