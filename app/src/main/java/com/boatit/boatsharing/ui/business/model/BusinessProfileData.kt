package com.boatit.boatsharing.ui.business.model

/**
 * BusinessProfileInfo - Business profile information container
 * 
 * FULFILLS: LLR-0.2.1 - BusinessProfileData Field Layout
 * 
 * This data class contains all the business profile information including
 * basic details, contact information, and business characteristics.
 * 
 * @property businessName String containing the business name
 * @property businessType String containing the type of business
 * @property businessDescription String containing the business description
 * @property yearEstablished Int containing the year the business was established
 * @property contactEmail String containing the business contact email
 * @property contactPhone String containing the business contact phone
 * @property website String containing the business website URL
 * @property licenseNumber String containing the business license number
 * @property taxId String containing the business tax ID
 * @property isActive Boolean indicating if the business is currently active
 */
data class BusinessProfileInfo(
    val businessName: String = "",
    val businessType: String = "",
    val businessDescription: String = "",
    val yearEstablished: Int = 0,
    val contactEmail: String = "",
    val contactPhone: String = "",
    val website: String = "",
    val licenseNumber: String = "",
    val taxId: String = "",
    val logoPath: String = "",
    val isActive: Boolean = true
)
