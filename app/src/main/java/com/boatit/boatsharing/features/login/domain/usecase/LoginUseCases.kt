package com.boatit.boatsharing.features.login.domain.usecase

import com.boatit.boatsharing.features.login.domain.model.LoginDomainModel
import com.boatit.boatsharing.features.login.domain.model.toDomainModel
import com.boatit.boatsharing.features.login.repository.ILoginRepository

class LoginUserUseCase(
    private val loginRepository: ILoginRepository,
) {
    suspend operator fun invoke(
        username: String,
        password: String,
    ): Result<LoginDomainModel> {
        val normalizedEmail = username.trim()
        val normalizedPassword = password.trim()

        if (!normalizedEmail.isValidEmail()) {
            return Result.failure(IllegalArgumentException("Enter a valid email address"))
        }
        if (normalizedPassword.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        return loginRepository.login(normalizedEmail, normalizedPassword).map { it.toDomainModel() }
    }
}

private fun String.isValidEmail(): Boolean {
    // More lenient email regex that accepts most valid email formats
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(this)
}
