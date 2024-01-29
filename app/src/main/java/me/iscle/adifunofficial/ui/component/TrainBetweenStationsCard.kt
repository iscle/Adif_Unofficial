package me.iscle.adifunofficial.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.iscle.adifunofficial.station.model.Station
import me.iscle.adifunofficial.ui.SearchableStationOutlinedTextField

@Composable
fun TrainBetweenStationsCard(
    modifier: Modifier = Modifier,
    onShowSnackbar: (text: String) -> Unit,
    onSearchTrains: (origin: Station, destination: Station) -> Unit,
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

            var originStation by remember { mutableStateOf<Station?>(null) }
            SearchableStationOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = "Origen",
                selectedStation = originStation,
                onStationSelected = { originStation = it },
            )

            var destinationStation by remember { mutableStateOf<Station?>(null) }
            SearchableStationOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = "Destino",
                selectedStation = destinationStation,
                onStationSelected = { destinationStation = it },
            )

            Button(
                onClick = {
                    val selectedOriginStation = originStation
                    val selectedDestinationStation = destinationStation
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