package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommercialRouteInfoResponse(
    val commercialPaths: List<CommercialRouteInfoDTO>,
)
