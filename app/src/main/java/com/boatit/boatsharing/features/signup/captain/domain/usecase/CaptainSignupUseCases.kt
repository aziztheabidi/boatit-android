package com.boatit.boatsharing.features.signup.captain.domain.usecase

import com.boatit.boatsharing.features.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.features.signup.captain.model.CaptainProfileResponse
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainBoatResponse
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainBoatResponse
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainDocumentRequest
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainDocumentResponse

class SaveCaptainProfileUseCase(
    private val saveCaptainProfile: suspend (CaptainProfileRequest) -> Result<CaptainProfileResponse>,
) {
    suspend operator fun invoke(request: CaptainProfileRequest): Result<CaptainProfileResponse> {
        return saveCaptainProfile(request)
    }
}

class SaveCaptainDocsUseCase(
    private val saveCaptainDocs: suspend (SaveCaptainDocumentRequest) -> Result<SaveCaptainDocumentResponse>,
) {
    suspend operator fun invoke(request: SaveCaptainDocumentRequest): Result<SaveCaptainDocumentResponse> {
        return saveCaptainDocs(request)
    }
}

class SaveCaptainBoatUseCase(
    private val saveCaptainBoat: suspend (SaveCaptainBoatRequest) -> Result<SaveCaptainBoatResponse>,
) {
    suspend operator fun invoke(request: SaveCaptainBoatRequest): Result<SaveCaptainBoatResponse> {
        return saveCaptainBoat(request)
    }
}

class FetchCaptainProfileUseCase(
    private val fetchCaptainProfile: suspend () -> Result<GetCaptainProfileResponse>,
) {
    suspend operator fun invoke(): Result<GetCaptainProfileResponse> {
        return fetchCaptainProfile()
    }
}

class FetchCaptainDocsUseCase(
    private val fetchCaptainDocs: suspend () -> Result<GetCaptainDocumentResponse>,
) {
    suspend operator fun invoke(): Result<GetCaptainDocumentResponse> {
        return fetchCaptainDocs()
    }
}

class FetchCaptainBoatUseCase(
    private val fetchCaptainBoat: suspend () -> Result<GetCaptainBoatResponse>,
) {
    suspend operator fun invoke(): Result<GetCaptainBoatResponse> {
        return fetchCaptainBoat()
    }
}
