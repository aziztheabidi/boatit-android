package com.boatit.boatsharing.ui.voyager.dashboard.model

import kotlinx.serialization.Serializable

@Serializable
data class FutureBookedVoyages(
    val Status: Int,
    val Message: String,
    val obj: BookedVoyageObj
)

@Serializable
data class BookedVoyageObj(
    val UnConfirmed: BookedVoyage? = null,
    val Confirmed: List<BookedVoyage> = emptyList()
)

@Serializable
data class BookedVoyage(
    val Id: String,
    val Name: String,
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
    val NoOfVoyagers: Int,
    val AmountToPay: Double,
    val WaterStay: String,
    val Duration: String,
    val BookingDateTime: String,
    val Sponsers: List<Sponser>
)


