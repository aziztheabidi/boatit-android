package com.boatit.boatsharing.features.voyager.dashboard.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BookVoyageRequest(
    val VoyagerUserId: String,
    val Name: String,
    val VoyageCategoryId: Int,
    val PickupDockId: Int,
    val DropOffDockId: Int,
    val NoOfVoyagers: Int,
    val IsImmediately: Boolean,
    val IsSplitPayment: Boolean,
    val BookingDate: String,
    val StartTime: String,
    val IsStayOnWater: Boolean,
    val EndTime: String,
    val PerHourRate: Double,
    val DurationInHours: Double,
    @SerialName("NoOfSponsers")
    val noOfSponsors: Int,
    val EstimatedCost: Double,
    val IndvidualAmount: Double,
    @SerialName("Sponsers")
    val sponsors: List<Sponsor>,
) 

@Serializable
data class Sponsor(
    val VoyagerUserId: String,
    val VoyagerUserName: String,
    val AmountToPay: Double,
    val Status: String,
)

@Serializable
data class BookVoyageResponse(
    val Status: Int,
    val Message: String,
    val obj: String?,
)
