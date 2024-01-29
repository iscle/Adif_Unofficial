package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteStepDTO(
    val arrivalPassthroughStepSides: RouteStepSideDTO?,
    val departurePassthroughStepSides: RouteStepSideDTO?,
) {
    @JsonClass(generateAdapter = true)
    data class RouteStepSideDTO(
        val announceState: String?,
        val announceable: Boolean?,
        val circulationState: CirculationState?,
        val ctcPlatform: String?,
        val forecastedOrAuditedDelay: Long?,
        val observation: String?,
        val operatorPlatform: String?,
        val plannedPlatform: String?,
        val plannedTime: Long?,
        val resultantPlatform: ResultantPlatform?,
        val sitraPlatform: String?,
        val stationCode: String?,
        val stopType: String?,
        val technicalNumber: String?,
        val timeType: TimeType?,
        val visualEffects: VisualEffectsDTO?,
    )
}
