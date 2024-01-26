package me.iscle.adifunofficial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private enum class NavigationBarItems(
    val icon: ImageVector,
    val label: String,
) {
    HOME(Icons.Filled.Home, "Inicio"),
    STATIONS(Icons.Filled.LocationCity, "Estaciones"),
    FAVORITES(Icons.Filled.Favorite, "Favoritos"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUi() {
    val scope = rememberCoroutineScope()
    var selectedNavigationBarItem by remember { mutableStateOf(NavigationBarItems.HOME) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = selectedNavigationBarItem.label)
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItems.entries.forEach {
                    NavigationBarItem(
                        selected = selectedNavigationBarItem == it,
                        onClick = { selectedNavigationBarItem = it },
                        icon = {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = it.label
                            )
                        },
                        label = {
                            Text(text = it.label)
                        }
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                StationsCard(
                    modifier = Modifier.fillMaxWidth(),
                    onShowSnackbar = { message ->
                        scope.launch {
                            if (snackbarHostState.currentSnackbarData?.visuals?.message != message) {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    }
                )
            }

            item {
                TrainBetweenStationsCard(
                    modifier = Modifier.fillMaxWidth(),
                    onShowSnackbar = { message ->
                        scope.launch {
                            if (snackbarHostState.currentSnackbarData?.visuals?.message != message) {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    }
                )
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = "Busquedas recientes",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        
                        RecentArrivalCard(
                            stationName = "Madrid",
                            onClick = {},
                        )

                        RecentDepartureCard(
                            stationName = "Madrid",
                            onClick = {},
                        )

                        RecentBetweenStationsCard(
                            origin = "Madrid",
                            destination = "Barcelona",
                            onClick = {},
                        )

                        RecentBetweenStationsCard(
                            origin = "Barcelona",
                            destination = "Granollers",
                            onClick = {},
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainUiPreview() {
    MainUi()
}