package me.iscle.adifunofficial.station.entity

import androidx.room.Entity

@Entity(primaryKeys = ["stationCode", "trafficTypeId"])
data class StationTrafficTypeCrossRef(
    val stationCode: String,
    val trafficTypeId: Long,
)
