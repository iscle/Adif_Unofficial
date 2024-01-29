package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Composition(
//    val coaches: List<Coaches?>?,
    val plannedTime: Long,
    val stationCode: String,
)
