package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocatorDTO(
    val description: String,
    val location: LocationDTO,
)