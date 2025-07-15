package com.boatit.boatsharing.ui.business.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessRequest(
    val Location: String,
    val BusinessHours: List<BusinessHour>,
    val IsDock: Boolean,
    val ShoreId: Int,
    val Name: String,
    val ZoneId: Int,
    val IslandId: Int,
    val State: String,
    val City: String,
    val ZipCode: String,
    val ShoreLine: String,
    val Address: String,
    val Latitude: Double,
    val Longitude: Double
)

@Serializable
data class BusinessHour(
    val Day: String,
    val StartTime: String,
    val EndTimeTime: String
)


@Serializable
data class GetBusinessResponse(
    val Status: Int,
    val Message: String,
    val obj: BusinessData?
)

@Serializable
data class BusinessData(
    val LogoPath: String?,
    val BusinessType: String?,
    val YearOfEstablishment: Int?,
    val Description: String?,
    val ImagesPath: List<String>?,
    val Location: String?,
    val BusinessHours: List<BusinessHour>?,
    val IsDock: Boolean?,
    val Name: String?,
    val ShoreId: Int?,
    val ShoreName:String?,
    val ZoneId: Int?,
    val ZoneName: String?,
    val IslandId: Int?,
    val IslandName: String?,
    val State: String?,
    val City: String?,
    val ZipCode: String?,
    val Address: String?,
    val Latitude: Double?,
    val Longitude: Double?,
    val UserId: String?,
    val ChangedOn: String?,
    val ChangedBy: String?
)

