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
import me.iscle.adifunofficial.circulation.CirculationRepository
import me.iscle.adifunofficial.circulation.model.BetweenStationsInfo
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
    private val originStationCode = savedStateHandle.get<String>("originStation")!!
    private val destinationStationCode = savedStateHandle.get<String>("destinationStation")!!

    private val _originStation = MutableStateFlow(Station.EMPTY_STATION)
    val originStation: StateFlow<Station>
        get() = _originStation

    private val _destinationStation = MutableStateFlow(Station.EMPTY_STATION)
    val destinationStation: StateFlow<Station>
        get() = _destinationStation

    private val _trafficTypes = MutableStateFlow<List<TrafficType>>(emptyList())
    val trafficTypes: StateFlow<List<TrafficType>>
        get() = _trafficTypes

    private val _trainsBetweenStations = MutableStateFlow<List<BetweenStationsInfo>>(emptyList())
    val trainsBetweenStations: StateFlow<List<BetweenStationsInfo>>
        get() = _trainsBetweenStations

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val originStation = stationRepository.getStation(originStationCode)
            val destinationStation = stationRepository.getStation(destinationStationCode)
            if (originStation == null || destinationStation == null) throw IllegalStateException("Stations not found")

            val trafficTypes = originStation.trafficTypes.filter { destinationStation.trafficTypes.contains(it) }

            Log.d(TAG, "init: emitting values")
            _originStation.emit(originStation)
            _destinationStation.emit(destinationStation)
            _trafficTypes.emit(trafficTypes)
        }
    }

    suspend fun getTrainsBetweenStations(
        pageNumber: Int,
        trafficType: TrafficType,
    ) {
        val trainsBetweenStations = circulationRepository.getTrainsBetweenStations(
            originStation = originStationCode,
            destinationStation = destinationStationCode,
            pageNumber = pageNumber,
            trafficType = trafficType,
        )

        _trainsBetweenStations.emit(trainsBetweenStations)
    }
}