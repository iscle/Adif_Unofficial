package me.iscle.adifunofficial.station

import android.util.Log
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType
import me.iscle.adifunofficial.elcano.stations.model.RequestedStationInfoListDTO
import me.iscle.adifunofficial.elcano.stations.model.StationInfoDTO
import me.iscle.adifunofficial.station.entity.LocationEntity
import me.iscle.adifunofficial.station.model.Location
import me.iscle.adifunofficial.station.model.Station

private const val TAG = "StationMapper"

object StationMapper {
    fun mapLocation(locationEntity: LocationEntity?): Location? {
        if (locationEntity == null) return null

        return Location(
            latitude = locationEntity.latitude,
            longitude = locationEntity.longitude,
        )
    }

    fun mapStationInfoDtoToStation(stationInfoDto: StationInfoDTO): Station? {
        val code = stationInfoDto.stationCode
        var longName = stationInfoDto.longName
        var shortName = stationInfoDto.shortName
        val commuterNetwork = stationInfoDto.commuterNetwork

        if (longName == null) longName = shortName
        if (shortName == null) shortName = longName
        if (code == null || longName == null || shortName == null) {
            Log.d(TAG, "mapStationInfoDtoToStation: Skipping station. (code=$code, longName=$longName, shortName=$shortName)")
            return null
        }

        val trafficTypes = stationInfoDto.trafficType?.mapNotNull {
            if (it == null) return@mapNotNull null
            TrafficType.fromString(it)
        } ?: emptyList()

        val location = stationInfoDto.location?.let {
            if (it.latitude == null || it.longitude == null) return@let null

            Location(
                latitude = it.latitude,
                longitude = it.longitude,
            )
        }

        return Station(
            longName = longName,
            shortName = shortName,
            code = code,
            location = location,
            commuterNetwork = commuterNetwork,
            trafficTypes = trafficTypes,
        )
    }

    fun mapRequestedStationInfoListToStationList(requestedStationInfoList: List<RequestedStationInfoListDTO?>?): List<Station>? {
        return requestedStationInfoList?.mapNotNull { requestedStationInfo ->
            val stationInfo = requestedStationInfo?.stationInfo ?: return@mapNotNull null
            mapStationInfoDtoToStation(stationInfo)
        }
    }
}