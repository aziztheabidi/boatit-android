package com.boatit.boatsharing.features.captain.domain.usecase

import com.boatit.boatsharing.features.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.features.captain.availabilitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageResponse
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainFeedbackRequest
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainFeedbackResponse
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteResponse
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageStartResponse
import com.boatit.boatsharing.features.captain.dashboard.repository.ICaptainActiveVoyagesRepository
import com.boatit.boatsharing.features.captain.domain.model.CaptainActiveVoyagesDomainModel
import com.boatit.boatsharing.features.captain.domain.model.CaptainCompletedVoyagesDomainModel
import com.boatit.boatsharing.features.captain.domain.model.toDomainModel
import com.boatit.boatsharing.features.captain.voyages.repository.ICaptainVoyagesRepository

class AcceptVoyageUseCase(
    private val acceptVoyage: suspend (AcceptVoyageRequest) -> Result<AcceptVoyageResponse>,
) {
    suspend operator fun invoke(request: AcceptVoyageRequest): Result<AcceptVoyageResponse> {
        return acceptVoyage(request)
    }
}

class DeclineVoyageUseCase(
    private val declineVoyage: suspend (AcceptVoyageRequest) -> Result<AcceptVoyageResponse>,
) {
    suspend operator fun invoke(request: AcceptVoyageRequest): Result<AcceptVoyageResponse> {
        return declineVoyage(request)
    }
}

class StartVoyageUseCase(
    private val startVoyage: suspend (VoyageStartRequest) -> Result<VoyageStartResponse>,
) {
    suspend operator fun invoke(request: VoyageStartRequest): Result<VoyageStartResponse> {
        return startVoyage(request)
    }
}

class CompleteVoyageUseCase(
    private val completeVoyage: suspend (VoyageCompleteRequest) -> Result<VoyageCompleteResponse>,
) {
    suspend operator fun invoke(request: VoyageCompleteRequest): Result<VoyageCompleteResponse> {
        return completeVoyage(request)
    }
}

class CancelVoyageUseCase(
    private val cancelVoyage: suspend (VoyageCompleteRequest) -> Result<VoyageCompleteResponse>,
) {
    suspend operator fun invoke(request: VoyageCompleteRequest): Result<VoyageCompleteResponse> {
        return cancelVoyage(request)
    }
}

class FetchCaptainActiveVoyagesUseCase(
    private val captainActiveVoyagesRepository: ICaptainActiveVoyagesRepository,
) {
    suspend operator fun invoke(): Result<CaptainActiveVoyagesDomainModel> {
        return captainActiveVoyagesRepository.voyages().map { it.toDomainModel() }
    }
}

class SubmitCaptainFeedbackUseCase(
    private val submitCaptainFeedback: suspend (CaptainFeedbackRequest) -> Result<CaptainFeedbackResponse>,
) {
    suspend operator fun invoke(request: CaptainFeedbackRequest): Result<CaptainFeedbackResponse> {
        return submitCaptainFeedback(request)
    }
}

class FetchCaptainCompletedVoyagesUseCase(
    private val captainVoyagesRepository: ICaptainVoyagesRepository,
) {
    suspend operator fun invoke(): Result<CaptainCompletedVoyagesDomainModel> {
        return captainVoyagesRepository.voyages().map { it.toDomainModel() }
    }
}

class UpdateCaptainAvailabilityUseCase(
    private val updateAvailability: suspend (CaptainAvailabilityRequest) -> Result<CaptainAvailabilityResponse>,
) {
    suspend operator fun invoke(request: CaptainAvailabilityRequest): Result<CaptainAvailabilityResponse> {
        return updateAvailability(request)
    }
}
