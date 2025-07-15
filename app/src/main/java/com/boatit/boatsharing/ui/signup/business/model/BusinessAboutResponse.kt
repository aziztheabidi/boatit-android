package com.boatit.boatsharing.ui.signup.business.model

import kotlinx.serialization.Serializable

@Serializable
data class SaveBusinessAboutRequest(
    val UserId: String,
    val Description: String,
    val IsDock: Boolean
)

// Response model
@Serializable
data class SaveBusinessAboutResponse(
    val Status: Int,
    val Message: String,
    val obj: String?
)