package me.iscle.adifunofficial.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.iscle.adifunofficial.station.model.Station
import me.iscle.adifunofficial.ui.SearchModalBottomSheet

@Composable
fun StationsCard(
    modifier: Modifier = Modifier,
    onShowSnackbar: (text: String) -> Unit,
    onViewDepartures: (station: Station) -> Unit,
    onViewArrivals: (station: Station) -> Unit,
) {
    ElevatedCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Estaciones",
                style = MaterialTheme.typography.titleLarge,
            )

            var showStationSearch by remember { mutableStateOf(false) }
            var station by remember { mutableStateOf<Station?>(null) }

            if (showStationSearch) {
                SearchModalBottomSheet(
                    onDismissRequest = {
                        showStationSearch = false
                    },
                    onStationSelected = {
                        station = it
                    },
                    defaultStation = station,
                )
            }

            val sourceInteractionSource = remember { MutableInteractionSource() }
            LaunchedEffect(sourceInteractionSource) {
                sourceInteractionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        showStationSearch = true
                    }
                }
            }
            OutlinedTextField(
                value = station?.longName ?: "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text(text = "Estación") },
                interactionSource = sourceInteractionSource,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = {
                        val selectedStation = station
                        if (selectedStation == null) {
                            onShowSnackbar("Selecciona una estación")
                        } else {
                            onViewDepartures(selectedStation)
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = "Ver salidas")
                }

                Button(
                    onClick = {
                        val selectedStation = station
                        if (selectedStation == null) {
                            onShowSnackbar("Selecciona una estación")
                        } else {
                            onViewArrivals(selectedStation)
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = "Ver llegadas")
                }
            }
        }
    }
}

@Preview
@Composable
fun StationsCardPreview() {
    StationsCard(
        onShowSnackbar = {},
        onViewArrivals = {},
        onViewDepartures = {},
    )
}