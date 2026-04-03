package com.boatit.boatsharing.ui.login.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerializedName("Email")
    val email: String,
    @SerializedName("Password")
    val password: String,
)
