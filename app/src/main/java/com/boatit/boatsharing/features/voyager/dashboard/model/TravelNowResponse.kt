package com.boatit.boatsharing.features.voyager.dashboard.model

import kotlinx.serialization.Serializable

@Serializable
data class TravelNowResponse(
    val Status: Int,
    val Message: String,
    val obj: TravelNowObj,
)

@Serializable
data class TravelNowObj(
    val Id: String? = "",
    val Name: String? = "",
    val CaptainUserId: String? = "",
    val CaptainName: String? = "",
    val PickupDock: String? = "",
    val PickupDockLatitude: Double? = 0.0,
    val PickupDockLongitude: Double? = 0.0,
    val DropOffDock: String? = "",
    val DropOffDockLatitude: Double? = 0.0,
    val DropOffDockLongitude: Double? = 0.0,
    val BoatName: String? = "",
    val BoatModel: String? = "",
    val OTP: Int? = 0,
    val NoOfVoyagers: Int? = 0,
    val AmountToPay: Double? = 0.0,
    val WaterStay: String? = "",
    val Duration: String? = "",
    val BookingDateTime: String? = "",
    val Sponsers: List<Sponser>? = emptyList(),
)
