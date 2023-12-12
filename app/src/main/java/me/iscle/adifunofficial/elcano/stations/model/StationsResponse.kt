package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationsResponse(
    val requestedStationInfoList: List<RequestedStationInfoListDTO>?,
    val token: String?,
)
