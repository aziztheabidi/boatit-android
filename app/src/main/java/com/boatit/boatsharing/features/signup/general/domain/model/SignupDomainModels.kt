package com.boatit.boatsharing.features.signup.general.domain.model

import com.boatit.boatsharing.features.signup.general.model.GetVoyagerProfileResponse
import com.boatit.boatsharing.features.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.features.signup.general.model.VerifyEmailResponse
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileData
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileResponse

data class RegistrationDomainModel(
    val status: Int,
    val message: String,
    val email: String?,
)

data class VerifyEmailDomainModel(
    val status: Int,
    val message: String,
    val verificationToken: String?,
)

data class VoyagerProfileDomainResult(
    val status: Int,
    val message: String,
)

data class VoyagerProfileDomainModel(
    val userId: String?,
    val phoneNumber: String?,
    val firstName: String?,
    val lastName: String?,
    val address: String?,
    val dateOfBirth: String?,
    val stripeEmail: String?,
    val changedOn: String?,
    val changedBy: String?,
)

fun RegistrationResponse.toDomainModel(): RegistrationDomainModel {
    return RegistrationDomainModel(
        status = Status ?: 0,
        message = Message.orEmpty(),
        email = obj,
    )
}

fun VerifyEmailResponse.toDomainModel(): VerifyEmailDomainModel {
    return VerifyEmailDomainModel(
        status = Status ?: 0,
        message = Message.orEmpty(),
        verificationToken = obj,
    )
}

fun VoyagerProfileResponse.toDomainModel(): VoyagerProfileDomainResult {
    return VoyagerProfileDomainResult(
        status = Status ?: 0,
        message = Message.orEmpty(),
    )
}

fun GetVoyagerProfileResponse.toDomainModel(): VoyagerProfileDomainModel? {
    return obj?.toDomainModel()
}

fun VoyagerProfileData.toDomainModel(): VoyagerProfileDomainModel {
    return VoyagerProfileDomainModel(
        userId = UserId,
        phoneNumber = PhoneNumber,
        firstName = FirstName,
        lastName = LastName,
        address = Address,
        dateOfBirth = DateOfBirth,
        stripeEmail = StripeEmail,
        changedOn = ChangedOn,
        changedBy = ChangedBy,
    )
}
