package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorPayments

interface ISponsorVoyagesRepository {
    suspend fun voyages(): Result<SponsorPayments>
}
