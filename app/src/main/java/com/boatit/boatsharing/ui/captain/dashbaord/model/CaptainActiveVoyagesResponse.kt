package com.boatit.boatsharing.ui.captain.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptainActiveVoyagesResponse(
    val Status: Int,
    val Message: String,
    val obj: ActiveVoyages
)

@Serializable
data class ActiveVoyages(
    val Ongoing: List<VoyageDetails>,
    val InProcess: List<VoyageDetails>
)

@Serializable
data class VoyageDetails(
    val Id: String,
    val VoyagerUserId: String? = null,
    val VoyagerName: String? = null,
    val VoyagerPhoneNumber: String? = null,
    val PickupDock: String? = null,
    val PickupDockLatitude: Double? = null,
    val PickupDockLongitude: Double? = null,
    val DropOffDock: String? = null,
    val DropOffDockLatitude: Double? = null,
    val DropOffDockLongitude: Double? = null,
    val NoOfVoyager: Int? = null,
    val AmountToPay: Double? = null,
    val PastVoyage: Int? = null
)
