package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VisualEffectsDTO(
    val countDown: Boolean,
    val inmediateDeparture: Boolean,
    val showDelay: Boolean,
)
