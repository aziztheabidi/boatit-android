package com.boatit.boatsharing.features.signup.business.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessInfoRequest(
    val UserId: String,
    val Name: String,
    val Type: String,
    val Address: String,
    val PhoneNumber: String,
    val YearOfEstablishment: String,
    val Time: String,
)

@Serializable
data class SaveBusinessInfoResponse(
    val Status: Int,
    val Message: String,
    val obj: String?,
)

@Serializable
data class BusinessInfoResponse(
    val Status: Int,
    val Message: String,
    val obj: BusinessInfoData?,
)

@Serializable
data class BusinessInfoData(
    val Name: String?,
    val Type: String?,
    val Address: String?,
    val PhoneNumber: String?,
    val YearOfEstablishment: Int?,
    val Time: String?,
    val Description: String?,
    val IsDock: Boolean?,
    val LogoPath: String?,
    val UserId: String?,
    val ChangedOn: String?,
    val ChangedBy: String?,
    val ImagesPath: List<String>?,
)
