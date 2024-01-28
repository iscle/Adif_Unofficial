package me.iscle.adifunofficial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.iscle.adifunofficial.station.StationRepository
import me.iscle.adifunofficial.ui.AdifNavHost
import me.iscle.adifunofficial.ui.theme.AdifUnofficialTheme
import javax.inject.Inject

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject protected lateinit var stationRepository: StationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdifUnofficialTheme {
                val navController = rememberNavController()
                AdifNavHost(navController = navController)
            }
        }

        lifecycleScope.launch {
            stationRepository.updateStations()
        }
    }
}