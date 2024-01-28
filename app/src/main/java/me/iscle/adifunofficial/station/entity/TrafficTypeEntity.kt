package me.iscle.adifunofficial.station.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType

@Entity
data class TrafficTypeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(index = true) val trafficType: TrafficType,
)