package me.iscle.adifunofficial.station.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class StationEntity(
    @Embedded var info: StationInfoEntity,
    @Relation(
        parentColumn = "code",
        entityColumn = "stationCode",
    )
    var location: LocationEntity?,
    @Relation(
        parentColumn = "code",
        entityColumn = "id",
        associateBy = Junction(
            StationTrafficTypeCrossRef::class,
            parentColumn = "stationCode",
            entityColumn = "trafficTypeId",
        ),
    )
    var trafficTypes: List<TrafficTypeEntity>,
)