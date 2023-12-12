package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailedInfoDTO(
    val extendedStationInfo: Boolean,
    val stationActivities: Boolean,
    val stationBanner: Boolean,
    val stationCommercialServices: Boolean,
    val stationInfo: Boolean,
    val stationServices: Boolean,
    val stationTransportServices: Boolean,
)