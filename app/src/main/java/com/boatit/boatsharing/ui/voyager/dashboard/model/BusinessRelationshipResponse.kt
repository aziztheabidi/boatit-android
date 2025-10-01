package com.boatit.boatsharing.ui.voyager.dashboard.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessRelationshipResponse(
    val Status: Int,
    val Message: String,
    val obj: BusinessRelationshipObj
)

@Serializable
data class BusinessRelationshipObj(
    val Followed: List<BusinessData>,
    val UnFollowed: List<BusinessData>
)

@Serializable
data class BusinessData(
    val Id: Int,
    val Name: String,
    val LogoPath: String,
    val BusinessType: String,
    val YearOfEstablishment: Int,
    val Description: String,
    val ImagesPath: List<String>,
    val Location: String,
    val BusinessHours: List<BusinessHour>
)

@Serializable
data class BusinessHour(
    val Day: String,
    val StartTime: String,
    val EndTimeTime: String
)
