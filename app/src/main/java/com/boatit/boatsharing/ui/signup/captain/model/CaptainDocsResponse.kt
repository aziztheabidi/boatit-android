package com.boatit.boatsharing.ui.signup.captain.model

import kotlinx.serialization.Serializable

@Serializable
data class SaveCaptainDocumentRequest(
    val UserId: String,
    val LicenseNumber: String,
    val LicenseExpiration: String,
    val TypeOfLicense: String,
    val InsuranceCompany: String,
    val PolicyNumber: String,
    val PolicyExpiration: String
)

@Serializable
data class SaveCaptainDocumentResponse(
    val Status: Int,
    val Message: String
)

@Serializable
data class GetCaptainDocumentResponse(
    val Status: Int,
    val Message: String,
    val obj: CaptainDocument
)

@Serializable
data class CaptainDocument(
    val LicenseNumber: String,
    val LicenseExpiration: String,
    val TypeOfLicense: String,
    val InsuranceCompany: String,
    val PolicyNumber: String,
    val PolicyExpiration: String,
    val UserId: String,
    val ChangedOn: String,
    val ChangedBy: String
)

