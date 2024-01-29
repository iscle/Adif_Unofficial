package me.iscle.adifunofficial.elcano.stations.network

import me.iscle.adifunofficial.elcano.stations.model.OneStationRequest
import me.iscle.adifunofficial.elcano.stations.model.StationObservationsRequest
import me.iscle.adifunofficial.elcano.stations.model.StationObservationsResponse
import me.iscle.adifunofficial.elcano.stations.model.StationResponse
import me.iscle.adifunofficial.elcano.stations.model.StationsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

private const val HEADER_CONTENT_TYPE = "Content-Type: application/json;charset=utf-8"
private const val HEADER_USER_KEY = "User-key: 0d021447a2fd2ac64553674d5a0c1a6f"
interface StationService {
    @Headers(HEADER_CONTENT_TYPE, HEADER_USER_KEY)
    @POST("/portroyalmanager/secure/stations/onestation/")
    suspend fun getStation(@Body oneStationRequest: OneStationRequest): StationResponse

    @Headers(HEADER_CONTENT_TYPE, HEADER_USER_KEY)
    @GET("/portroyalmanager/secure/stations/allstations/reducedinfo/{token}/")
    suspend fun getStations(@Path("token") token: String): StationsResponse

    @Headers(HEADER_CONTENT_TYPE, HEADER_USER_KEY)
    @POST("/portroyalmanager/secure/stationsobservations/")
    suspend fun getStationObservations(@Body stationObservationsRequest: StationObservationsRequest): StationObservationsResponse
}