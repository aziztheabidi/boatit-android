package com.boatit.boatsharing.ui.voyager.dashboard.model

import kotlinx.serialization.Serializable

@Serializable
data class VoyagerFeedbackRequest(
    val Id: String,
    val Rating: Int,
    val Review: String
)

@Serializable
data class VoyagerFeedbackResponse(
    val Status: Int?=null,
    val Message: String?=null,
    val obj: String?=null,
)