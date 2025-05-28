package com.boatit.boatsharing.ui.business.model

import kotlinx.serialization.Serializable

@Serializable
data class GetBusinessResponse(
    val Status: Int,
    val Message: String,
    val obj: BusinessData?
)

@Serializable
data class BusinessData(
    val LogoPath: String,
    val BusinessType: String,
    val ImagesPath: List<String>,
    val Location: String,
    val BusinessHours: List<BusinessHour>,
    val IsDock: Boolean,
    val Name: String,
    val Zone: String,
    val State: String,
    val City: String,
    val ZipCode: String,
    val ShoreLine: String,
    val Address: String,
    val Latitude: Double,
    val Longitude: Double,
    val UserId: String,
    val ChangedOn: String,
    val ChangedBy: String
)

@Serializable
data class BusinessHour(
    val Day: String,
    val StartTime: String,
    val EndTimeTime: String
)
