package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationInfoDTO(
    val accessible: AccessibleDTO?,
    val akaList: List<String>,
    val commercialZoneType: String,
    val commuterNetwork: String?,
    val description: String?,
    val lines: List<String>,
    val location: LocationDTO,
    val longName: String,
    val shortName: String,
    val stationCode: String,
    val stationType: String,
    val trafficType: List<String>,
)
