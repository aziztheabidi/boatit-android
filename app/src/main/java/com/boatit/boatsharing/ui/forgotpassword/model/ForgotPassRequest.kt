package com.boatit.boatsharing.ui.forgotpassword.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPassRequest(
    @SerializedName("Email")
    val email: String,
)
