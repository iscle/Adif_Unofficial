package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrafficCirculationPathRequest(
    val commercialService: CirculationPathRequest.State?, // seems to always be YES
    val commercialStopType: CirculationPathRequest.State?, // seems to always be YES
    val destinationStationCode: String?, // only used when searching between stations
    val originStationCode: String?, // only used when searching between stations
    val page: CirculationPathRequest.PageInfoDTO?,
    val stationCode: String?, // only used when searching arrivals/departures
    val trafficType: TrafficType?,
)
