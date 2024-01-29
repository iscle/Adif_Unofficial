package me.iscle.adifunofficial.station.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.iscle.adifunofficial.elcano.circulation.model.CommuterNetwork

@Entity
data class StationEntity(
    val longName: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val normalizedLongName: String,
    val shortName: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val normalizedShortName: String,
    @PrimaryKey @ColumnInfo(collate = ColumnInfo.NOCASE) val code: String,
    val commuterNetwork: CommuterNetwork?,
)
