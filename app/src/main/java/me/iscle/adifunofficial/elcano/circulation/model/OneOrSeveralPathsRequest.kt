package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OneOrSeveralPathsRequest(
    val allControlPoints: Boolean,
    val commercialNumber: String,
    val destinationStationCode: String,
    val launchingDate: Long,
    val originStationCode: String,
)
