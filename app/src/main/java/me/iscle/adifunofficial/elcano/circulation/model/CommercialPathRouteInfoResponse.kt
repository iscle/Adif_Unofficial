package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommercialPathRouteInfoResponse(
    val commercialPaths: List<CommercialPathRouteSidesInfoDTO?>?,
)
