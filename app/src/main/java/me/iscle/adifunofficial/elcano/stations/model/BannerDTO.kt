package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BannerDTO(
    val bannerDescription: String?,
    val bannerTitle: String?,
    val bannerURI: String?,
)
