package com.boatit.boatsharing.ui.voyager.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class NearbyPlacesResponse(
    val Status: Int,
    val Message: String,
    val obj: DockListObj?
)

@Serializable
data class DockListObj(
    val All: List<Place>?,
    val Business: List<Place>?
)

@Serializable
data class Place(
    val Name: String,
    val Zone: String,
    val State: String,
    val City: String,
    val ZipCode: String,
    val ShoreLine: String,
    val Address: String,
    val Latitude: Double,
    val Longitude: Double,
    val DockTypeId: Int,
    val DockType: String
)

@Serializable
data class FindBoatRequest(
    val VoyagerUserId: String,
    val VoyageCategoryId: Int,
    val PickupDockId: Int,
    val DropOffDockId: Int,
    val NoOfVoyagers: Int,
    val EstimatedCost: Double,
    val IsImmediately: Boolean,
    val IsSplitPayment: Boolean,
    val BookingDate: String,
)

@Serializable
data class FindBoatResponse(
    val Status: Int,
    val Message: String,
    val obj: String?
)