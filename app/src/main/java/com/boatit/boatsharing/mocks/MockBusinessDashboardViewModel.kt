package com.boatit.boatsharing.mocks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.ui.business.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.net.Uri
import android.content.Context

/**
 * MockBusinessDashboardViewModel - Mock ViewModel for BusinessDashboard testing
 * 
 * Provides identical interface to BusinessDashboardViewModel with mock data.
 * This allows complete testing of the BusinessDashboard UI without backend dependencies.
 */
class MockBusinessDashboardViewModel : ViewModel(), com.boatit.boatsharing.ui.business.viewmodel.IBusinessDashboardViewModel {
    
    // Mock session events - no session expiration for testing
    private val mockSessionEvents = MutableSharedFlow<com.boatit.boatsharing.utils.session.SessionEvent>()
    override fun getSessionEvents(): SharedFlow<com.boatit.boatsharing.utils.session.SessionEvent> = mockSessionEvents.asSharedFlow()
    
    // Mock dashboard state initialized with mock data
    private val _dashboardState = MutableStateFlow(
        BusinessDashboardState(
            isLoading = false,
            isError = false, 
            errorMessage = null,
            businessData = MockBusinessDataConfig.mockBusinessProfileInfo,
            selectedZone = MockBusinessDataConfig.mockLocationData.zone,
            selectedShore = MockBusinessDataConfig.mockLocationData.shore,
            selectedIsland = MockBusinessDataConfig.mockLocationData.island,
            isButtonEnabled = true,
            imageList = MockBusinessDataConfig.mockImageList,
            dockEnabled = MockBusinessDataConfig.mockDockData.isActive,
            dockData = MockBusinessDataConfig.mockDockData,
            locationData = MockBusinessDataConfig.mockLocationData,
            businessHours = MockBusinessDataConfig.mockBusinessHours,
            zones = MockBusinessDataConfig.mockZones,
            shores = MockBusinessDataConfig.mockShores,
            islands = MockBusinessDataConfig.mockIslands
        )
    )
    override val dashboardState: StateFlow<BusinessDashboardState> = _dashboardState.asStateFlow()
    
    // Update functions - simulate UI changes
    override fun updateLoadingState(isLoading: Boolean) {
        _dashboardState.value = _dashboardState.value.copy(isLoading = isLoading)
    }
    
    override fun updateErrorState(isError: Boolean, errorMessage: String?) {
        _dashboardState.value = _dashboardState.value.copy(
            isError = isError,
            errorMessage = errorMessage
        )
    }
    
    override fun updateBusinessData(businessData: BusinessProfileInfo?) {
        _dashboardState.value = _dashboardState.value.copy(businessData = businessData)
    }
    
    override fun updateSelectedZone(zone: String?) {
        _dashboardState.value = _dashboardState.value.copy(selectedZone = zone)
    }
    
    override fun updateSelectedShore(shore: String?) {
        _dashboardState.value = _dashboardState.value.copy(selectedShore = shore)
    }
    
    override fun updateSelectedIsland(island: String?) {
        _dashboardState.value = _dashboardState.value.copy(selectedIsland = island)
    }
    
    override fun updateImageList(imageList: List<String>) {
        _dashboardState.value = _dashboardState.value.copy(imageList = imageList)
    }
    
    override fun updateBusinessDescription(description: String) {
        val currentBusinessData = _dashboardState.value.businessData
        val updatedBusinessData = currentBusinessData?.copy(businessDescription = description) 
            ?: MockBusinessDataConfig.mockBusinessProfileInfo.copy(businessDescription = description)
        _dashboardState.value = _dashboardState.value.copy(businessData = updatedBusinessData)
    }
    
    override fun updateLocationData(locationData: LocationData?) {
        _dashboardState.value = _dashboardState.value.copy(locationData = locationData)
    }
    
    override fun updateBusinessHours(businessHours: List<BusinessHour>) {
        _dashboardState.value = _dashboardState.value.copy(businessHours = businessHours)
    }
    
    override fun saveBusinessHours(hours: List<BusinessHour>) {
        updateBusinessHours(hours)
    }
    
    override fun createDefaultBusinessHours(): List<BusinessHour> {
        return MockBusinessDataConfig.mockBusinessHours
    }
    
    override fun loadDropdownData() {
        viewModelScope.launch {
            updateLoadingState(true)
            // Simulate loading delay
            kotlinx.coroutines.delay(500)
            updateLoadingState(false)
        }
    }
    
    override fun loadBusinessData() {
        viewModelScope.launch {
            updateLoadingState(true)
            // Simulate loading delay
            kotlinx.coroutines.delay(800)
            updateLoadingState(false)
        }
    }
    
    override fun uploadImagesToBackend(selectedUris: List<Uri>, context: Context) {
        viewModelScope.launch {
            try {
                updateLoadingState(true)
                
                // Simulate upload delay
                kotlinx.coroutines.delay(1000)
                
                // Add new images to state (simulate successful upload)
                val newImageUris = selectedUris.map { uri -> uri.toString() }
                val updatedImageList = _dashboardState.value.imageList + newImageUris
                updateImageList(updatedImageList)
                
                updateLoadingState(false)
                updateErrorState(false, null)
                
            } catch (e: Exception) {
                updateLoadingState(false)
                updateErrorState(true, "Mock: Failed to upload images - ${e.message}")
            }
        }
    }
    
    override fun enableSaveButton() {
        _dashboardState.value = _dashboardState.value.copy(isButtonEnabled = true)
    }
    
    override fun disableSaveButton() {
        _dashboardState.value = _dashboardState.value.copy(isButtonEnabled = false)
    }
    
    override fun updateDockEnabled(enabled: Boolean) {
        _dashboardState.value = _dashboardState.value.copy(dockEnabled = enabled)
    }
    
    override fun initializeDashboardData() {
        viewModelScope.launch {
            updateLoadingState(true)
            // Simulate initialization delay
            kotlinx.coroutines.delay(1200)
            updateLoadingState(false)
        }
    }
    
    override fun validateForm(): Boolean {
        val state = _dashboardState.value
        
        // Always return true for mock - simulate valid form
        return state.businessData?.businessName?.isNotBlank() == true &&
               state.businessData?.businessType?.isNotBlank() == true &&
               state.selectedZone?.isNotBlank() == true &&
               state.selectedShore?.isNotBlank() == true
    }
    
    override fun checkAuthentication(): Boolean {
        // Always return true for mock - simulate authenticated user
        return true
    }
    
    override fun saveBusinessProfile() {
        viewModelScope.launch {
            if (!validateForm()) {
                updateErrorState(true, "Mock: Please fill in all required fields")
                return@launch
            }
            
            try {
                updateLoadingState(true)
                disableSaveButton()
                
                // Simulate save delay
                kotlinx.coroutines.delay(1500)
                
                updateLoadingState(false)
                enableSaveButton()
                updateErrorState(false, null)
                
                // In real app, this would show success message
                
            } catch (e: Exception) {
                updateLoadingState(false)
                enableSaveButton()
                updateErrorState(true, "Mock: Failed to save business profile - ${e.message}")
            }
        }
    }
}
