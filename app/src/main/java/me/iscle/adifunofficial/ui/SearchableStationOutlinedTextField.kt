package me.iscle.adifunofficial.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import me.iscle.adifunofficial.station.model.Station

@Composable
fun SearchableStationOutlinedTextField(
    modifier: Modifier = Modifier,
    label: String,
    selectedStation: Station?,
    onStationSelected: (Station) -> Unit,
) {
    var showStationSearch by remember { mutableStateOf(false) }

    if (showStationSearch) {
        SearchModalBottomSheet(
            onDismissRequest = { showStationSearch = false },
            station = selectedStation,
            onStationSelected = onStationSelected,
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
        value = selectedStation?.longName ?: "",
        onValueChange = {},
        modifier = modifier,
        readOnly = true,
        label = { Text(text = label) },
        interactionSource = sourceInteractionSource,
    )
}

@Preview
@Composable
fun SearchableStationOutlinedTextFieldPreview() {
    SearchableStationOutlinedTextField(
        label = "Estación",
        selectedStation = null,
        onStationSelected = {},
    )
}