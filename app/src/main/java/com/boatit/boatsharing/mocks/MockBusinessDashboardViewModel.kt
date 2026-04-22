package com.boatit.boatsharing.mocks

import com.boatit.boatsharing.features.business.model.BusinessDashboardState
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiEffect
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiEvent
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiState
import com.boatit.boatsharing.features.business.model.BusinessHour
import com.boatit.boatsharing.features.business.model.DockData
import com.boatit.boatsharing.features.business.model.LocationData
import com.boatit.boatsharing.features.business.viewmodel.IBusinessDashboardViewModel
import com.boatit.boatsharing.data.local.session.SessionEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class MockBusinessDashboardViewModel : IBusinessDashboardViewModel {
    private val _dashboardState = MutableStateFlow(MockBusinessDataConfig.initialState)
    override val uiState: StateFlow<BusinessDashboardUiState> = _dashboardState.asStateFlow()
    override val dashboardState: StateFlow<BusinessDashboardState> = uiState

    private val _uiEffects = MutableSharedFlow<BusinessDashboardUiEffect>()
    override val uiEffects: SharedFlow<BusinessDashboardUiEffect> = _uiEffects

    private val _sessionEvents = MutableSharedFlow<SessionEvent>()

    override fun getSessionEvents(): SharedFlow<SessionEvent> = _sessionEvents

    override fun onEvent(event: BusinessDashboardUiEvent) {
        when (event) {
            BusinessDashboardUiEvent.Initialize -> initializeDashboardData()
            is BusinessDashboardUiEvent.UpdateImageList -> updateImageList(event.imageList)
            is BusinessDashboardUiEvent.UploadGalleryImages -> uploadImagesToBackend(event.files)
            is BusinessDashboardUiEvent.UploadLogo -> {
                val updated = listOf(event.imageUrl) + _dashboardState.value.imageList
                updateImageList(updated)
                uploadImagesToBackend(listOf(event.file))
            }
            is BusinessDashboardUiEvent.RemoveImage -> {
                updateImageList(_dashboardState.value.imageList.filterNot { it == event.imageUrl })
                _uiEffects.tryEmit(BusinessDashboardUiEffect.ShowToast("Image removed"))
            }
            is BusinessDashboardUiEvent.UpdateLocationData -> updateLocationData(event.locationData)
            is BusinessDashboardUiEvent.UpdateSelectedZone -> updateSelectedZone(event.zoneId, event.zoneName)
            is BusinessDashboardUiEvent.UpdateSelectedShore -> updateSelectedShore(event.shoreId, event.shoreName)
            is BusinessDashboardUiEvent.UpdateSelectedIsland -> updateSelectedIsland(event.islandId, event.islandName)
            is BusinessDashboardUiEvent.UpdateDockEnabled -> updateDockEnabled(event.enabled)
            is BusinessDashboardUiEvent.UpdateDockData -> updateDockData(event.dockData)
            is BusinessDashboardUiEvent.SaveBusinessHours -> saveBusinessHours(event.hours)
            BusinessDashboardUiEvent.SaveBusinessProfile -> saveBusinessProfile()
        }
    }

    override fun checkAuthentication(): Boolean = true

    override fun initializeDashboardData() {
        _dashboardState.value = _dashboardState.value.copy(isLoading = false)
    }

    override fun updateImageList(imageList: List<String>) {
        _dashboardState.value = _dashboardState.value.copy(imageList = imageList)
    }

    override fun updateLocationData(locationData: LocationData) {
        _dashboardState.value = _dashboardState.value.copy(locationData = locationData)
    }

    override fun updateSelectedZone(
        zoneId: Int,
        zone: String,
    ) {
        _dashboardState.value = _dashboardState.value.copy(selectedZone = zone, selectedZoneId = zoneId)
    }

    override fun updateSelectedShore(
        shoreId: Int,
        shore: String,
    ) {
        _dashboardState.value = _dashboardState.value.copy(selectedShore = shore, selectedShoreId = shoreId)
    }

    override fun updateSelectedIsland(
        islandId: Int,
        island: String,
    ) {
        _dashboardState.value = _dashboardState.value.copy(selectedIsland = island, selectedIslandId = islandId)
    }

    override fun updateDockEnabled(enabled: Boolean) {
        _dashboardState.value = _dashboardState.value.copy(dockEnabled = enabled)
    }

    override fun updateDockData(dockData: DockData) {
        _dashboardState.value = _dashboardState.value.copy(dockData = dockData)
    }

    override fun saveBusinessHours(hours: List<BusinessHour>) {
        _dashboardState.value = _dashboardState.value.copy(businessHours = hours)
    }

    override fun saveBusinessProfile() {
        _dashboardState.value = _dashboardState.value.copy(isError = false, errorMessage = null)
    }

    override fun validateForm() {
        _dashboardState.value = _dashboardState.value.copy(isButtonEnabled = true)
    }

    override fun uploadImagesToBackend(files: List<File>) {
        if (files.isEmpty()) return
    }

    override fun createDefaultBusinessHours(): List<BusinessHour> {
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        return daysOfWeek.map { day ->
            BusinessHour(Day = day, StartTime = "09:00:00", EndTimeTime = "17:00:00")
        }
    }
}
