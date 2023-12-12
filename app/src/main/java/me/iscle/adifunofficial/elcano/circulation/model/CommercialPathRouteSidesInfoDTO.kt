package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommercialPathRouteSidesInfoDTO(
    val commercialPathInfo: CommercialPathInfoDTO,
    val passthroughSteps: List<PassthroughDetailsStepDTO>,
)
