package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OneStationRequest(
    val detailedInfo: DetailedInfoDTO?,
    val stationCode: String?,
    val token: String?,
)
