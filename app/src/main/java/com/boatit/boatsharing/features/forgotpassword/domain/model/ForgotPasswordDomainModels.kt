package com.boatit.boatsharing.features.forgotpassword.domain.model

import com.boatit.boatsharing.features.forgotpassword.data.dto.ForgotPassResponseDto

data class ForgotPasswordDomainModel(
    val status: Int,
    val message: String,
    val reference: String,
)

fun ForgotPassResponseDto.toDomainModel(): ForgotPasswordDomainModel =
    ForgotPasswordDomainModel(
        status = Status,
        message = Message,
        reference = obj,
    )
