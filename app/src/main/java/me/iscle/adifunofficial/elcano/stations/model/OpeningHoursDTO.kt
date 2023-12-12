package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpeningHoursDTO(
    val longText: String,
    val shortText: String,
)
