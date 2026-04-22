package com.boatit.boatsharing.features.signup.general.domain.usecase

import com.boatit.boatsharing.features.login.domain.model.LoginDomainModel
import com.boatit.boatsharing.features.login.domain.model.toDomainModel
import com.boatit.boatsharing.features.signup.general.domain.model.RegistrationDomainModel
import com.boatit.boatsharing.features.signup.general.domain.model.VerifyEmailDomainModel
import com.boatit.boatsharing.features.signup.general.domain.model.VoyagerProfileDomainModel
import com.boatit.boatsharing.features.signup.general.domain.model.VoyagerProfileDomainResult
import com.boatit.boatsharing.features.signup.general.domain.model.toDomainModel
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.features.signup.general.repository.IGetVoyagerProfileRepository
import com.boatit.boatsharing.features.signup.general.repository.IPasswordRepository
import com.boatit.boatsharing.features.signup.general.repository.IRegistrationRepository
import com.boatit.boatsharing.features.signup.general.repository.IVerifyEmailRepository
import com.boatit.boatsharing.features.signup.general.repository.IVoyagerProfileRepository

class RegisterUserUseCase(
    private val registrationRepository: IRegistrationRepository,
) {
    suspend operator fun invoke(
        username: String,
        phoneNumber: String,
        email: String,
    ): Result<RegistrationDomainModel> {
        val normalizedName = username.trim()
        val normalizedPhone = phoneNumber.trim()
        val normalizedEmail = email.trim()

        if (normalizedName.length < 3) {
            return Result.failure(IllegalArgumentException("Name must be at least 3 characters"))
        }
        if (normalizedPhone.length < 3 || !normalizedPhone.all { it.isDigit() }) {
            return Result.failure(IllegalArgumentException("Phone number must contain at least 3 digits"))
        }
        if (!normalizedEmail.isValidEmail()) {
            return Result.failure(IllegalArgumentException("Enter a valid email address"))
        }

        return registrationRepository.tempRegister(normalizedName, normalizedPhone, normalizedEmail)
            .map { it.toDomainModel() }
    }
}

class VerifySignupEmailUseCase(
    private val verifyEmailRepository: IVerifyEmailRepository,
) {
    suspend operator fun invoke(
        email: String,
        otp: String,
    ): Result<VerifyEmailDomainModel> {
        val normalizedEmail = email.trim()
        val normalizedOtp = otp.trim()

        if (normalizedOtp.length !in 4..6 || !normalizedOtp.all { it.isDigit() }) {
            return Result.failure(IllegalArgumentException("OTP must be 4 to 6 digits"))
        }

        return verifyEmailRepository.verifyEmail(normalizedEmail, normalizedOtp)
            .map { it.toDomainModel() }
    }
}

class RegisterPasswordUseCase(
    private val passwordRepository: IPasswordRepository,
) {
    suspend operator fun invoke(
        password: String,
        token: String,
    ): Result<LoginDomainModel> {
        val normalizedPassword = password.trim()
        val normalizedToken = token.trim()

        val hasNumber = normalizedPassword.any { it.isDigit() }
        val hasSymbol = normalizedPassword.any { !it.isLetterOrDigit() }
        if (normalizedPassword.length < 8 || !hasNumber || !hasSymbol) {
            return Result.failure(
                IllegalArgumentException("Password must be at least 8 characters and include a number and symbol"),
            )
        }
        if (normalizedToken.isBlank()) {
            return Result.failure(IllegalArgumentException("Verification token is required"))
        }

        return passwordRepository.passwordRepository(normalizedPassword, normalizedToken)
            .map { it.toDomainModel() }
    }
}

class SaveVoyagerProfileUseCase(
    private val voyagerProfileRepository: IVoyagerProfileRepository,
) {
    suspend operator fun invoke(request: VoyagerProfileRequest): Result<VoyagerProfileDomainResult> {
        if (request.UserId.isNullOrBlank()) {
            return Result.failure(IllegalArgumentException("User id is required"))
        }
        if (request.FirstName.trim().length < 3 || request.LastName.trim().length < 3) {
            return Result.failure(IllegalArgumentException("First and last name must be at least 3 characters"))
        }
        if (!request.StripeEmail.trim().isValidEmail()) {
            return Result.failure(IllegalArgumentException("Enter a valid payout email"))
        }

        return voyagerProfileRepository.saveVoyagerProfile(request)
            .map { it.toDomainModel() }
    }
}

class FetchVoyagerProfileUseCase(
    private val getVoyagerProfileRepository: IGetVoyagerProfileRepository,
) {
    suspend operator fun invoke(): Result<VoyagerProfileDomainModel?> {
        return getVoyagerProfileRepository.getVoyagerProfile().map { it.toDomainModel() }
    }
}

private fun String.isValidEmail(): Boolean {
    // More lenient email regex that accepts most valid email formats
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(this)
}
