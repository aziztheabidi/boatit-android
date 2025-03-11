package com.boatit.boatsharing.ui.forgotpassword.view
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPassResponse(
    val Status: Int,
    val Message: String,
    val obj: String
)

