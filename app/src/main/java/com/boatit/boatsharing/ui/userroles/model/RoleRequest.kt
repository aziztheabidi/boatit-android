package com.boatit.boatsharing.ui.userroles.model

import kotlinx.serialization.Serializable

@Serializable
data class RoleRequest(
    val UserId: String,
    val Role: String
)
