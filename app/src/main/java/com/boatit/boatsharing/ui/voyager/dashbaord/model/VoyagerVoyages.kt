package com.boatit.boatsharing.ui.voyager.dashbaord.model

import kotlinx.serialization.Serializable


@Serializable
data class VoyagerVoyagesResponse(
    val Status: Int,
    val Message: String,
    val obj: PastVoyages
)

@Serializable
data class PastVoyages(
    val Past: List<VoyageDetails>
)

@Serializable
data class VoyageDetails(
    val Id: String,
    val CaptainUserId: String,
    val CaptainName: String,
    val PickupDock: String,
    val PickupDockLatitude: Double,
    val PickupDockLongitude: Double,
    val DropOffDock: String,
    val DropOffDockLatitude: Double,
    val DropOffDockLongitude: Double,
    val BoatName: String,
    val BoatModel: String,
    val OTP: Int,
    val AmountToPay: Double,
    val Status: String
)

@Serializable
data class ActiveVoyageResponse(
    val Status: Int,
    val Message: String,
    val obj: ActiveVoyageDetails
)

@Serializable
data class ActiveVoyageDetails(
    val Id: String,
    val CaptainUserId: String,
    val CaptainName: String,
    val PickupDock: String,
    val PickupDockLatitude: Double,
    val PickupDockLongitude: Double,
    val DropOffDock: String,
    val DropOffDockLatitude: Double,
    val DropOffDockLongitude: Double,
    val BoatName: String,
    val BoatModel: String,
    val OTP: Int,
    val AmountToPay: Double,
    val Status: String
)