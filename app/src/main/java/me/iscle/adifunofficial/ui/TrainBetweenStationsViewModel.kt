package me.iscle.adifunofficial.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.iscle.adifunofficial.circulation.CirculationRepository
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType
import me.iscle.adifunofficial.station.StationRepository
import me.iscle.adifunofficial.station.model.Station
import javax.inject.Inject

private const val TAG = "TrainBetweenStationsVie"

@HiltViewModel
class TrainBetweenStationsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stationRepository: StationRepository,
    private val circulationRepository: CirculationRepository,
) : ViewModel() {
    private var originStationCode = savedStateHandle.get<String>("originStation")!!
    private var destinationStationCode = savedStateHandle.get<String>("destinationStation")!!

    private val _originStation = MutableStateFlow(Station.EMPTY_STATION)
    val originStation: StateFlow<Station>
        get() = _originStation

    private val _destinationStation = MutableStateFlow(Station.EMPTY_STATION)
    val destinationStation: StateFlow<Station>
        get() = _destinationStation

    private val _trafficTypes = MutableStateFlow<List<TrafficType>>(emptyList())
    val trafficTypes: StateFlow<List<TrafficType>>
        get() = _trafficTypes

    private val _trafficType = MutableStateFlow<TrafficType?>(null)
    val trafficType: StateFlow<TrafficType?>
        get() = _trafficType

    private val _trainsBetweenStations = MutableStateFlow<List<BetweenStationsRowData>>(emptyList())
    val trainsBetweenStations: StateFlow<List<BetweenStationsRowData>>
        get() = _trainsBetweenStations

    private var isLoadingMore = false
    private var canLoadMore = true
    private var nextPage = 0

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchStations()
            fetchTrains()
        }
    }

    private suspend fun fetchStations() {
        withContext(Dispatchers.IO) {
            // invalidate current results
            _originStation.value = Station.EMPTY_STATION
            _destinationStation.value = Station.EMPTY_STATION
            _trafficTypes.value = emptyList()
            _trafficType.value = null
            clearTrains()

            // fetch new results
            val originStation = stationRepository.getStation(originStationCode)
            val destinationStation = stationRepository.getStation(destinationStationCode)
            if (originStation == null || destinationStation == null) throw IllegalStateException("Stations not found")

            val trafficTypes =
                originStation.trafficTypes.filter { destinationStation.trafficTypes.contains(it) }

            Log.d(TAG, "init: emitting values")
            _originStation.value = originStation
            _destinationStation.value = destinationStation
            _trafficTypes.value = trafficTypes
            _trafficType.value = trafficTypes.firstOrNull()
        }
    }

    private suspend fun fetchTrains() {
        if (!canLoadMore) return
        withContext(Dispatchers.IO) {
            val trafficType = trafficType.value ?: return@withContext
            val trainsBetweenStations = circulationRepository.getTrainsBetweenStations(
                originStation = originStationCode,
                destinationStation = destinationStationCode,
                pageNumber = nextPage,
                trafficType = trafficType,
            )

            if (trainsBetweenStations.isEmpty()) {
                // TODO: Check that the response was actually successful and not an error
                canLoadMore = false
                return@withContext
            }

            val newList = mutableListOf<BetweenStationsRowData>()
            newList.addAll(_trainsBetweenStations.value)
            newList.addAll(trainsBetweenStations.map {
                BetweenStationsRowData(
                    originTime = it.originStopInfo.plannedTime,
                    destinationTime = it.destinationStopInfo.plannedTime,
                    destinationName = stationRepository.getStation(it.routeInfo.destinationStation)?.shortName ?: it.routeInfo.destinationStation,
                    line = it.routeInfo.line ?: "",
                    originPlatform = it.originStopInfo.platform ?: "",
                )
            })

            _trainsBetweenStations.value = newList.sortedBy { it.originTime }
        }
    }

    private fun clearTrains() {
        _trainsBetweenStations.value = emptyList()
        nextPage = 0
    }

    fun setStations(originStation: Station, destinationStation: Station) {
        originStationCode = originStation.code
        destinationStationCode = destinationStation.code
        fetchData()
    }

    fun setTrafficType(trafficType: TrafficType) {
        _trafficType.value = trafficType
        clearTrains()
        viewModelScope.launch { fetchTrains() }
    }

    fun loadMore() {
        if (isLoadingMore) return
        isLoadingMore = true
        nextPage++
        viewModelScope.launch { fetchTrains() }.invokeOnCompletion { isLoadingMore = false}
    }
}