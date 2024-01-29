package me.iscle.adifunofficial.station

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType
import me.iscle.adifunofficial.station.entity.LocationEntity
import me.iscle.adifunofficial.station.entity.StationEntity
import me.iscle.adifunofficial.station.entity.StationTrafficTypeCrossRef
import me.iscle.adifunofficial.station.entity.TrafficTypeEntity
import me.iscle.adifunofficial.station.model.Station
import me.iscle.adifunofficial.util.AdifNormalizer

@Dao
interface StationDao {
    @Insert
    suspend fun insert(stationEntity: StationEntity)

    @Query("SELECT * FROM StationEntity WHERE code = :stationCode")
    suspend fun getStationEntity(stationCode: String): StationEntity?

    @Transaction
    suspend fun getStation(stationCode: String): Station? {
        val stationEntity = getStationEntity(stationCode) ?: return null
        return mapStationEntityToStation(stationEntity)
    }

    @Transaction
    @RawQuery
    suspend fun queryMultipleStationEntity(query: SupportSQLiteQuery): List<StationEntity>

    @Query("DELETE FROM StationEntity")
    suspend fun deleteAll()

    @Query("SELECT id FROM TrafficTypeEntity WHERE trafficType = :trafficType")
    suspend fun getTrafficTypeEntityId(trafficType: TrafficType): Long?

    @Query("INSERT INTO TrafficTypeEntity (trafficType) VALUES (:trafficType)")
    suspend fun insertTrafficType(trafficType: TrafficType): Long

    @Insert
    suspend fun insert(stationTrafficTypeCrossRef: StationTrafficTypeCrossRef)

    @Query("SELECT * FROM TrafficTypeEntity INNER JOIN StationTrafficTypeCrossRef ON TrafficTypeEntity.id = StationTrafficTypeCrossRef.trafficTypeId WHERE StationTrafficTypeCrossRef.stationCode = :stationCode")
    @RewriteQueriesToDropUnusedColumns
    suspend fun getTrafficTypesForStation(stationCode: String): List<TrafficTypeEntity>

    @Insert
    suspend fun insert(locationEntity: LocationEntity)

    @Query("SELECT * FROM LocationEntity WHERE stationCode = :stationCode")
    suspend fun getLocationForStation(stationCode: String): LocationEntity?

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

            if (station.location != null) {
                val locationEntity = LocationEntity(
                    stationCode = station.code,
                    latitude = station.location.latitude,
                    longitude = station.location.longitude,
                )
                insert(locationEntity)
            }
        }
    }

    suspend fun mapStationEntityToStation(stationEntity: StationEntity): Station {
        val trafficTypes = getTrafficTypesForStation(stationEntity.code).map { it.trafficType }
        val location = StationMapper.mapLocation(getLocationForStation(stationEntity.code))
        return Station(
            code = stationEntity.code,
            longName = stationEntity.longName,
            shortName = stationEntity.shortName,
            commuterNetwork = stationEntity.commuterNetwork,
            trafficTypes = trafficTypes,
            location = location,
        )
    }

    @Transaction
    suspend fun searchStations(searchString: String): List<Station> {
        val parts = AdifNormalizer.normalize(searchString).split("\\s+".toRegex())

        var queryString = "SELECT * FROM StationEntity WHERE ("
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

        val stationEntities = queryMultipleStationEntity(query)
        return stationEntities.map { mapStationEntityToStation(it) }
    }
}