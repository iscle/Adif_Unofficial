package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PassthroughDetailsStepDTO(
    val announceable: Boolean?,
    val arrivalPassthroughStepSides: PassthroughDetailsStepSideDTO?,
    val departurePassthroughStepSides: PassthroughDetailsStepSideDTO?,
    val stationCode: String?,
    val stopType: String?,
) {
    @JsonClass(generateAdapter = true)
    data class PassthroughDetailsStepSideDTO(
        val announceState: String?,
        val circulationState: CirculationState?,
        val ctcPlatform: String?,
        val forecastedOrAuditedDelay: Long?,
        val observation: String?,
        val operatorPlatform: String?,
        val plannedPlatform: String?,
        val plannedTime: Long?,
        val resultantPlatform: ResultantPlatform?,
        val showDelay: Boolean?,
        val sitraPlatform: String?,
        val technicalCirculationKey: PassthroughStepDTO.TechnicalCirculationKeyDTO?,
        val timeType: TimeType?,
    )
}
