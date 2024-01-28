package me.iscle.adifunofficial.circulation.model

import me.iscle.adifunofficial.elcano.circulation.model.TrafficType

data class RouteInfo(
    val originStation: String?,
    val destinationStation: String?,
    val line: String?,
    val trafficType: TrafficType?,
)
