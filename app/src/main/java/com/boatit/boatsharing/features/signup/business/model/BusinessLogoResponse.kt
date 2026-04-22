package com.boatit.boatsharing.features.signup.business.model

import kotlinx.serialization.Serializable

@Serializable
data class SaveBusinessLogoResponse(
    val Status: Int,
    val Message: String,
    val obj: String?,
)
