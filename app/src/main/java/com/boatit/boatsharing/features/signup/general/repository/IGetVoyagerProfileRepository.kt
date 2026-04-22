package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.features.signup.general.model.GetVoyagerProfileResponse

interface IGetVoyagerProfileRepository {
    suspend fun getVoyagerProfile(): Result<GetVoyagerProfileResponse>
}
