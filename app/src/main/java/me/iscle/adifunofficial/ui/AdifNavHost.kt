package me.iscle.adifunofficial.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    data object Home : Screen("home")
    data object Favorites : Screen("favorites")
    data object DeparturesFromStation : Screen(
        route = "departuresFromStation/{station}",
        arguments = listOf(
            navArgument("station") {
                type = NavType.StringType
            }
        ),
    ) {
        fun createRoute(station: String) = "departuresFromStation/$station"
    }

    data object ArrivalsToStation : Screen(
        route = "arrivalsToStation/{station}",
        arguments = listOf(
            navArgument("station") {
                type = NavType.StringType
            }
        ),
    ) {
        fun createRoute(station: String) = "arrivalsToStation/$station"
    }

    data object TrainBetweenStations : Screen(
        route = "trainBetweenStations/{originStation}/{destinationStation}",
        arguments = listOf(
            navArgument("originStation") {
                type = NavType.StringType
            },
            navArgument("destinationStation") {
                type = NavType.StringType
            },
        ),
    ) {
        fun createRoute(originStation: String, destinationStation: String) =
            "trainBetweenStations/$originStation/$destinationStation"
    }
//    data object TrainDetails : Screen("trainDetails")
//    data object StationDetails : Screen("stationDetails")
}

@Composable
fun AdifNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(
            route = Screen.Home.route,
            arguments = Screen.Home.arguments,
        ) {
            HomeScreen(
                navigateToArrivals = { station ->
                    navController.navigate(Screen.ArrivalsToStation.createRoute(station))
                },
                navigateToDepartures = { station ->
                    navController.navigate(Screen.DeparturesFromStation.createRoute(station))
                },
                navigateToTrainBetweenStations = { origin, destination ->
                    navController.navigate(
                        Screen.TrainBetweenStations.createRoute(
                            origin,
                            destination,
                        )
                    )
                },
            )
        }

        composable(
            route = Screen.Favorites.route,
            arguments = Screen.Favorites.arguments,
        ) {

        }

        composable(
            route = Screen.DeparturesFromStation.route,
            arguments = Screen.DeparturesFromStation.arguments,
        ) {

        }

        composable(
            route = Screen.ArrivalsToStation.route,
            arguments = Screen.ArrivalsToStation.arguments,
        ) {

        }

        composable(
            route = Screen.TrainBetweenStations.route,
            arguments = Screen.TrainBetweenStations.arguments,
        ) {
            TrainBetweenStationsScreen(
                onBack = { navController.navigateUp() },
            )
        }
    }
}