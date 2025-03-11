package com.boatit.boatsharing.ui.captain.voyages.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptainVoyagesResponse(
    val Status: Int,
    val Message: String,
    val obj: CaptainVoyages
)

@Serializable
data class CaptainVoyages(
    val Pending: List<VoyageDetails>,
    val Ongoing: List<VoyageDetails>,
    val InProcess: List<VoyageDetails>
)

@Serializable
data class VoyageDetails(
    val Id: String? = null,
    val Name: String? = null,
    val PhoneNumber: String? = null,
    val PickupDock: String? = null,
    val DropOffDock: String? = null,
    val NoOfVoyager: Int? = null,
    val TotalAmount: Double? = null,
    val PastVoyage: Int? = null
)