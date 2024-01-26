package me.iscle.adifunofficial.station.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @PrimaryKey val id: Int,
    val latitude: Double,
    val longitude: Double,
)
