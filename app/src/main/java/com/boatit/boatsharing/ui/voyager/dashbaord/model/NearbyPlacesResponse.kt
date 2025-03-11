package com.boatit.boatsharing.ui.voyager.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class NearbyPlacesResponse(
    val Status: Int,
    val Message: String,
    val obj: List<Place>
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
    val DockType: String,
//    val Id: Int,
//    val ValidFlag: Boolean,
    val ChangedOn: String,
    val ChangedBy: String
)

@Serializable
data class FindBoatRequest(
    val VoyagerUserId: String,
    val PickupDockId: Int,
    val DropOffDockId: Int,
    val NoOfVoyagers: Int
)

@Serializable
data class FindBoatResponse(
    val Status: Int,
    val Message: String,
    val obj: String?
)