package me.iscle.adifunofficial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import me.iscle.adifunofficial.station.model.Station

private const val TAG = "SearchModalBottomSheet"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchModalBottomSheet(
    viewModel: SearchModalBottomSheetViewModel = viewModel(),
    onDismissRequest: () -> Unit,
    defaultQuery: String = "",
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

    FixedModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            var query by remember(defaultQuery) { mutableStateOf(defaultQuery) }
            val focusRequester = remember { FocusRequester() }
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    if (it.isNotBlank()) {
                        scope.launch {
                            results = viewModel.searchStations(query)
                        }
                    } else {
                        results = emptyList()
                    }
                },
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
            ) {
                items(results) {
                    Text(text = it.longName)
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
        onDismissRequest = {},
        defaultQuery = "Madrid",
    )
}