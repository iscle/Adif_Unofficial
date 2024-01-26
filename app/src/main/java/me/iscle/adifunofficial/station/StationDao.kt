package me.iscle.adifunofficial.station

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.iscle.adifunofficial.station.model.Station

@Dao
interface StationDao {
    @Query("SELECT * FROM Station")
    suspend fun getAll(): List<Station>

    @Insert
    suspend fun insertAll(stations: List<Station>)

    @Insert
    suspend fun insert(station: Station)

    @Query("SELECT * FROM Station " +
            "WHERE " +
            "normalizedLongName LIKE '%' || :query || '%' OR " +
            "normalizedShortName LIKE '%' || :query || '%' OR " +
            "code LIKE '%' || :query || '%' ")
    suspend fun search(query: String): List<Station>

    @Query("DELETE FROM Station")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(station: Station)
}