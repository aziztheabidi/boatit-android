package com.boatit.boatsharing.ui.voyager.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class TravelNowResponse(
    val Status: Int,
    val Message: String,
    val obj: TravelNowObj
)

@Serializable
data class TravelNowObj(
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

