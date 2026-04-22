package com.boatit.boatsharing.features.login.data.dto

import kotlinx.serialization.Serializable

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
    var Accesstoken: String,
    var Refreshtoken: String,
)
