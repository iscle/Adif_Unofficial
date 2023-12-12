package me.iscle.adifunofficial.elcano.circulation.model

import com.squareup.moshi.JsonClass

interface CirculationPathRequest {
    enum class State {
        YES,
        NOT,
        BOTH,
    }

    @JsonClass(generateAdapter = true)
    data class PageInfoDTO(
        val pageNumber: Int?,
    )
}