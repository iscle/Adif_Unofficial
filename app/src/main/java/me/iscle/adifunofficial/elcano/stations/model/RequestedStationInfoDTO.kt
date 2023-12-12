package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestedStationInfoDTO(
    val banner: BannerDTO,
    val extendedStationInfo: ExtendedStationInfoDTO,
    val stationActivities: List<StationCommercialServicesDTO>,
    val stationCode: String,
    val stationCommercialServices: List<StationCommercialServicesDTO>,
    val stationInfo: StationInfoDTO,
    val stationServices: List<StationServicesDTO>,
    val stationTransportServices: List<StationTransportServicesDTO>,
)
