package me.iscle.adifunofficial.station

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType
import me.iscle.adifunofficial.station.entity.StationEntity
import me.iscle.adifunofficial.station.entity.StationInfoEntity
import me.iscle.adifunofficial.station.entity.TrafficTypeEntity

@Dao
interface StationDao {
    @Transaction
    @Query("SELECT * FROM StationInfoEntity")
    suspend fun getAll(): List<StationEntity>

    @Insert
    suspend fun insertAll(stationEntities: List<StationInfoEntity>)

    @Insert
    suspend fun insert(stationEntity: StationInfoEntity)

    @Transaction
    @RawQuery
    suspend fun search(query: SupportSQLiteQuery): List<StationEntity>

    @Query("DELETE FROM StationInfoEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM TrafficTypeEntity WHERE id = :id")
    suspend fun getTrafficType(id: Int): TrafficTypeEntity?

    @Query("SELECT id FROM TrafficTypeEntity WHERE trafficType = :trafficType")
    suspend fun getTrafficTypeEntityId(trafficType: TrafficType): Long?

    @Query("INSERT INTO TrafficTypeEntity (trafficType) VALUES (:trafficType)")
    suspend fun insertTrafficType(trafficType: TrafficType): Long

    suspend fun getOrInsertTrafficType(trafficType: TrafficType): Long {
        return getTrafficTypeEntityId(trafficType) ?: insertTrafficType(trafficType)
    }
}