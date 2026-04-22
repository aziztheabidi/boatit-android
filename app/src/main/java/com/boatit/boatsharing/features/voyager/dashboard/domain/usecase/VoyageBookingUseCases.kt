package com.boatit.boatsharing.features.voyager.dashboard.domain.usecase

import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.CalculateFair
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyages
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyages
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatResponse

class BookVoyageUseCase(
    private val bookVoyage: suspend (BookVoyageRequest) -> Result<BookVoyageResponse>,
) {
    suspend operator fun invoke(request: BookVoyageRequest): Result<BookVoyageResponse> {
        return bookVoyage(request)
    }
}

class ConfirmBookedVoyageUseCase(
    private val confirmVoyage: suspend (ConfirmBookedVoyages) -> Result<ConfirmBookedVoyageResponse>,
) {
    suspend operator fun invoke(request: ConfirmBookedVoyages): Result<ConfirmBookedVoyageResponse> {
        return confirmVoyage(request)
    }
}

class CancelBookedVoyageUseCase(
    private val cancelVoyage: suspend (CancelBookedVoyages) -> Result<CancelBookedVoyageResponse>,
) {
    suspend operator fun invoke(request: CancelBookedVoyages): Result<CancelBookedVoyageResponse> {
        return cancelVoyage(request)
    }
}

class FindBoatUseCase(
    private val findBoat: suspend (FindBoatRequest) -> Result<FindBoatResponse>,
) {
    suspend operator fun invoke(request: FindBoatRequest): Result<FindBoatResponse> {
        return findBoat(request)
    }
}

class CalculateVoyageFareUseCase(
    private val calculateFare: suspend (String, Int, Int, Int, Int) -> Result<CalculateFair>,
) {
    suspend operator fun invoke(
        durationInHours: String,
        fromDockId: Int,
        toDockId: Int,
        voyageCategoryId: Int,
        noOfVoyagers: Int,
    ): Result<CalculateFair> {
        return calculateFare(durationInHours, fromDockId, toDockId, voyageCategoryId, noOfVoyagers)
    }
}
