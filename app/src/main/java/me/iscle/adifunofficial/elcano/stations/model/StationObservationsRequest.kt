package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationObservationsRequest(
    val stationCodes: List<String>,
)
