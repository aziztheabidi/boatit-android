package com.boatit.boatsharing.features.forgotpassword.domain.usecase

import com.boatit.boatsharing.features.forgotpassword.domain.model.ForgotPasswordDomainModel
import com.boatit.boatsharing.features.forgotpassword.repository.IForgotPassRepository

class SendForgotPasswordUseCase(
    private val forgotPassRepository: IForgotPassRepository,
) {
    suspend operator fun invoke(email: String): Result<ForgotPasswordDomainModel> {
        val normalizedEmail = email.trim()
        if (!normalizedEmail.isValidEmail()) {
            return Result.failure(IllegalArgumentException("Enter a valid email address"))
        }

        return forgotPassRepository.forgotPassResp(normalizedEmail)
    }
}

private fun String.isValidEmail(): Boolean {
    // More lenient email regex that accepts most valid email formats
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(this)
}
