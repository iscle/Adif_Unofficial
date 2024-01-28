package me.iscle.adifunofficial

import androidx.room.Database
import androidx.room.RoomDatabase
import me.iscle.adifunofficial.station.StationDao
import me.iscle.adifunofficial.station.entity.LocationEntity
import me.iscle.adifunofficial.station.entity.StationInfoEntity
import me.iscle.adifunofficial.station.entity.StationTrafficTypeCrossRef
import me.iscle.adifunofficial.station.entity.TrafficTypeEntity

@Database(
    entities = [
        LocationEntity::class,
        StationInfoEntity::class,
        TrafficTypeEntity::class,
        StationTrafficTypeCrossRef::class,
    ],
    version = 3,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
}