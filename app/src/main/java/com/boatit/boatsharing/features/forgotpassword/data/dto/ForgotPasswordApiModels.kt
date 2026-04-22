package com.boatit.boatsharing.features.forgotpassword.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPassRequestDto(
    val Email: String,
)

@Serializable
data class ForgotPassResponseDto(
    val Status: Int,
    val Message: String,
    val obj: String,
)
