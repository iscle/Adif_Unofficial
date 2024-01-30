package me.iscle.adifunofficial.station

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import me.iscle.adifunofficial.elcano.stations.network.StationService
import me.iscle.adifunofficial.station.model.Station
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
    private val cachedStations = mutableMapOf<String, Station>()

    suspend fun updateStations(): Boolean {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "updateStations: Starting stations update!")

            var lastUpdate: Long
            var token: String
            dataStore.data.first().let { preferences ->
                lastUpdate = preferences[LAST_UPDATE] ?: 0
                token = preferences[TOKEN_KEY] ?: "0"
            }

            val now = System.currentTimeMillis()
            if (now - lastUpdate >= 24L.hours.inWholeMilliseconds) {
                try {
                    Log.d(TAG, "updateStations: Requesting new stations...")
                    val response = stationService.getStations(token)
                    val body = if (response.isSuccessful) response.body() else null
                    if (body == null) {
                        Log.w(TAG, "updateStations: The server returned a null response!")
                        return@withContext false
                    }

                    if (body.token != null) {
                        dataStore.edit { preferences ->
                            preferences[TOKEN_KEY] = body.token
                        }
                    }

                    val stations = StationMapper.mapRequestedStationInfoListToStationList(body.requestedStationInfoList)
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

    suspend fun getStation(
        stationCode: String,
    ): Station? {
        if (cachedStations.containsKey(stationCode)) {
            return cachedStations[stationCode]
        }

        val station = stationDao.getStation(stationCode)
        if (station != null) {
            cachedStations[stationCode] = station
        }
        return station
    }

    suspend fun search(
        searchString: String,
    ) = stationDao.searchStations(searchString)
}