package com.boatit.boatsharing.ui.login.model
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val Status: Int,
    val Message: String,
    val obj: UserData? = null
)

@Serializable
data class UserData(
    val Email: String,
    val Password: String,
    val UserId: String,
    val Username: String,
    var Role: String,
    val MissingStep: Int,
    var Accesstoken: String,
    var Refreshtoken: String
)


