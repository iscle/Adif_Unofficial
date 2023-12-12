package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ObservationDTO(
    val avldmd: Boolean?,
    val cercanias: Boolean?,
    val observation: String?,
    val type: ObservationTypeDTO?,
)
