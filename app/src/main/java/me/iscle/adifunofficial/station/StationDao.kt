package me.iscle.adifunofficial.station

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType
import me.iscle.adifunofficial.station.entity.StationEntity
import me.iscle.adifunofficial.station.entity.StationTrafficTypeCrossRef
import me.iscle.adifunofficial.station.model.Station
import me.iscle.adifunofficial.util.AdifNormalizer

@Dao
interface StationDao {
    @Insert
    suspend fun insert(stationEntity: StationEntity)

    @Transaction
    @RawQuery
    suspend fun search(query: SupportSQLiteQuery): List<StationEntity>

    @Query("DELETE FROM StationEntity")
    suspend fun deleteAll()

    @Query("SELECT id FROM TrafficTypeEntity WHERE trafficType = :trafficType")
    suspend fun getTrafficTypeEntityId(trafficType: TrafficType): Long?

    @Query("INSERT INTO TrafficTypeEntity (trafficType) VALUES (:trafficType)")
    suspend fun insertTrafficType(trafficType: TrafficType): Long

    @Insert
    suspend fun insert(stationTrafficTypeCrossRef: StationTrafficTypeCrossRef)

    @Transaction
    suspend fun insertAll(stations: List<Station>) {
        for (station in stations) {
            val stationEntity = StationEntity(
                longName = station.longName,
                normalizedLongName = AdifNormalizer.normalize(station.longName),
                shortName = station.shortName,
                normalizedShortName = AdifNormalizer.normalize(station.shortName),
                code = station.code,
                commuterNetwork = station.commuterNetwork,
            )
            insert(stationEntity)

            for (trafficType in station.trafficTypes) {
                val trafficTypeEntityId = getTrafficTypeEntityId(trafficType) ?: insertTrafficType(trafficType)
                val stationTrafficTypeCrossRef = StationTrafficTypeCrossRef(
                    stationCode = station.code,
                    trafficTypeId = trafficTypeEntityId,
                )
                insert(stationTrafficTypeCrossRef)
            }
        }
    }

    @Transaction
    suspend fun searchStations(searchString: String): List<Station> {
        val parts = AdifNormalizer.normalize(searchString).split("\\s+".toRegex())

        var queryString = "SELECT * FROM Station WHERE ("
        parts.forEachIndexed { index, s ->
            queryString += "normalizedLongName LIKE '%' || ? || '%'"
            if (index != parts.size - 1) {
                queryString += " AND "
            }
        }
        queryString += ") OR ("
        parts.forEachIndexed { index, s ->
            queryString += "normalizedShortName LIKE '%' || ? || '%'"
            if (index != parts.size - 1) {
                queryString += " AND "
            }
        }
        queryString += ") OR (code LIKE '%' || ? || '%')"


        val finalArgs = parts + parts + queryString
        val query = SimpleSQLiteQuery(queryString, finalArgs.toTypedArray())
        stationDao.search(query)

        val stations = search(query)
        return stations.map { stationEntity ->
            Station(
                code = stationEntity.info.code,
                longName = stationEntity.info.longName,
                shortName = stationEntity.info.shortName,
                commuterNetwork = stationEntity.info.commuterNetwork,
                trafficTypes = stationEntity.trafficTypes.map { trafficTypeEntity ->
                    TrafficType.valueOf(trafficTypeEntity.trafficType)
                },
                location = stationEntity.location,
            )
        }
    }
}