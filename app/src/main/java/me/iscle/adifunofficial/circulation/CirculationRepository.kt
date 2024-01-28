package me.iscle.adifunofficial.circulation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.iscle.adifunofficial.circulation.model.BetweenStationsInfo
import me.iscle.adifunofficial.elcano.circulation.model.CirculationPathRequest
import me.iscle.adifunofficial.elcano.circulation.model.TrafficCirculationPathRequest
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType
import me.iscle.adifunofficial.elcano.circulation.network.CirculationService
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "circulation_repository")

class CirculationRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val circulationService: CirculationService,
) {
    private val dataStore = context.dataStore

    suspend fun getTrainsBetweenStations(
        originStation: String,
        destinationStation: String,
        pageNumber: Int,
        trafficType: TrafficType,
    ): List<BetweenStationsInfo> {
        return withContext(Dispatchers.IO) {
            val request = TrafficCirculationPathRequest(
                commercialService = CirculationPathRequest.State.YES, // seems to be always YES
                commercialStopType = CirculationPathRequest.State.YES, // seems to be always YES
                destinationStationCode = destinationStation,
                originStationCode = originStation,
                page = CirculationPathRequest.PageInfoDTO(
                    pageNumber = pageNumber,
                ),
                stationCode = null,
                trafficType = trafficType,
            )

            val response = circulationService.betweenStations(request)
            val commercialPaths = response.commercialPaths
            if (commercialPaths != null) {
                CirculationMapper.mapBetweenStations(commercialPaths)
            } else {
                emptyList()
            }
        }
    }

    suspend fun getArrivals(
        station: String,
        pageNumber: Int,
        trafficType: TrafficType,
    ) {
        val request = TrafficCirculationPathRequest(
            commercialService = CirculationPathRequest.State.YES,
            commercialStopType = CirculationPathRequest.State.YES,
            destinationStationCode = null,
            originStationCode = null,
            page = CirculationPathRequest.PageInfoDTO(
                pageNumber = pageNumber,
            ),
            stationCode = station,
            trafficType = trafficType,
        )
    }

    suspend fun getDepartures(
        station: String,
        pageNumber: Int,
        trafficType: TrafficType,
    ) {

    }
}