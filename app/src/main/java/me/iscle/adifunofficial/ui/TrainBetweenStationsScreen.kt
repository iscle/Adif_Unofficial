package me.iscle.adifunofficial.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

private const val TAG = "TrainBetweenStationsScr"

@Composable
fun TrainBetweenStationsScreen(
    viewModel: TrainBetweenStationsViewModel = hiltViewModel(),
) {
    val trainsBetweenStations by viewModel.trainsBetweenStations.collectAsState()

    LaunchedEffect(Unit) {
        val trafficType = viewModel.trafficTypes.first()
        Log.d(TAG, "TrainBetweenStationsScreen: Using traffic type $trafficType")
        viewModel.getTrainsBetweenStations(0, trafficType)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            items(trainsBetweenStations) {
                Text(text = it.toString())
            }
        }
    }
}