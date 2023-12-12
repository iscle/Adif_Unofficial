package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExtendedStationInfoDTO(
    val blueprints: List<String>,
    val locator: LocatorDTO,
    val openingHours: OpeningHoursDTO,
    val pictures: List<String>,
)
