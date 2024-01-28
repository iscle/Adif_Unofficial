package me.iscle.adifunofficial.station

import me.iscle.adifunofficial.elcano.stations.model.RequestedStationInfoListDTO
import me.iscle.adifunofficial.elcano.stations.model.StationInfoDTO
import me.iscle.adifunofficial.util.AdifNormalizer

private const val TAG = "StationMapper"

object StationMapper {
    fun mapStationInfoDtoToStation(stationInfoDto: StationInfoDTO): StationEntity? {
        var longName = stationInfoDto.longName
        var shortName = stationInfoDto.shortName
        val code = stationInfoDto.stationCode
        val commuterNetwork = stationInfoDto.commuterNetwork

        if (longName == null) longName = shortName
        if (shortName == null) shortName = longName

        if (longName == null || shortName == null || code == null || commuterNetwork == null) {
            return null
        }

        val normalizedLongName = AdifNormalizer.normalize(longName)
        val normalizedShortName = AdifNormalizer.normalize(shortName)

        return StationEntity(
            longName = longName,
            normalizedLongName = normalizedLongName,
            shortName = shortName,
            normalizedShortName = normalizedShortName,
            code = code,
            location = null,
            commuterNetwork = commuterNetwork,
            trafficTypes = emptyList(),
        )
    }

    fun mapRequestedStationInfoListToStationList(requestedStationInfoList: List<RequestedStationInfoListDTO?>?): List<StationEntity>? {
        return requestedStationInfoList?.mapNotNull { requestedStationInfo ->
            val stationInfo = requestedStationInfo?.stationInfo ?: return@mapNotNull null
            mapStationInfoDtoToStation(stationInfo)
        }
    }
}