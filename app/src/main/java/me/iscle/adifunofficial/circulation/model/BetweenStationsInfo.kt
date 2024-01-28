package me.iscle.adifunofficial.circulation.model

data class BetweenStationsInfo(
    val routeInfo: RouteInfo,
    val originStopInfo: StopInfo,
    val destinationStopInfo: StopInfo,
)