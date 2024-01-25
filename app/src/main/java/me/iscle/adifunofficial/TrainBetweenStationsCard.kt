package me.iscle.adifunofficial

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

@Composable
fun TrainBetweenStationsCard(
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit,
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

            var origin by remember { mutableStateOf("") }
            val sourceInteractionSource = remember { MutableInteractionSource() }
            LaunchedEffect(sourceInteractionSource) {
                sourceInteractionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        origin = "Origin tapped!"
                    }
                }
            }
            OutlinedTextField(
                value = origin,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text(text = "Origen") },
                interactionSource = sourceInteractionSource,
            )

            var destination by remember { mutableStateOf("") }
            val destinationInteractionSource = remember { MutableInteractionSource() }
            LaunchedEffect(destinationInteractionSource) {
                destinationInteractionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        destination = "Destination tapped!"
                    }
                }
            }
            OutlinedTextField(
                value = destination,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text(text = "Destino") },
                interactionSource = destinationInteractionSource,
            )

            Button(
                onClick = {
                          if (origin.isEmpty() && destination.isEmpty()) {
                              onShowSnackbar("Selecciona una estación de origen y destino")
                          } else if (origin.isEmpty()) {
                              onShowSnackbar("Selecciona una estación de origen")
                          } else if (destination.isEmpty()) {
                              onShowSnackbar("Selecciona una estación de destino")
                          } else {
                              // TODO: Navigate to train between stations
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
    )
}