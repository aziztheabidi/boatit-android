package com.boatit.boatsharing.features.voyager.dashboard.domain.usecase

import com.boatit.boatsharing.features.chat.model.ActiveVoyagersResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.model.SponsorPaymentsDomainModel
import com.boatit.boatsharing.features.voyager.dashboard.domain.model.toDomainModel
import com.boatit.boatsharing.features.voyager.dashboard.model.BusinessRelationshipResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.FollowedVoyagersResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.NearbyPlacesResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorPayments
import com.boatit.boatsharing.features.voyager.dashboard.model.TravelNowResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageCategoryDropdownResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFeedbackRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFeedbackResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerVoyagesResponse

class FetchVoyagerPastVoyagesUseCase(
    private val fetchVoyages: suspend () -> Result<VoyagerVoyagesResponse>,
) {
    suspend operator fun invoke(): Result<VoyagerVoyagesResponse> {
        return fetchVoyages()
    }
}

class SubmitVoyagerFeedbackUseCase(
    private val submitFeedback: suspend (VoyagerFeedbackRequest) -> Result<VoyagerFeedbackResponse>,
) {
    suspend operator fun invoke(request: VoyagerFeedbackRequest): Result<VoyagerFeedbackResponse> {
        return submitFeedback(request)
    }
}

class FollowBusinessUseCase(
    private val followBusiness: suspend (VoyagerFollowBusinessRequest) -> Result<VoyagerFollowBusinessResponse>,
) {
    suspend operator fun invoke(request: VoyagerFollowBusinessRequest): Result<VoyagerFollowBusinessResponse> {
        return followBusiness(request)
    }
}

class UnFollowBusinessUseCase(
    private val unFollowBusiness: suspend (VoyagerFollowBusinessRequest) -> Result<VoyagerFollowBusinessResponse>,
) {
    suspend operator fun invoke(request: VoyagerFollowBusinessRequest): Result<VoyagerFollowBusinessResponse> {
        return unFollowBusiness(request)
    }
}

class FetchTravelNowVoyagesUseCase(
    private val fetchTravelNow: suspend () -> Result<TravelNowResponse>,
) {
    suspend operator fun invoke(): Result<TravelNowResponse> {
        return fetchTravelNow()
    }
}

class FetchBusinessRelationshipsUseCase(
    private val fetchBusinessRelationships: suspend () -> Result<BusinessRelationshipResponse>,
) {
    suspend operator fun invoke(): Result<BusinessRelationshipResponse> {
        return fetchBusinessRelationships()
    }
}

class FetchFollowedVoyagersUseCase(
    private val fetchFollowedVoyagers: suspend () -> Result<FollowedVoyagersResponse>,
) {
    suspend operator fun invoke(): Result<FollowedVoyagersResponse> {
        return fetchFollowedVoyagers()
    }
}

class FetchSponsorPaymentsUseCase(
    private val fetchSponsorPayments: suspend () -> Result<SponsorPayments>,
) {
    suspend operator fun invoke(): Result<SponsorPaymentsDomainModel> {
        return fetchSponsorPayments().map { it.toDomainModel() }
    }
}

class FetchActiveVoyagersUseCase(
    private val fetchActiveVoyagers: suspend () -> Result<ActiveVoyagersResponse>,
) {
    suspend operator fun invoke(): Result<ActiveVoyagersResponse> {
        return fetchActiveVoyagers()
    }
}

class FetchNearbyPlacesUseCase(
    private val fetchNearbyPlaces: suspend () -> Result<NearbyPlacesResponse>,
) {
    suspend operator fun invoke(): Result<NearbyPlacesResponse> {
        return fetchNearbyPlaces()
    }
}

class FetchVoyageCategoriesUseCase(
    private val fetchVoyageCategories: suspend () -> Result<VoyageCategoryDropdownResponse>,
) {
    suspend operator fun invoke(): Result<VoyageCategoryDropdownResponse> {
        return fetchVoyageCategories()
    }
}
