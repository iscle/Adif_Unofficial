package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessibleDTO(
    val accessible: Boolean?,
    val description: String?,
)
