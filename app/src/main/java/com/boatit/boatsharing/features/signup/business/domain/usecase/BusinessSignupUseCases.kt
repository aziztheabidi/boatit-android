package com.boatit.boatsharing.features.signup.business.domain.usecase

import com.boatit.boatsharing.features.signup.business.model.BusinessInfoResponse
import com.boatit.boatsharing.features.signup.business.model.BusinessProfileRequest
import com.boatit.boatsharing.features.signup.business.model.GetBusinessProfileResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessAboutRequest
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessAboutResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessInfoResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessLogoResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessProfileResponse
import java.io.File

class SaveBusinessProfileUseCase(
    private val saveBusinessProfile: suspend (BusinessProfileRequest) -> Result<SaveBusinessProfileResponse>,
) {
    suspend operator fun invoke(request: BusinessProfileRequest): Result<SaveBusinessProfileResponse> {
        return saveBusinessProfile(request)
    }
}

class SaveBusinessInfoUseCase(
    private val saveBusinessInfo: suspend (
        com.boatit.boatsharing.features.signup.business.model.BusinessInfoRequest,
    ) -> Result<SaveBusinessInfoResponse>,
) {
    suspend operator fun invoke(
        request: com.boatit.boatsharing.features.signup.business.model.BusinessInfoRequest,
    ): Result<SaveBusinessInfoResponse> {
        return saveBusinessInfo(request)
    }
}

class SaveBusinessAboutUseCase(
    private val saveBusinessAbout: suspend (SaveBusinessAboutRequest) -> Result<SaveBusinessAboutResponse>,
) {
    suspend operator fun invoke(request: SaveBusinessAboutRequest): Result<SaveBusinessAboutResponse> {
        return saveBusinessAbout(request)
    }
}

class SaveBusinessLogoUseCase(
    private val saveBusinessLogo: suspend (String, File, List<File?>) -> Result<SaveBusinessLogoResponse>,
) {
    suspend operator fun invoke(
        userId: String,
        logoFile: File,
        logoFiles: List<File?>,
    ): Result<SaveBusinessLogoResponse> {
        return saveBusinessLogo(userId, logoFile, logoFiles)
    }
}

class SaveBusinessGalleryUseCase(
    private val saveBusinessGallery: suspend (String, List<File?>) -> Result<SaveBusinessLogoResponse>,
) {
    suspend operator fun invoke(
        userId: String,
        logoFiles: List<File?>,
    ): Result<SaveBusinessLogoResponse> {
        return saveBusinessGallery(userId, logoFiles)
    }
}

class FetchBusinessProfileUseCase(
    private val fetchBusinessProfile: suspend () -> Result<GetBusinessProfileResponse>,
) {
    suspend operator fun invoke(): Result<GetBusinessProfileResponse> {
        return fetchBusinessProfile()
    }
}

class FetchBusinessInfoUseCase(
    private val fetchBusinessInfo: suspend () -> Result<BusinessInfoResponse>,
) {
    suspend operator fun invoke(): Result<BusinessInfoResponse> {
        return fetchBusinessInfo()
    }
}
