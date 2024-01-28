package me.iscle.adifunofficial.circulation.model

import me.iscle.adifunofficial.elcano.circulation.model.CirculationState
import me.iscle.adifunofficial.elcano.circulation.model.TimeType

data class StopInfo(
    val plannedTime: Long,
    val delay: Long?,
    val showDelay: Boolean,
    val timeType: TimeType,
    val platform: String?,
    val circulationState: CirculationState,
)
