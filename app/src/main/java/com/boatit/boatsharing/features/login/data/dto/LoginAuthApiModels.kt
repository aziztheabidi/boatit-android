package com.boatit.boatsharing.features.login.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/** Wire / JSON DTO for `Account/Login` (field names match backend contract). */
@Serializable
data class LoginResponseDto(
    val Status: Int,
    val Message: String,
    val obj: UserDataDto? = null,
)

@Serializable
data class UserDataDto(
    val Email: String,
    val Password: String,
    val UserId: String,
    val Username: String,
    var Role: String,
    val MissingStep: Int,
    @SerialName("Accesstoken")
    var accessToken: String,
    @SerialName("Refreshtoken")
    var refreshToken: String,
)
