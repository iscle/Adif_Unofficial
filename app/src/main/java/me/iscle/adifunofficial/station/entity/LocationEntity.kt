package me.iscle.adifunofficial.station.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val stationCode: String,
    val latitude: Double,
    val longitude: Double,
)
