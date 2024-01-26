package me.iscle.adifunofficial.station.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Station(
    val longName: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val normalizedLongName: String,
    val shortName: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val normalizedShortName: String,
    @PrimaryKey @ColumnInfo(collate = ColumnInfo.NOCASE) val code: String,
//    val location: Location?,
)