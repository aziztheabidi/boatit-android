package com.boatit.boatsharing.features.business.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.requiresReLogin
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.business.domain.usecase.FetchBusinessDashboardProfileUseCase
import com.boatit.boatsharing.features.business.domain.usecase.FetchBusinessDocksUseCase
import com.boatit.boatsharing.features.business.domain.usecase.SaveBusinessDashboardProfileUseCase
import com.boatit.boatsharing.features.business.model.BusinessDashboardState
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiEffect
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiEvent
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiState
import com.boatit.boatsharing.features.business.model.BusinessHour
import com.boatit.boatsharing.features.business.model.BusinessRequest
import com.boatit.boatsharing.features.business.model.DockData
import com.boatit.boatsharing.features.business.model.LocationData
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessGalleryUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class BusinessDashboardViewModel(
    private val fetchBusinessDashboardProfileUseCase: FetchBusinessDashboardProfileUseCase,
    private val fetchBusinessDocksUseCase: FetchBusinessDocksUseCase,
    private val saveBusinessDashboardProfileUseCase: SaveBusinessDashboardProfileUseCase,
    private val saveBusinessGalleryUseCase: SaveBusinessGalleryUseCase,
    private val userSessionStore: UserSessionStore,
) : BaseViewModel<BusinessDashboardUiState, BusinessDashboardUiEvent, BusinessDashboardUiEffect>(
        BusinessDashboardState(),
    ),
    IBusinessDashboardViewModel {
    override val dashboardState: StateFlow<BusinessDashboardState> = uiState

    override fun onEvent(event: BusinessDashboardUiEvent) {
        when (event) {
            BusinessDashboardUiEvent.Initialize -> initializeDashboardData()
            is BusinessDashboardUiEvent.UpdateImageList -> updateImageList(event.imageList)
            is BusinessDashboardUiEvent.UploadGalleryImages -> uploadImagesToBackend(event.files)
            is BusinessDashboardUiEvent.UploadLogo -> {
                updateImageList(listOf(event.imageUrl) + currentState.imageList)
                uploadImagesToBackend(listOf(event.file))
            }
            is BusinessDashboardUiEvent.RemoveImage -> {
                val updated = currentState.imageList.toMutableList().apply { remove(event.imageUrl) }
                updateImageList(updated)
                emitEffect(BusinessDashboardUiEffect.ShowToast("Image removed"))
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

    override fun checkAuthentication(): Boolean = userSessionStore.currentUserId().isNotBlank()

    override fun initializeDashboardData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, isError = false, errorMessage = null) }

            when (val profileResult = fetchBusinessDashboardProfileUseCase().toResource()) {
                is Resource.Success -> {
                    val response = profileResult.data
                    val business = response.obj
                    val currentHours = business?.BusinessHours
                    val hours = if (currentHours.isNullOrEmpty()) createDefaultBusinessHours() else currentHours

                    val locationData =
                        LocationData(
                            location = business?.Location.orEmpty(),
                            state = business?.State.orEmpty(),
                            city = business?.City.orEmpty(),
                            zipCode = business?.ZipCode.orEmpty(),
                            latitude = business?.Latitude ?: 0.0,
                            longitude = business?.Longitude ?: 0.0,
                        )

                    updateState {
                        copy(
                            businessData = business,
                            imageList = business?.ImagesPath ?: emptyList(),
                            locationData = locationData,
                            businessHours = hours,
                            dockData =
                                DockData(
                                    name = business?.Name.orEmpty(),
                                    address = business?.Address.orEmpty(),
                                    description = business?.Description.orEmpty(),
                                ),
                            dockEnabled = business?.IsDock ?: false,
                            selectedZone = business?.ZoneName.orEmpty(),
                            selectedShore = business?.ShoreName.orEmpty(),
                            selectedIsland = business?.IslandName.orEmpty(),
                            selectedZoneId = business?.ZoneId ?: 0,
                            selectedShoreId = business?.ShoreId ?: 0,
                            selectedIslandId = business?.IslandId ?: 0,
                            isLoading = false,
                            isError = false,
                            errorMessage = null,
                        )
                    }
                    validateForm()
                }

                is Resource.Error -> {
                    val message = profileResult.error.toMessage()
                    if (profileResult.error.requiresReLogin()) {
                        emitEffect(BusinessDashboardUiEffect.SessionExpired)
                    }
                    updateState {
                        copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = message,
                        )
                    }
                    emitEffect(BusinessDashboardUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }

            when (val docksResult = fetchBusinessDocksUseCase().toResource()) {
                is Resource.Success -> {
                    val response = docksResult.data
                    updateState {
                        copy(
                            zones = response.obj?.Zone ?: emptyList(),
                            shores = response.obj?.Shore ?: emptyList(),
                            islands = response.obj?.Island ?: emptyList(),
                        )
                    }
                }

                is Resource.Error -> {
                    val message = docksResult.error.toMessage()
                    if (docksResult.error.requiresReLogin()) {
                        emitEffect(BusinessDashboardUiEffect.SessionExpired)
                    }
                    updateState {
                        copy(
                            isError = true,
                            errorMessage = message,
                        )
                    }
                    emitEffect(BusinessDashboardUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    override fun updateImageList(imageList: List<String>) {
        updateState { copy(imageList = imageList) }
    }

    override fun updateLocationData(locationData: LocationData) {
        updateState { copy(locationData = locationData) }
        validateForm()
    }

    override fun updateSelectedZone(
        zoneId: Int,
        zone: String,
    ) {
        updateState { copy(selectedZone = zone, selectedZoneId = zoneId) }
        validateForm()
    }

    override fun updateSelectedShore(
        shoreId: Int,
        shore: String,
    ) {
        updateState { copy(selectedShore = shore, selectedShoreId = shoreId) }
        validateForm()
    }

    override fun updateSelectedIsland(
        islandId: Int,
        island: String,
    ) {
        updateState { copy(selectedIsland = island, selectedIslandId = islandId) }
        validateForm()
    }

    override fun updateDockEnabled(enabled: Boolean) {
        updateState { copy(dockEnabled = enabled) }
        validateForm()
    }

    override fun updateDockData(dockData: DockData) {
        updateState { copy(dockData = dockData) }
        validateForm()
    }

    override fun saveBusinessHours(hours: List<BusinessHour>) {
        updateState { copy(businessHours = hours) }
    }

    override fun saveBusinessProfile() {
        val state = currentState
        val business = state.businessData
        val businessRequest =
            BusinessRequest(
                Location = state.locationData.location.ifBlank { business?.Location.orEmpty() },
                BusinessHours = state.businessHours,
                IsDock = state.dockEnabled,
                ShoreId = state.selectedShoreId,
                Name = state.dockData.name,
                ZoneId = state.selectedZoneId,
                IslandId = state.selectedIslandId,
                State = state.locationData.state.ifBlank { business?.State.orEmpty() },
                City = state.locationData.city.ifBlank { business?.City.orEmpty() },
                ZipCode = state.locationData.zipCode.ifBlank { business?.ZipCode.orEmpty() },
                ShoreLine = state.selectedShore,
                Address = state.dockData.address,
                Latitude = state.locationData.latitude,
                Longitude = state.locationData.longitude,
                Description = state.dockData.description,
            )

        viewModelScope.launch {
            updateState { copy(isLoading = true, isError = false, errorMessage = null) }

            when (val saveResult = saveBusinessDashboardProfileUseCase(businessRequest).toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, isError = false, errorMessage = null) }
                }

                is Resource.Error -> {
                    val message = saveResult.error.toMessage()
                    if (saveResult.error.requiresReLogin()) {
                        emitEffect(BusinessDashboardUiEffect.SessionExpired)
                    }
                    updateState {
                        copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = message,
                        )
                    }
                    emitEffect(BusinessDashboardUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    override fun validateForm() {
        val state = currentState
        val isDockValid =
            if (state.dockEnabled) {
                state.dockData.name.isNotBlank() && state.dockData.address.isNotBlank()
            } else {
                true
            }

        val isLocationValid = state.selectedZoneId > 0 && state.selectedShoreId > 0 && state.selectedIslandId > 0

        updateState {
            copy(
                isButtonEnabled = isDockValid && isLocationValid && state.businessHours.isNotEmpty(),
            )
        }
    }

    override fun uploadImagesToBackend(files: List<File>) {
        val userId = userSessionStore.currentUserId()
        if (userId.isBlank()) return
        if (files.isEmpty()) return

        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            when (val uploadResult = saveBusinessGalleryUseCase(userId, files).toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false) }
                }

                is Resource.Error -> {
                    val message = uploadResult.error.toMessage()
                    if (uploadResult.error.requiresReLogin()) {
                        emitEffect(BusinessDashboardUiEffect.SessionExpired)
                    }
                    updateState {
                        copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = message,
                        )
                    }
                    emitEffect(BusinessDashboardUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    override fun createDefaultBusinessHours(): List<BusinessHour> {
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        return daysOfWeek.map { day ->
            BusinessHour(Day = day, StartTime = "09:00:00", EndTimeTime = "17:00:00")
        }
    }
}
