package me.iscle.adifunofficial.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.iscle.adifunofficial.circulation.model.BetweenStationsInfo
import me.iscle.adifunofficial.circulation.model.RouteInfo
import me.iscle.adifunofficial.circulation.model.StopInfo
import me.iscle.adifunofficial.elcano.circulation.model.CirculationState
import me.iscle.adifunofficial.elcano.circulation.model.TimeType
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "TrainBetweenStationsScr"

@Composable
fun TrainBetweenStationsScreen(
    onBack: () -> Unit,
    viewModel: TrainBetweenStationsViewModel = hiltViewModel(),
) {
    val originStation = viewModel.originStation.collectAsState().value
    val destinationStation = viewModel.destinationStation.collectAsState().value
    val trafficTypes = viewModel.trafficTypes.collectAsState().value
    val trainsBetweenStations = viewModel.trainsBetweenStations.collectAsState().value

    var selectedTrafficType by remember(trafficTypes) { mutableStateOf(trafficTypes.firstOrNull()) }

    val capturedSelectedTrafficType = selectedTrafficType
    LaunchedEffect(capturedSelectedTrafficType) {
        if (capturedSelectedTrafficType == null) return@LaunchedEffect
        Log.d(TAG, "TrainBetweenStationsScreen: Using traffic type $capturedSelectedTrafficType")
        viewModel.getTrainsBetweenStations(0, capturedSelectedTrafficType)
    }

    TrainBetweenStationsUi(
        onBack = onBack,
        originStation = originStation.longName,
        destinationStation = destinationStation.longName,
        trafficTypes = trafficTypes,
        selectedTrafficType = selectedTrafficType,
        onSelectedTrafficTypeChanged = { selectedTrafficType = it },
        trainsBetweenStations = trainsBetweenStations,
        isLoading = false,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainBetweenStationsUi(
    onBack: () -> Unit,
    originStation: String,
    destinationStation: String,
    trafficTypes: List<TrafficType>,
    selectedTrafficType: TrafficType?,
    onSelectedTrafficTypeChanged: (TrafficType) -> Unit,
    trainsBetweenStations: List<BetweenStationsInfo>,
    isLoading: Boolean,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Trenes entre estaciones")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = "De $originStation",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "a $destinationStation",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(trafficTypes) {
                    FilterChip(
                        selected = it == selectedTrafficType,
                        onClick = { onSelectedTrafficTypeChanged(it) },
                        label = { Text(text = it.name) },
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(trainsBetweenStations) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val originTimeFormatted = timeFormat.format(Date(it.originStopInfo.plannedTime))
                        val destinationTimeFormatted = timeFormat.format(Date(it.destinationStopInfo.plannedTime))

                        Text(
                            text = "$originTimeFormatted - $destinationTimeFormatted",
                        )

                        Text(
                            text = it.routeInfo.destinationStation!!,
                        )

                        Text(
                            text = it.routeInfo.line!!,
                        )

                        Text(
                            text = it.originStopInfo.platform!!,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TrainBetweenStationsScreenPreview() {
    TrainBetweenStationsUi(
        onBack = {},
        originStation = "Granollers-Canovelles",
        destinationStation = "Barcelona Plaça Catalunya",
        trafficTypes = listOf(
            TrafficType.CERCANIAS,
            TrafficType.AVLDMD,
        ),
        selectedTrafficType = TrafficType.CERCANIAS,
        onSelectedTrafficTypeChanged = {},
        trainsBetweenStations = listOf(
            BetweenStationsInfo(
                routeInfo = RouteInfo(
                    originStation = null,
                    destinationStation = "Las Vegas",
                    line = "R3",
                    trafficType = null,
                ),
                originStopInfo = StopInfo(
                    plannedTime = System.currentTimeMillis(),
                    delay = 0L,
                    timeType = TimeType.FORECASTED,
                    platform = "1",
                    circulationState = CirculationState.PENDING_TO_CIRCULATE,
                ),
                destinationStopInfo = StopInfo(
                    plannedTime = System.currentTimeMillis(),
                    delay = 0L,
                    timeType = TimeType.FORECASTED,
                    platform = "1",
                    circulationState = CirculationState.PENDING_TO_CIRCULATE,
                ),
            )
        ),
        isLoading = false,
    )
}

