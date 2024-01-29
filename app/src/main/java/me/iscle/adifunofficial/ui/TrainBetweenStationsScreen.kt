package me.iscle.adifunofficial.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType
import me.iscle.adifunofficial.station.model.Station
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
    LaunchedEffect(originStation, destinationStation, capturedSelectedTrafficType) {
        if (capturedSelectedTrafficType == null) return@LaunchedEffect
        Log.d(TAG, "TrainBetweenStationsScreen: Using traffic type $capturedSelectedTrafficType")
        viewModel.getTrainsBetweenStations(0, capturedSelectedTrafficType)
    }

    TrainBetweenStationsUi(
        onBack = onBack,
        onSelectedStationsChanged = viewModel::setStations,
        originStation = originStation,
        destinationStation = destinationStation,
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
    onSelectedStationsChanged: (origin: Station, destination: Station) -> Unit,
    originStation: Station,
    destinationStation: Station,
    trafficTypes: List<TrafficType>,
    selectedTrafficType: TrafficType?,
    onSelectedTrafficTypeChanged: (TrafficType) -> Unit,
    trainsBetweenStations: List<BetweenStationsRowData>,
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
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    SearchableStationOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Origen",
                        selectedStation = originStation,
                        onStationSelected = {
                            onSelectedStationsChanged(it, destinationStation)
                        }
                    )
                    SearchableStationOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Destino",
                        selectedStation = destinationStation,
                        onStationSelected = {
                            onSelectedStationsChanged(originStation, it)
                        }
                    )
                }
                IconButton(onClick = {
                    onSelectedStationsChanged(destinationStation, originStation)
                }) {
                    Icon(
                        imageVector = Icons.Filled.SwapVert,
                        contentDescription = "Swap",
                    )
                }
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

            var maxTimeColumnWidth by remember { mutableIntStateOf(0) }
            var maxLineColumnWidth by remember { mutableIntStateOf(0) }
            var maxPlatformColumnWidth by remember { mutableIntStateOf(0) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Hora",
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        maxTimeColumnWidth = maxOf(maxTimeColumnWidth, placeable.width)
                        layout(maxTimeColumnWidth, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    },
                )

                Text(
                    text = "Destino",
                    modifier = Modifier.weight(1f),
                )

                Text(
                    text = "Línea",
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        maxLineColumnWidth = maxOf(maxLineColumnWidth, placeable.width)
                        layout(maxLineColumnWidth, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    },
                )

                Text(
                    text = "Vía",
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        maxPlatformColumnWidth = maxOf(maxPlatformColumnWidth, placeable.width)
                        layout(maxPlatformColumnWidth, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    },
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                itemsIndexed(trainsBetweenStations) { index, item ->
                    Column {
                        if (index != 0) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val originTimeFormatted = timeFormat.format(Date(item.originTime))
                            val destinationTimeFormatted = timeFormat.format(Date(item.destinationTime))

                            Text(
                                text = "$originTimeFormatted - $destinationTimeFormatted",
                                modifier = Modifier.layout { measurable, constraints ->
                                    val placeable = measurable.measure(constraints)
                                    maxTimeColumnWidth = maxOf(maxTimeColumnWidth, placeable.width)
                                    layout(placeable.width, placeable.height) {
                                        placeable.placeRelative(0, 0)
                                    }
                                },
                            )

                            Text(
                                text = item.destinationName,
                                modifier = Modifier.weight(1f),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )

                            Text(
                                text = item.line,
                                modifier = Modifier.layout { measurable, constraints ->
                                    val placeable = measurable.measure(constraints)
                                    maxLineColumnWidth = maxOf(maxLineColumnWidth, placeable.width)
                                    layout(maxLineColumnWidth, placeable.height) {
                                        placeable.placeRelative(0, 0)
                                    }
                                },
                            )

                            Text(
                                text = item.originPlatform,
                                modifier = Modifier.layout { measurable, constraints ->
                                    val placeable = measurable.measure(constraints)
                                    maxPlatformColumnWidth = maxOf(maxPlatformColumnWidth, placeable.width)
                                    layout(maxPlatformColumnWidth, placeable.height) {
                                        placeable.placeRelative(0, 0)
                                    }
                                },
                            )
                        }
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
        onSelectedStationsChanged = { _, _ -> },
        originStation = Station(
            code = "123",
            longName = "Barcelona Sants",
            shortName = "Sants",
            commuterNetwork = null,
            trafficTypes = listOf(
                TrafficType.CERCANIAS,
                TrafficType.AVLDMD,
            ),
            location = null,
        ),
        destinationStation = Station(
            code = "123",
            longName = "Barcelona Plaça Catalunya",
            shortName = "Plaça Catalunya",
            commuterNetwork = null,
            trafficTypes = listOf(
                TrafficType.CERCANIAS,
                TrafficType.AVLDMD,
            ),
            location = null,
        ),
        trafficTypes = listOf(
            TrafficType.CERCANIAS,
            TrafficType.AVLDMD,
        ),
        selectedTrafficType = TrafficType.CERCANIAS,
        onSelectedTrafficTypeChanged = {},
        trainsBetweenStations = listOf(
            BetweenStationsRowData(
                originTime = System.currentTimeMillis(),
                destinationTime = System.currentTimeMillis() + 1000 * 60 * 60 * 2,
                destinationName = "Plaça Catalunya",
                line = "R3",
                originPlatform = "1",
            ),
            BetweenStationsRowData(
                originTime = System.currentTimeMillis(),
                destinationTime = System.currentTimeMillis() + 2000 * 60 * 60 * 2,
                destinationName = "Barcelona Sants",
                line = "R3",
                originPlatform = "1",
            ),
            BetweenStationsRowData(
                originTime = System.currentTimeMillis(),
                destinationTime = System.currentTimeMillis() + 3000 * 60 * 60 * 2,
                destinationName = "Granollers Centre",
                line = "R3",
                originPlatform = "1",
            ),
        ),
        isLoading = false,
    )
}

