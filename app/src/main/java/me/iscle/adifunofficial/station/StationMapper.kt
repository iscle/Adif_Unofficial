package me.iscle.adifunofficial.station

import me.iscle.adifunofficial.AdifNormalizer
import me.iscle.adifunofficial.elcano.stations.model.RequestedStationInfoListDTO
import me.iscle.adifunofficial.elcano.stations.model.StationInfoDTO
import me.iscle.adifunofficial.station.model.Station

private const val TAG = "StationMapper"

object StationMapper {
    fun mapStationInfoDtoToStation(stationInfoDto: StationInfoDTO): Station? {
        var longName = stationInfoDto.longName
        var shortName = stationInfoDto.shortName
        val code = stationInfoDto.stationCode

        if (longName == null) longName = shortName
        if (shortName == null) shortName = longName

        if (longName == null || shortName == null || code == null) {
            return null
        }

        val normalizedLongName = AdifNormalizer.normalize(longName)
        val normalizedShortName = AdifNormalizer.normalize(shortName)

        return Station(
            longName = longName,
            normalizedLongName = normalizedLongName,
            shortName = shortName,
            normalizedShortName = normalizedShortName,
            code = code,
        )
    }

    fun mapRequestedStationInfoListToStationList(requestedStationInfoList: List<RequestedStationInfoListDTO?>?): List<Station>? {
        return requestedStationInfoList?.mapNotNull { requestedStationInfo ->
            val stationInfo = requestedStationInfo?.stationInfo ?: return@mapNotNull null
            mapStationInfoDtoToStation(stationInfo)
        }
    }
}