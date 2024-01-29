package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PassthroughStepDTO(
    val announceable: Boolean?,
    val arrivalPassthroughStepSides: DeparturePassthroughStepSidesDTO?,
    val departurePassthroughStepSides: DeparturePassthroughStepSidesDTO?,
    val stationCode: String?,
    val stopType: String?,
) {
    @JsonClass(generateAdapter = true)
    data class DeparturePassthroughStepSidesDTO(
        val announceState: String?,
        val circulationState: String?,
        val ctcPlatform: String?,
        val forecastedOrAuditedDelay: Int?,
        val observation: String?,
        val operatorPlatform: String?,
        val plannedPlatform: String?,
        val plannedTime: Long?,
        val resultantPlatform: String?,
        val sitraPlatform: String?,
        val technicalCirculationKey: TechnicalCirculationKeyDTO?,
        val timeType: String?,
        val visualEffects: VisualEffectsDTO?,
    )

    @JsonClass(generateAdapter = true)
    data class TechnicalCirculationKeyDTO(
        val technicalLaunchingDate: Long?,
        val technicalNumber: String?,
    )
}
