package com.boatit.boatsharing.ui.signup.business.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessInfoRequest(
    val UserId: String,
    val BusinessName: String,
    val BusinessAddress: String,
    val ContactNumber: String,
    val Email: String
)

@Serializable
data class SaveBusinessInfoResponse(
    val Status: Int,
    val Message: String,
    val obj: String?
)

@Serializable
data class BusinessInfoResponse(
    val Status: Int,
    val Message: String,
    val obj: BusinessInfoData?
)

@Serializable
data class BusinessInfoData(
    val UserId: String,
    val BusinessName: String,
    val BusinessAddress: String,
    val ContactNumber: String,
    val Email: String
)