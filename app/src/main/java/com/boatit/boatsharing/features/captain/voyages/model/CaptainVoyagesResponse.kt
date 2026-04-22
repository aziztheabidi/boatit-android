package com.boatit.boatsharing.features.captain.voyages.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptainVoyagesResponse(
    val Status: Int,
    val Message: String,
    val obj: CaptainVoyages,
)

@Serializable
data class CaptainVoyages(
    val Pending: List<CaptainVoyageListItemDto>,
    val Ongoing: List<CaptainVoyageListItemDto>,
    val InProcess: List<CaptainVoyageListItemDto>,
)

/** Past/current captain voyage list row from `/Captain/GetPastVoyages` style payloads (wire DTO). */
@Serializable
data class CaptainVoyageListItemDto(
    val Id: String? = null,
    val Name: String? = null,
    val PhoneNumber: String? = null,
    val PickupDock: String? = null,
    val DropOffDock: String? = null,
    val NoOfVoyager: Int? = null,
    val TotalAmount: Double? = null,
    val PastVoyage: Int? = null,
)
