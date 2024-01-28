package me.iscle.adifunofficial.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import me.iscle.adifunofficial.circulation.CirculationRepository
import me.iscle.adifunofficial.circulation.model.BetweenStationsInfo
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType
import javax.inject.Inject

class TrainBetweenStationsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val circulationRepository: CirculationRepository,
) : ViewModel() {
    val originStation = savedStateHandle.get<String>("originStation")!!
    val destinationStation = savedStateHandle.get<String>("destinationStation")!!
    val trafficTypes = savedStateHandle.get<Array<TrafficType>>("trafficTypes")!!

    private val _trainsBetweenStations = MutableStateFlow<List<BetweenStationsInfo>>(emptyList())
    val trainsBetweenStations: MutableStateFlow<List<BetweenStationsInfo>>
        get() = _trainsBetweenStations

    suspend fun getTrainsBetweenStations(
        pageNumber: Int,
        trafficType: TrafficType,
    ) = circulationRepository.getTrainsBetweenStations(originStation, destinationStation, pageNumber, trafficType)
}