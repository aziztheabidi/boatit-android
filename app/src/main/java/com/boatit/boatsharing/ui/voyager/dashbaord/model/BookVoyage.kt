package com.boatit.boatsharing.ui.voyager.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class BookVoyageRequest(
    val VoyagerUserId: String,
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
    val NoOfSponsers: Int,
    val EstimatedCost: Double,
    val IndvidualAmount: Double,
    val Sponsers: List<Sponser>
)

@Serializable
data class Sponser(
    val VoyagerUserId: String
)

@Serializable
data class BookVoyageResponse(
    val Status: Int,
    val Message: String,
    val obj: String?
)
