package com.boatit.boatsharing.features.business.viewmodel

import com.boatit.boatsharing.features.business.model.BusinessDashboardState
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiEffect
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiEvent
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiState
import com.boatit.boatsharing.features.business.model.BusinessHour
import com.boatit.boatsharing.features.business.model.DockData
import com.boatit.boatsharing.features.business.model.LocationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface IBusinessDashboardViewModel {
    val uiState: StateFlow<BusinessDashboardUiState>
    val uiEffect: Flow<BusinessDashboardUiEffect>

    fun onEvent(event: BusinessDashboardUiEvent)

    val dashboardState: StateFlow<BusinessDashboardState>

    fun checkAuthentication(): Boolean

    fun initializeDashboardData()

    fun updateImageList(imageList: List<String>)

    fun updateLocationData(locationData: LocationData)

    fun updateSelectedZone(
        zoneId: Int,
        zone: String,
    )

    fun updateSelectedShore(
        shoreId: Int,
        shore: String,
    )

    fun updateSelectedIsland(
        islandId: Int,
        island: String,
    )

    fun updateDockEnabled(enabled: Boolean)

    fun updateDockData(dockData: DockData)

    fun saveBusinessHours(hours: List<BusinessHour>)

    fun saveBusinessProfile()

    fun validateForm()

    fun uploadImagesToBackend(files: List<File>)

    fun createDefaultBusinessHours(): List<BusinessHour>
}
