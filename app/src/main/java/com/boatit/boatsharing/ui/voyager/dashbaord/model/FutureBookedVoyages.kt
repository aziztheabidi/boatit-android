package com.boatit.boatsharing.ui.voyager.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class FutureBookedVoyages(
    val Status: Int,
    val Message: String,
    val obj: BookedVoyageObj
)

@Serializable
data class BookedVoyageObj(
    val UnConfirmed: BookedVoyage,
    val Confirmed: List<BookedVoyage>
)

@Serializable
data class BookedVoyage(
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
//    val Status: String,
    val Sponsers: List<FutureSponser> = emptyList()
)

@Serializable
data class FutureSponser(
    val VoyagerUserId: String? = null // If sponsor object structure expands, update accordingly
)
