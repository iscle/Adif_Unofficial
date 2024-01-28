package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass
import me.iscle.adifunofficial.elcano.circulation.model.CommuterNetwork

@JsonClass(generateAdapter = true)
data class StationInfoDTO(
    val accessible: AccessibleDTO?,
    val akaList: List<String?>?,
    val commercialZoneType: String?,
    val commuterNetwork: CommuterNetwork?,
    val description: String?,
    val lines: List<String?>?,
    val location: LocationDTO?,
    val longName: String?,
    val shortName: String?,
    val stationCode: String?,
    val stationType: String?,
    val trafficType: List<String?>?,
)
