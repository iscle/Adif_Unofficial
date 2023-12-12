package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommercialRouteInfoDTO(
    val commercialPathInfo: CommercialPathInfoDTO,
    val passthroughStep: RouteStepDTO,
)
