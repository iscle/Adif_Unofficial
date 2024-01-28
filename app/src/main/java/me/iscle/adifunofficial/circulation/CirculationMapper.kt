package me.iscle.adifunofficial.circulation

import me.iscle.adifunofficial.circulation.model.ArrivalDepartureInfo
import me.iscle.adifunofficial.circulation.model.BetweenStationsInfo
import me.iscle.adifunofficial.circulation.model.RouteInfo
import me.iscle.adifunofficial.circulation.model.StopInfo
import me.iscle.adifunofficial.elcano.circulation.model.CommercialPathInfoDTO
import me.iscle.adifunofficial.elcano.circulation.model.CommercialPathRouteSidesInfoDTO
import me.iscle.adifunofficial.elcano.circulation.model.PassthroughDetailsStepDTO
import me.iscle.adifunofficial.elcano.circulation.model.ResultantPlatform

private const val TAG = "CirculationMapper"

object CirculationMapper {
    fun mapArrivals(
        commercialPathRouteSidesInfoDto: CommercialPathRouteSidesInfoDTO,
    ): ArrivalDepartureInfo? {
        val commercialPathInfo = commercialPathRouteSidesInfoDto.commercialPathInfo ?: return null
        val passthroughSteps = commercialPathRouteSidesInfoDto.passthroughSteps?.firstOrNull() ?: return null
        val arrivalPassthroughStepSides = passthroughSteps.arrivalPassthroughStepSides ?: return null

        return mapArrivalDepartureInfo(commercialPathInfo, arrivalPassthroughStepSides)
    }

    fun mapDepartures(
        commercialPathRouteSidesInfoDto: CommercialPathRouteSidesInfoDTO,
    ): ArrivalDepartureInfo? {
        val commercialPathInfo = commercialPathRouteSidesInfoDto.commercialPathInfo ?: return null
        val passthroughSteps = commercialPathRouteSidesInfoDto.passthroughSteps?.firstOrNull() ?: return null
        val departurePassThroughStepSides = passthroughSteps.departurePassthroughStepSides ?: return null

        return mapArrivalDepartureInfo(commercialPathInfo, departurePassThroughStepSides)
    }
    
    fun mapBetweenStations(
        commercialPathRouteSidesInfoDtos: List<CommercialPathRouteSidesInfoDTO?>,
    ): List<BetweenStationsInfo> {
        return commercialPathRouteSidesInfoDtos.filterNotNull().mapNotNull { mapBetweenStations(it) }
    }

    fun mapBetweenStations(
        commercialPathRouteSidesInfoDto: CommercialPathRouteSidesInfoDTO,
    ): BetweenStationsInfo? {
        val commercialPathInfo = commercialPathRouteSidesInfoDto.commercialPathInfo ?: return null
        val passthroughSteps = commercialPathRouteSidesInfoDto.passthroughSteps?.firstOrNull() ?: return null
        val departurePassThroughStepSides = passthroughSteps.departurePassthroughStepSides ?: return null
        val arrivalPassThroughStepSides = passthroughSteps.arrivalPassthroughStepSides ?: return null

        val routeInfo = mapRouteInfo(commercialPathInfo) ?: return null
        val departureStopInfo = mapStopInfo(departurePassThroughStepSides) ?: return null
        val arrivalStopInfo = mapStopInfo(arrivalPassThroughStepSides) ?: return null

        return BetweenStationsInfo(
            routeInfo = routeInfo,
            originStopInfo = departureStopInfo,
            destinationStopInfo = arrivalStopInfo,
        )
    }

    private fun mapArrivalDepartureInfo(
        commercialPathInfo: CommercialPathInfoDTO,
        passthroughStepSides: PassthroughDetailsStepDTO.PassthroughDetailsStepSideDTO,
    ): ArrivalDepartureInfo? {
        val routeInfo = mapRouteInfo(commercialPathInfo) ?: return null
        val stopInfo = mapStopInfo(passthroughStepSides) ?: return null

        return ArrivalDepartureInfo(
            routeInfo = routeInfo,
            stopInfo = stopInfo,
        )
    }

    private fun mapRouteInfo(
        commercialPathInfo: CommercialPathInfoDTO,
    ): RouteInfo? {
        val originStation = commercialPathInfo.commercialOriginStationCode ?: return null
        val destinationStation = commercialPathInfo.commercialDestinationStationCode ?: return null
        val line = commercialPathInfo.line ?: return null
        val trafficType = commercialPathInfo.trafficType ?: return null

        return RouteInfo(
            originStation = originStation,
            destinationStation = destinationStation,
            line = line,
            trafficType = trafficType,
        )
    }

    private fun mapStopInfo(
        passthroughStepSides: PassthroughDetailsStepDTO.PassthroughDetailsStepSideDTO,
    ): StopInfo? {
        val plannedTime = passthroughStepSides.plannedTime ?: return null
        val delay = passthroughStepSides.forecastedOrAuditedDelay
        val showDelay = passthroughStepSides.showDelay
        val timeType = passthroughStepSides.timeType ?: return null
        val platform = when (passthroughStepSides.resultantPlatform) {
            ResultantPlatform.SITRA -> passthroughStepSides.sitraPlatform
            ResultantPlatform.CTC -> passthroughStepSides.ctcPlatform
            ResultantPlatform.OPERATOR -> passthroughStepSides.operatorPlatform
            ResultantPlatform.RELIABLE_PLANNED -> passthroughStepSides.plannedPlatform
            ResultantPlatform.PLANNED -> ""
            else -> null
        }.let { if (it == "*") null else it }
        val circulationState = passthroughStepSides.circulationState ?: return null

        return StopInfo(
            plannedTime = plannedTime,
            delay = delay,
            showDelay = showDelay ?: false,
            timeType = timeType,
            platform = platform,
            circulationState = circulationState,
        )
    }
}