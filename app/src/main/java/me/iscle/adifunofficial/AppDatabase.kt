package me.iscle.adifunofficial

import androidx.room.Database
import androidx.room.RoomDatabase
import me.iscle.adifunofficial.station.StationDao
import me.iscle.adifunofficial.station.model.Location
import me.iscle.adifunofficial.station.model.Station

@Database(
    entities = [
        Location::class,
        Station::class
    ],
    version = 2,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
}