package me.iscle.adifunofficial

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.iscle.adifunofficial.elcano.stations.network.StationsService
import me.iscle.adifunofficial.ui.theme.AdifUnofficialTheme
import javax.inject.Inject

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject protected lateinit var stationsService: StationsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdifUnofficialTheme {
                MainUi()
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val response = stationsService.stations("0")
            Log.d(TAG, "onCreate: $response")
            response.requestedStationInfoList?.mapNotNull { it.stationInfo }?.sortedBy { it.longName }?.forEach {
                Log.d(TAG, "onCreate: ${it.shortName}, ${it.longName}")
            }
        }
    }
}