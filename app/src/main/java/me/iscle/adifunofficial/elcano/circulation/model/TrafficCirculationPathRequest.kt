package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrafficCirculationPathRequest(
    val commercialService: CirculationPathRequest.State?,
    val commercialStopType: CirculationPathRequest.State?,
    val destinationStationCode: String?,
    val originStationCode: String?,
    val page: CirculationPathRequest.PageInfoDTO?,
    val stationCode: String?,
    val trafficType: TrafficType?,
)
