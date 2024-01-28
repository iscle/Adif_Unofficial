package me.iscle.adifunofficial.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import me.iscle.adifunofficial.station.entity.StationEntity
import me.iscle.adifunofficial.ui.component.FixedModalBottomSheet

private const val TAG = "SearchModalBottomSheet"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchModalBottomSheet(
    viewModel: SearchModalBottomSheetViewModel = hiltViewModel(),
    onDismissed: () -> Unit,
    onStationSelected: (StationEntity) -> Unit,
    defaultStationEntity: StationEntity? = null,
) {
    val density = LocalDensity.current
    val sheetState = remember(density) {
        SheetState(
            skipPartiallyExpanded = true,
            density = density,
        )
    }

    FixedModalBottomSheet(
        onDismissRequest = onDismissed,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val scope = rememberCoroutineScope()
            var results by remember { mutableStateOf(emptyList<StationEntity>()) }
            var query by remember(defaultStationEntity) {
                val defaultStationName = defaultStationEntity?.longName ?: ""
                mutableStateOf(TextFieldValue(defaultStationName, TextRange(defaultStationName.length, defaultStationName.length)))
            }
            LaunchedEffect(query) {
                results = if (query.text.isNotBlank()) {
                    viewModel.searchStations(query.text)
                } else {
                    emptyList()
                }
            }

            val focusRequester = remember { FocusRequester() }
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
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
                items(results) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clickable {
                                onStationSelected(it)
                                scope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion { onDismissed() }
                            },
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = it.longName)
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

@Preview
@Composable
fun SearchModalBottomSheetPreview() {
    SearchModalBottomSheet(
        onDismissed = {},
        onStationSelected = {},
        defaultStationEntity = StationEntity(
            longName = "Sant Andreu Arenal",
            normalizedLongName = "Sant Andreu Arenal",
            shortName = "Sant Andreu Arenal",
            normalizedShortName = "Sant Andreu Arenal",
            code = "SAA",
            location = null,
            commuterNetwork = CommuterNetwork.RODALIES_CATALUNYA,
            trafficTypes = emptyList(),
        ),
    )
}