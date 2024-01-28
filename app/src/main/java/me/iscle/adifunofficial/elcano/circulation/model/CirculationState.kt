package me.iscle.adifunofficial.elcano.circulation.model

enum class CirculationState {
    PENDING_TO_CIRCULATE,
    RUNNING,
    STOPPED,
    SUPPRESSED,
    FINISHED,
    TRACKING_LOST,
}