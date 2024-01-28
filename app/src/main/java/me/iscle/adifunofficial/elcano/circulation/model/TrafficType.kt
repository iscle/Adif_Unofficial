package me.iscle.adifunofficial.elcano.circulation.model

enum class TrafficType {
    CERCANIAS,
    AVLDMD,
    OTHERS,
    TRAVELERS,
    GOODS,
    ALL;

    companion object {
        fun fromString(string: String): TrafficType? {
            return entries.firstOrNull { it.name == string }
        }
    }
}