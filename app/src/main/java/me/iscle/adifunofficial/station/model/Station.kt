package me.iscle.adifunofficial.station.model

import me.iscle.adifunofficial.elcano.circulation.model.CommuterNetwork
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType

data class Station(
    val code: String,
    val longName: String,
    val shortName: String,
    val commuterNetwork: CommuterNetwork,
    val trafficTypes: List<TrafficType>,
    val location: Location?,
)