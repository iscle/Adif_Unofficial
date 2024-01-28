package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommercialPathRouteSidesInfoDTO(
    val commercialPathInfo: CommercialPathInfoDTO?, // information about the train route
    val passthroughSteps: List<PassthroughDetailsStepDTO?>?, // information about the user's request
)
