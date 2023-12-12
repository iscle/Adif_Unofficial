package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationObservationsDTO(
    val observations: List<ObservationDTO>,
    val stationCode: String,
)
