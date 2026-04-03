package com.boatit.boatsharing.ui.voyager.dashboard.model

import kotlinx.serialization.Serializable

@Serializable
data class SponsorPayments(
    val Status: Int,
    val Message: String,
    val obj: List<SponsorVoyagerPayment>
)

@Serializable
data class SponsorVoyagerPayment(
    val Id: String,
    val Name: String,
    val VoyagerName: String,
    val VoyagerPhoneNumber: String,
    val PickupDock: String,
    val PickupDockLatitude: Double,
    val PickupDockLongitude: Double,
    val DropOffDock: String,
    val DropOffDockLatitude: Double,
    val DropOffDockLongitude: Double,
    val AmountToPay: Double,
    val NoOfVoyagers: Int,
    val WaterStay: String,
    val Duration: String,
    val BookingDateTime: String,
    val VoyageStatus: String
)
