package me.iscle.adifunofficial.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import me.iscle.adifunofficial.station.entity.StationEntity
import me.iscle.adifunofficial.ui.SearchModalBottomSheet

@Composable
fun TrainBetweenStationsCard(
    modifier: Modifier = Modifier,
    onShowSnackbar: (text: String) -> Unit,
    onSearchTrains: (origin: StationEntity, destination: StationEntity) -> Unit,
) {
    ElevatedCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Trenes entre estaciones",
                style = MaterialTheme.typography.titleLarge,
            )

            var showOriginStationSearch by remember { mutableStateOf(false) }
            var originStationEntity by remember { mutableStateOf<StationEntity?>(null) }

            if (showOriginStationSearch) {
                SearchModalBottomSheet(
                    onDismissed = { showOriginStationSearch = false },
                    onStationSelected = { originStationEntity = it },
                    defaultStationEntity = originStationEntity,
                )
            }

            val sourceInteractionSource = remember { MutableInteractionSource() }
            LaunchedEffect(sourceInteractionSource) {
                sourceInteractionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        showOriginStationSearch = true
                    }
                }
            }
            OutlinedTextField(
                value = originStationEntity?.longName ?: "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text(text = "Origen") },
                interactionSource = sourceInteractionSource,
            )

            var showDestinationStationSearch by remember { mutableStateOf(false) }
            var destinationStationEntity by remember { mutableStateOf<StationEntity?>(null) }

            if (showDestinationStationSearch) {
                SearchModalBottomSheet(
                    onDismissed = { showDestinationStationSearch = false },
                    onStationSelected = { destinationStationEntity = it },
                    defaultStationEntity = destinationStationEntity,
                )
            }

            val destinationInteractionSource = remember { MutableInteractionSource() }
            LaunchedEffect(destinationInteractionSource) {
                destinationInteractionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        showDestinationStationSearch = true
                    }
                }
            }
            OutlinedTextField(
                value = destinationStationEntity?.longName ?: "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text(text = "Destino") },
                interactionSource = destinationInteractionSource,
            )

            Button(
                onClick = {
                    val selectedOriginStation = originStationEntity
                    val selectedDestinationStation = destinationStationEntity
                    if (selectedOriginStation == null && selectedDestinationStation == null) {
                        onShowSnackbar("Selecciona una estación de origen y destino")
                    } else if (selectedOriginStation == null) {
                        onShowSnackbar("Selecciona una estación de origen")
                    } else if (selectedDestinationStation == null) {
                        onShowSnackbar("Selecciona una estación de destino")
                    } else {
                        onSearchTrains(selectedOriginStation, selectedDestinationStation)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Buscar trenes")
            }
        }
    }
}

@Preview
@Composable
fun TrainBetweenStationsCardPreview() {
    TrainBetweenStationsCard(
        onShowSnackbar = {},
        onSearchTrains = { _, _ -> }
    )
}