package me.iscle.adifunofficial.elcano.circulation.model

enum class TrafficType {
    CERCANIAS,
    AVLDMD,
    OTHERS,
    TRAVELERS,
    GOODS,
    ALL;

    fun prettyName(commuterNetwork: CommuterNetwork): String {
        return if (this == CERCANIAS && commuterNetwork == CommuterNetwork.RODALIES_CATALUNYA) {
            "Rodalies"
        } else {
            name.replaceFirstChar { it.uppercase() }
        }
    }

    companion object {
        fun fromString(string: String): TrafficType? {
            return entries.firstOrNull { it.name == string }
        }
    }
}