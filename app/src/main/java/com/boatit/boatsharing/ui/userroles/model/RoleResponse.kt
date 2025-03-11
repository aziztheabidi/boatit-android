package com.boatit.boatsharing.ui.userroles.model
import kotlinx.serialization.Serializable

@Serializable
data class RoleResponse(
    val Status: Int? = null,
    val Message: String? = null,
    val obj: TokenData? = null
)

@Serializable
data class TokenData(
    val Accesstoken: String? = null,
    val Refreshtoken: String? = null
)
