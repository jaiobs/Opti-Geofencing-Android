package com.example.optisol.kotlinemap.respones.gird

import com.google.gson.annotations.SerializedName

data class GirdResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("success")
	val success: Int? = null,

	@field:SerializedName("banner")
	val banner: List<BannerItem?>? = null,

	@field:SerializedName("services")
	val services: List<ServicesItem?>? = null
)