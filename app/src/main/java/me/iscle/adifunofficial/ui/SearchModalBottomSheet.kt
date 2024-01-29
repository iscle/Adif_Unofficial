package me.iscle.adifunofficial.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import me.iscle.adifunofficial.elcano.circulation.model.CommuterNetwork
import me.iscle.adifunofficial.elcano.circulation.model.TrafficType
import me.iscle.adifunofficial.station.model.Station
import me.iscle.adifunofficial.ui.component.FixedModalBottomSheet

private const val TAG = "SearchModalBottomSheet"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchModalBottomSheet(
    viewModel: SearchModalBottomSheetViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
    onStationSelected: (Station) -> Unit,
    defaultStation: Station? = null,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val sheetState = remember(density) {
        SheetState(
            skipPartiallyExpanded = true,
            density = density,
        )
    }

    var results by remember { mutableStateOf(emptyList<Station>()) }
    var query by remember(defaultStation) {
        val defaultStationName = defaultStation?.longName ?: ""
        mutableStateOf(TextFieldValue(defaultStationName, TextRange(defaultStationName.length, defaultStationName.length)))
    }

    LaunchedEffect(query) {
        results = if (query.text.isBlank()) {
            emptyList()
        } else {
            viewModel.searchStations(query.text)
        }
    }

    SearchModalBottomSheetInternal(
        state = sheetState,
        query = query,
        onQueryChange = { query = it },
        results = results,
        onStationSelected = {
            onStationSelected(it)
            scope
                .launch { sheetState.hide() }
                .invokeOnCompletion { onDismissRequest() }
        },
        onDismissRequest = onDismissRequest,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchModalBottomSheetInternal(
    state: SheetState,
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    results: List<Station>,
    onStationSelected: (Station) -> Unit,
    onDismissRequest: () -> Unit,
) {
    FixedModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = state,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val focusRequester = remember { FocusRequester() }
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
                label = { Text(text = "Buscar") },
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(results) { station ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onStationSelected(station)
                            },
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Text(
                                text = station.longName,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = station.trafficTypes.joinToString(" | ") {
                                    it.prettyName(station.commuterNetwork)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchModalBottomSheetPreview() {
    SearchModalBottomSheetInternal(
        state = SheetState(
            skipPartiallyExpanded = true,
            density = LocalDensity.current,
            initialValue = SheetValue.Expanded,
        ),
        query = TextFieldValue("grano"),
        onQueryChange = {},
        results = listOf(
            Station(
                longName = "Sant Andreu Arenal",
                shortName = "Sant Andreu Arenal",
                code = "SAA",
                location = null,
                commuterNetwork = CommuterNetwork.RODALIES_CATALUNYA,
                trafficTypes = listOf(TrafficType.CERCANIAS, TrafficType.AVLDMD),
            ),
            Station(
                longName = "Sant Andreu Comtal",
                shortName = "Sant Andreu Comtal",
                code = "SAC",
                location = null,
                commuterNetwork = CommuterNetwork.RODALIES_CATALUNYA,
                trafficTypes = listOf(TrafficType.CERCANIAS),
            ),
            Station(
                longName = "Sant Andreu Condal",
                shortName = "Sant Andreu Condal",
                code = "SAD",
                location = null,
                commuterNetwork = CommuterNetwork.RODALIES_CATALUNYA,
                trafficTypes = listOf(TrafficType.AVLDMD),
            ),
        ),
        onStationSelected = {},
        onDismissRequest = {},
    )
}