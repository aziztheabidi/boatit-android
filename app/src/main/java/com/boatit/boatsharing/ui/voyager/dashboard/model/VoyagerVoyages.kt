package com.boatit.boatsharing.ui.voyager.dashboard.model

import com.boatit.boatsharing.utils.StringOrIntAsIntSerializer
import kotlinx.serialization.Serializable

@Serializable
data class VoyagerVoyagesResponse(
    val Status: Int,
    val Message: String,
    val obj: List<PastVoyages>
)

@Serializable
data class PastVoyages(
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
    val Rating: Double,
    val NoOfVoyagers: Int,
    val AmountToPay: Double,
    val WaterStay: String,
    val Duration: String,
    val BookingDateTime: String,
    val Sponsers: List<String>
)

@Serializable
data class CaptainCompletedVoyageResponse(
    val Status: Int,
    val Message: String,
    val obj: List<CaptainCompletedVoyage>
)

@Serializable
data class CaptainCompletedVoyage(
    val Id: String,
    val Name: String,
    val VoyagerUserId: String,
    val VoyagerName: String,
    val VoyagerPhoneNumber: String,
    val Rating: Double? =null,
    val PickupDock: String,
    val PickupDockLatitude: Double,
    val PickupDockLongitude: Double,
    val DropOffDock: String,
    val DropOffDockLatitude: Double,
    val DropOffDockLongitude: Double,
    val NoOfVoyager: Int,
    val AmountToPay: Double,
    val WaterStay: String,
    val Duration: String,
    val BookingDateTime: String
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
    val Status: String,
    val CaptainUserId: String,
    val CaptainName: String,
    val Name:String,
    val PickupDock: String,
    val PickupDockLatitude: Double,
    val PickupDockLongitude: Double,
    val DropOffDock: String,
    val DropOffDockLatitude: Double,
    val DropOffDockLongitude: Double,
    val BoatName: String,
    val BoatModel: String,
    val OTP: Int? = null,
    val AmountToPay: Double,
    val Rating: Double? = null,
    val Duration: String? = null,

    @Serializable(with = StringOrIntAsIntSerializer::class)
    val NoOfVoyagers: Int?,

    val WaterStay: String,
    val BookingDateTime:String
)