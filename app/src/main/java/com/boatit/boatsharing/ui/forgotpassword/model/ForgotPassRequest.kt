package com.boatit.boatsharing.ui.forgotpassword.model
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPassRequest(
    val email: String,
)
