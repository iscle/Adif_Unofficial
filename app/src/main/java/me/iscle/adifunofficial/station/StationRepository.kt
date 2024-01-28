package me.iscle.adifunofficial.station

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.sqlite.db.SimpleSQLiteQuery
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import me.iscle.adifunofficial.elcano.stations.network.StationService
import me.iscle.adifunofficial.station.entity.StationEntity
import me.iscle.adifunofficial.util.AdifNormalizer
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

private const val TAG = "StationRepository"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_repository")
private val LAST_UPDATE = longPreferencesKey("last_update")
private val TOKEN_KEY = stringPreferencesKey("token")

class StationRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val stationService: StationService,
    private val stationDao: StationDao,
) {
    private val dataStore = context.dataStore

    suspend fun updateStations(): Boolean {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "updateStations: Starting stations update!")

            var lastUpdate = 0L
            var token = "0"

            dataStore.data.first().let { preferences ->
                lastUpdate = preferences[LAST_UPDATE] ?: 0
                token = preferences[TOKEN_KEY] ?: "0"
            }

            val now = System.currentTimeMillis()
            if (now - lastUpdate >= 24L.hours.inWholeMilliseconds) {
                try {
                    Log.d(TAG, "updateStations: Requesting new stations...")
                    val response = stationService.getStations(token)
                    if (response.token != null) {
                        dataStore.edit { preferences ->
                            preferences[TOKEN_KEY] = response.token
                        }
                    }

                    val stations = StationMapper.mapRequestedStationInfoListToStationList(response.requestedStationInfoList)
                    if (stations != null) {
                        Log.d(TAG, "updateStations: Got ${stations.size} stations")
                        dataStore.edit { preferences ->
                            preferences[LAST_UPDATE] = now
                        }
                        stationDao.deleteAll()
                        stationDao.insertAll(stations)
                    } else {
                        Log.w(TAG, "updateStations: The server returned an empty response!")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "updateStations: Failed to request new stations", e)
                    return@withContext false
                }
            } else {
                Log.d(TAG, "updateStations: Skipping update, db is recent!")
            }

            true
        }
    }

    suspend fun search(
        searchString: String,
    ): List<StationEntity> {
        return withContext(Dispatchers.IO) {
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
        }
    }
}