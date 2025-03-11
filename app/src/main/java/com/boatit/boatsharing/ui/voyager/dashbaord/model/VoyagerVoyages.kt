package com.boatit.boatsharing.ui.voyager.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class VoyagerVoyagesResponse(
    val Status: Int,
    val Message: String,
    val obj: VoyagesData
)

@Serializable
data class VoyagesData(
    val Active: ActiveVoyage,
    val Past: List<PastVoyage>
)

@Serializable
data class ActiveVoyage(
    val CaptainName: String,
    val PickupDock: String,
    val DropOffDock: String,
    val BoatName: String,
    val BoatModel: String,
    val OTP: Int,
    val PaymentAmount: Double,
    val Status: String
)

@Serializable
data class PastVoyage(
    val CaptainName: String,
    val PickupDock: String,
    val DropOffDock: String,
    val BoatName: String,
    val BoatModel: String,
    val OTP: Int,
    val PaymentAmount: Double,
    val Status: String
)