package me.iscle.adifunofficial.elcano.circulation.network

import me.iscle.adifunofficial.elcano.circulation.model.CommercialPathRouteInfoResponse
import me.iscle.adifunofficial.elcano.circulation.model.OneOrSeveralPathsRequest
import me.iscle.adifunofficial.elcano.circulation.model.TrafficCirculationPathRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

private const val HEADER_USER_KEY = "User-key: f4ce9fbfa9d721e39b8984805901b5df"

interface CirculationService {
    @Headers(HEADER_USER_KEY)
    @POST("/portroyalmanager/secure/circulationpaths/arrivals/traffictype/")
    suspend fun arrivals(@Body trafficCirculationPathRequest: TrafficCirculationPathRequest): CommercialPathRouteInfoResponse

    @Headers(HEADER_USER_KEY)
    @POST("/portroyalmanager/secure/circulationpaths/betweenstations/traffictype/")
    suspend fun betweenStations(@Body trafficCirculationPathRequest: TrafficCirculationPathRequest): CommercialPathRouteInfoResponse

    @Headers(HEADER_USER_KEY)
    @POST("/portroyalmanager/secure/circulationpaths/departures/traffictype/")
    suspend fun departures(@Body trafficCirculationPathRequest: TrafficCirculationPathRequest): CommercialPathRouteInfoResponse

    @Headers(HEADER_USER_KEY)
    @POST("/portroyalmanager/secure/circulationpathdetails/onepaths/")
    suspend fun onePaths(@Body oneOrSeveralPathsRequest: OneOrSeveralPathsRequest): CommercialPathRouteInfoResponse

    @Headers(HEADER_USER_KEY)
    @POST("/portroyalmanager/secure/circulationpathdetails/severalpaths/")
    suspend fun severalPaths(@Body oneOrSeveralPathsRequest: OneOrSeveralPathsRequest): CommercialPathRouteInfoResponse

    @Headers(HEADER_USER_KEY)
    @POST("/portroyalmanager/secure/circulationpaths/compositions/path/")
    suspend fun compositions(@Body oneOrSeveralPathsRequest: OneOrSeveralPathsRequest): CommercialPathRouteInfoResponse
}