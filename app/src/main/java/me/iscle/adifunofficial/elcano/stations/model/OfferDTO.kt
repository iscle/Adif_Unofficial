package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OfferDTO(
    val descrpiton: String,
    val logo: String,
    val name: String,
    val pictures: List<String>,
)
