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
    data class PassthroughDetailsStepSideDTO(
        val announceState: String?,
        val circulationState: String?,
        val ctcPlatform: String?,
        val forecastedOrAuditedDelay: Int?,
        val observation: String?,
        val operatorPlatform: String?,
        val plannedPlatform: String?,
        val plannedTime: Long?,
        val resultantPlatform: String?,
        val showDelay: Boolean?,
        val sitraPlatform: String?,
        val technicalCirculationKey: PassthroughStepDTO.TechnicalCirculationKeyDTO?,
        val timeType: String?,
    )
}
