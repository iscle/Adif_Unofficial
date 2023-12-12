package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteStepDTO(
    val arrivalPassthroughStepSides: RouteStepSideDTO,
    val departurePassthroughStepSides: RouteStepSideDTO,
) {
    data class RouteStepSideDTO(
        val announceState: String,
        val announceable: Boolean,
        val circulationState: String,
        val ctcPlatform: String,
        val forecastedOrAuditedDelay: Int,
        val observation: String,
        val operatorPlatform: String,
        val plannedPlatform: String,
        val plannedTime: Long,
        val resultantPlatform: String,
        val sitraPlatform: String,
        val stationCode: String,
        val stopType: String,
        val technicalNumber: String,
        val timeType: String,
        val visualEffects: VisualEffectsDTO,
    )
}
