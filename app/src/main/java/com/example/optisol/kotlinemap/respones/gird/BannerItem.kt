package com.example.optisol.kotlinemap.respones.gird

import com.google.gson.annotations.SerializedName

data class BannerItem(

	@field:SerializedName("hide")
	val hide: String? = null,

	@field:SerializedName("banner_url")
	val bannerUrl: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)