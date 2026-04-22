package com.boatit.boatsharing.features.business.domain.usecase

import com.boatit.boatsharing.features.business.model.BusinessRequest
import com.boatit.boatsharing.features.business.model.DeleteRequest
import com.boatit.boatsharing.features.business.model.DocksDropdownResponse
import com.boatit.boatsharing.features.business.model.GetBusinessResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessInfoResponse

class FetchBusinessDashboardProfileUseCase(
    private val fetchBusinessProfile: suspend () -> Result<GetBusinessResponse>,
) {
    suspend operator fun invoke(): Result<GetBusinessResponse> {
        return fetchBusinessProfile()
    }
}

class FetchBusinessDocksUseCase(
    private val fetchBusinessDocks: suspend () -> Result<DocksDropdownResponse>,
) {
    suspend operator fun invoke(): Result<DocksDropdownResponse> {
        return fetchBusinessDocks()
    }
}

class SaveBusinessDashboardProfileUseCase(
    private val saveBusinessProfile: suspend (BusinessRequest) -> Result<SaveBusinessInfoResponse>,
) {
    suspend operator fun invoke(request: BusinessRequest): Result<SaveBusinessInfoResponse> {
        return saveBusinessProfile(request)
    }
}

class DeleteBusinessDashboardImageUseCase(
    private val deleteBusinessImage: suspend (DeleteRequest) -> Result<SaveBusinessInfoResponse>,
) {
    suspend operator fun invoke(request: DeleteRequest): Result<SaveBusinessInfoResponse> {
        return deleteBusinessImage(request)
    }
}
