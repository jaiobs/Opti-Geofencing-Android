package com.optisol.optigeofencingandroid.respones.gird

import com.google.gson.annotations.SerializedName

data class ServicesItem(

	@field:SerializedName("service_created")
	val serviceCreated: String? = null,

	@field:SerializedName("hide")
	val hide: String? = null,

	@field:SerializedName("services_logo")
	val servicesLogo: String? = null,

	@field:SerializedName("service_status")
	val serviceStatus: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)