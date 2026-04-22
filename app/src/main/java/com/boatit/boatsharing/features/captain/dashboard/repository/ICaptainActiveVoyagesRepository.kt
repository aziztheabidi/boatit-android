package com.boatit.boatsharing.features.captain.dashboard.repository

import com.boatit.boatsharing.features.captain.dashboard.model.CaptainActiveVoyagesResponse

interface ICaptainActiveVoyagesRepository {
    suspend fun voyages(): Result<CaptainActiveVoyagesResponse>
}
