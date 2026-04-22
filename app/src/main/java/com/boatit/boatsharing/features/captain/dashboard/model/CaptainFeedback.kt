package com.boatit.boatsharing.features.captain.dashboard.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptainFeedbackRequest(
    val Id: String,
    val Rating: Int,
    val Review: String,
)

@Serializable
data class CaptainFeedbackResponse(
    val Status: Int? = null,
    val Message: String? = null,
    val obj: String? = null,
)
