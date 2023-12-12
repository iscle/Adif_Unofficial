package me.iscle.adifunofficial.elcano.stations.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationCommercialServicesDTO(
    val accessible: AccessibleDTO?,
    val androidLink: String?,
    val description: String?,
    val identifier: String?,
    val localNumber: String?,
    val locator: LocatorDTO?,
    val logo: String?,
    val name: String?,
    val offers: List<OfferDTO>?,
    val openingHours: OpeningHoursDTO?,
    val paymentWay: List<String>?,
    val phoneNumber: String?,
    val pictures: List<String>?,
    val sapNumber: String?,
    val serviceType: String?,
    val web: String?,
)
