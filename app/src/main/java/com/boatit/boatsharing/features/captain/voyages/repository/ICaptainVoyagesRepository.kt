package com.boatit.boatsharing.features.captain.voyages.repository

import com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyageResponse

interface ICaptainVoyagesRepository {
    suspend fun voyages(): Result<CaptainCompletedVoyageResponse>
}
