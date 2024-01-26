package me.iscle.adifunofficial

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.iscle.adifunofficial.station.StationRepository
import javax.inject.Inject

@HiltViewModel
class SearchModalBottomSheetViewModel @Inject constructor(
    private val stationRepository: StationRepository,
) : ViewModel() {

    suspend fun searchStations(query: String) = stationRepository.search(query)
}