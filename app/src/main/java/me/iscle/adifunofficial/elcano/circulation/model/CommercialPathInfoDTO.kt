package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommercialPathInfoDTO(
    val announceableStations: List<String>?,
    val commercialDestinationStationCode: String?,
    val commercialOriginStationCode: String?,
    val commercialPathKey: ComercialPathKeyDTO?,
    val compositionData: CompositionDataDTO?,
    val line: String?,
    val observation: String?,
    val opeProComPro: OpeProComProDTO?,
    val timestamp: Long?,
    val trafficType: TrafficType?,
) {
    @JsonClass(generateAdapter = true)
    data class ComercialPathKeyDTO(
        val commercialCirculationKey: CommercialCirculationKeyDTO?,
        val destinationStationCode: String?,
        val originStationCode: String?,
    )

    @JsonClass(generateAdapter = true)
    data class CompositionDataDTO(
        val compositionType: String?,
        val compositionTypeCode: String?,
        val compositionTypeDescription: String?,
    )

    @JsonClass(generateAdapter = true)
    data class OpeProComProDTO(
        val commercialProduct: String?,
        val operator: String?,
        val product: String?,
    )

    @JsonClass(generateAdapter = true)
    data class CommercialCirculationKeyDTO(
        val commercialNumber: String?,
        val launchingDate: Long?,
    )
}
