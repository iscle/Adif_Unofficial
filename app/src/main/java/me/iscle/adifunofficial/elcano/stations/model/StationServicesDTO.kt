package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationServicesDTO(
    val accessible: AccessibleDTO,
    val androidLink: String,
    val description: String,
    val huaweiLink: String,
    val identifier: String,
    val ioslink: String,
    val locator: LocatorDTO,
    val logo: String,
    val name: String,
    val openingHours: OpeningHoursDTO,
    val phoneNumber: String,
    val pictures: List<String>,
    val serviceType: String,
    val web: String,
)
