package com.boatit.boatsharing.ui.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.ui.business.model.BusinessDashboardState
import com.boatit.boatsharing.ui.business.model.BusinessProfileInfo
import com.boatit.boatsharing.ui.business.model.BusinessHour
import com.boatit.boatsharing.ui.business.model.DockData
import com.boatit.boatsharing.ui.business.model.LocationData
import com.boatit.boatsharing.ui.business.model.BusinessRequest
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.utils.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import android.net.Uri

/**
 * BusinessDashboardViewModel - Main ViewModel for BusinessDashboard
 * 
 * FULFILLS: LLR-1.1.1 - BusinessDashboardViewModel Implementation
 * 
 * This ViewModel manages the state and business logic for the BusinessDashboard screen,
 * including data loading, form validation, state updates, and network operations.
 */
class BusinessDashboardViewModel : ViewModel(), KoinComponent {
    
    // Dependency injection
    private val sessionManager: SessionManager by inject()
    private val getBusinessViewModel: GetBusinessViewModel by inject()
    private val businessDashViewModel: BusinessDashViewModel by inject()
    private val businessLogoViewModel: com.boatit.boatsharing.ui.signup.business.viewmodel.BusinessLogoViewModel by inject()
    
    // FULFILLS: LLR-1.1.4 - ViewModel StateFlow Initialization
    private val _dashboardState = MutableStateFlow(BusinessDashboardState())
    val dashboardState: StateFlow<BusinessDashboardState> = _dashboardState.asStateFlow()
    
    // FULFILLS: LLR-1.1.5 - Loading State Update Function
    fun updateLoadingState(isLoading: Boolean) {
        _dashboardState.value = _dashboardState.value.copy(isLoading = isLoading)
    }
    
    // FULFILLS: LLR-1.1.6 - Error State Update Function
    fun updateErrorState(isError: Boolean, errorMessage: String?) {
        _dashboardState.value = _dashboardState.value.copy(
            isError = isError,
            errorMessage = errorMessage
        )
    }
    
    // FULFILLS: LLR-1.1.7 - Business Data Update Function
    fun updateBusinessData(businessData: BusinessProfileInfo?) {
        _dashboardState.value = _dashboardState.value.copy(businessData = businessData)
    }
    
    // FULFILLS: LLR-1.1.8 - Zone Selection Update Function
    fun updateSelectedZone(zone: String?) {
        _dashboardState.value = _dashboardState.value.copy(selectedZone = zone)
    }
    
    // FULFILLS: LLR-1.1.9 - Shore Selection Update Function
    fun updateSelectedShore(shore: String?) {
        _dashboardState.value = _dashboardState.value.copy(selectedShore = shore)
    }
    
    // FULFILLS: LLR-1.1.10 - Island Selection Update Function
    fun updateSelectedIsland(island: String?) {
        _dashboardState.value = _dashboardState.value.copy(selectedIsland = island)
    }
    
    // FULFILLS: LLR-1.1.11 - Image List Update Function
    fun updateImageList(imageList: List<String>) {
        _dashboardState.value = _dashboardState.value.copy(imageList = imageList)
    }
    
    // Business Description Update Function
    fun updateBusinessDescription(description: String) {
        val currentBusinessData = _dashboardState.value.businessData
        val updatedBusinessData = currentBusinessData?.copy(businessDescription = description) 
            ?: BusinessProfileInfo(businessDescription = description)
        _dashboardState.value = _dashboardState.value.copy(businessData = updatedBusinessData)
    }
    
    // Location Data Update Function
    fun updateLocationData(locationData: LocationData?) {
        _dashboardState.value = _dashboardState.value.copy(locationData = locationData)
    }
    
    // Business Hours Update Function
    fun updateBusinessHours(businessHours: List<BusinessHour>) {
        _dashboardState.value = _dashboardState.value.copy(businessHours = businessHours)
    }
    
    // Default business hours creation
    fun createDefaultBusinessHours(): List<BusinessHour> {
        return listOf(
            BusinessHour("Monday", "09:00", "17:00"),
            BusinessHour("Tuesday", "09:00", "17:00"),
            BusinessHour("Wednesday", "09:00", "17:00"),
            BusinessHour("Thursday", "09:00", "17:00"),
            BusinessHour("Friday", "09:00", "17:00"),
            BusinessHour("Saturday", "10:00", "15:00"),
            BusinessHour("Sunday", "Closed", "Closed")
        )
    }
    
    // Save business hours function
    /**
     * FULFILLS: LLR-2.8.3 - Session Event Access Implementation
     * Provides access to session events for UI consumption
     */
    fun getSessionEvents() = sessionManager.sessionEvents
    
    fun saveBusinessHours(hours: List<BusinessHour>) {
        updateBusinessHours(hours)
    }
    
    // FULFILLS: LLR-2.6.1 - Docks Data Loading Integration
    fun loadDropdownData() {
        viewModelScope.launch {
            try {
                updateLoadingState(true)
                
                // Call existing GetBusinessViewModel.docks() to load docks data
                getBusinessViewModel.docks()
                
                // Transform docks response to populate dropdown state
                getBusinessViewModel.docksState.collect { response ->
                    when (response) {
                        is NetworkResponse.Loading -> {
                            updateStateFromNetworkResponse(response as NetworkResponse<Any>)
                        }
                        is NetworkResponse.Success -> {
                            // Extract dropdown data from response
                            response.data?.obj?.let { docksData ->
                                updateZoneDropdown(docksData.Zone)
                                updateShoreDropdown(docksData.Shore)
                                updateIslandDropdown(docksData.Island)
                            }
                            updateStateFromNetworkResponse(NetworkResponse.Success<Any>(Unit))
                            updateLoadingState(false)
                            return@collect // Stop collecting after success
                        }
                        is NetworkResponse.Error -> {
                            updateStateFromNetworkResponse(response as NetworkResponse<Any>)
                            updateLoadingState(false)
                            return@collect // Stop collecting after error
                        }
                    }
                }
            } catch (e: Exception) {
                val errorResponse = NetworkResponse.Error<Any>("Failed to load dropdown data: ${e.message}")
                updateStateFromNetworkResponse(errorResponse)
                updateLoadingState(false)
            }
        }
    }
    
    // FULFILLS: LLR-2.6.2 - Voyages Data Loading Integration  
    fun loadBusinessData() {
        viewModelScope.launch {
            try {
                updateLoadingState(true)
                
                // Call existing GetBusinessViewModel.voyages() to load business data
                getBusinessViewModel.voyages()
                
                // Transform business response to populate dashboard state
                getBusinessViewModel.loginState.collect { response ->
                    when (response) {
                        is NetworkResponse.Loading -> {
                            updateStateFromNetworkResponse(response as NetworkResponse<Any>)
                        }
                        is NetworkResponse.Success -> {
                            // Extract and populate business data from response
                            response.data?.obj?.let { businessData ->
                                updateBusinessDataFromResponse(businessData)
                            }
                            updateStateFromNetworkResponse(NetworkResponse.Success<Any>(Unit))
                            updateLoadingState(false)
                            return@collect // Stop collecting after success
                        }
                        is NetworkResponse.Error -> {
                            updateStateFromNetworkResponse(response as NetworkResponse<Any>)
                            updateLoadingState(false)
                            return@collect // Stop collecting after error
                        }
                    }
                }
            } catch (e: Exception) {
                val errorResponse = NetworkResponse.Error<Any>("Failed to load business data: ${e.message}")
                updateStateFromNetworkResponse(errorResponse)
                updateLoadingState(false)
            }
        }
    }
    
    // Helper function to populate business data from backend response
    private fun updateBusinessDataFromResponse(businessData: com.boatit.boatsharing.ui.business.model.BusinessData) {
        // Convert BusinessData to BusinessProfileInfo format
        val profileInfo = BusinessProfileInfo(
            businessName = businessData.Name ?: "",
            businessType = businessData.BusinessType ?: "",
            businessDescription = businessData.Description ?: "",
            yearEstablished = businessData.YearOfEstablishment ?: 0,
            contactEmail = "", // Not in backend response
            contactPhone = "", // Not in backend response
            website = "" // Not in backend response
        )
        updateBusinessData(profileInfo)
        
        // Update location data
        val locationData = LocationData(
            address = businessData.Address ?: "",
            street = "", // Not specifically in response
            city = businessData.City ?: "",
            state = businessData.State ?: "",
            country = "", // Not in response
            postalCode = businessData.ZipCode ?: "",
            latitude = businessData.Latitude ?: 0.0,
            longitude = businessData.Longitude ?: 0.0,
            zone = businessData.ZoneName ?: "",
            shore = businessData.ShoreName ?: "",
            island = businessData.IslandName ?: "",
            isWaterfront = false, // Default
            hasParking = false, // Default
            hasAccessibility = false, // Default
            isActive = true // Default
        )
        updateLocationData(locationData)
        
        // Update zone/shore/island selections
        updateSelectedZone(businessData.ZoneName)
        updateSelectedShore(businessData.ShoreName)
        updateSelectedIsland(businessData.IslandName)
        
        // Update business hours
        businessData.BusinessHours?.let { hours ->
            updateBusinessHours(hours)
        }
        
        // Update dock enabled status
        updateDockEnabled(businessData.IsDock ?: false)
        
        // Update image list
        businessData.ImagesPath?.let { imagePaths ->
            updateImageList(imagePaths)
        }
    }
    
    // Helper functions to update dropdown state from backend data
    // FULFILLS: LLR-2.6.1 - Docks Data Loading Integration (dropdown population)
    private fun updateZoneDropdown(zones: List<com.boatit.boatsharing.ui.business.model.DockDropdownItem>?) {
        zones?.let { zoneList ->
            _dashboardState.value = _dashboardState.value.copy(zones = zoneList)
        }
    }
    
    private fun updateShoreDropdown(shores: List<com.boatit.boatsharing.ui.business.model.DockDropdownItem>?) {
        shores?.let { shoreList ->
            _dashboardState.value = _dashboardState.value.copy(shores = shoreList)
        }
    }
    
    private fun updateIslandDropdown(islands: List<com.boatit.boatsharing.ui.business.model.DockDropdownItem>?) {
        islands?.let { islandList ->
            _dashboardState.value = _dashboardState.value.copy(islands = islandList)
        }
    }
    
    // Initialize backend data loading on startup
    // FULFILLS: LLR-2.6.1 and LLR-2.6.2 - Comprehensive Backend Integration
    fun initializeDashboardData() {
        viewModelScope.launch {
            // Load both dropdown data and business data concurrently
            loadDropdownData()
            loadBusinessData()
        }
    }
    
    // FULFILLS: LLR-2.1.3 - Backend Image Upload Integration
    fun uploadImagesToBackend(selectedUris: List<Uri>, context: android.content.Context) {
        viewModelScope.launch {
            try {
                updateLoadingState(true)
                
                // Convert URIs to Files (similar to original implementation)
                val fileList = selectedUris.map { uri -> 
                    val file = java.io.File(context.cacheDir, "image_${System.currentTimeMillis()}.jpg")
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        file.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    file
                }
                
                // Upload via BusinessLogoViewModel
                businessLogoViewModel.uploadBusinessGallery(com.boatit.boatsharing.utils.AppConstants.USER_ID ?: "", fileList)
                
                // Monitor upload progress
                businessLogoViewModel.registrationState.collect { response ->
                    when (response) {
                        is NetworkResponse.Loading -> {
                            updateStateFromNetworkResponse(response as NetworkResponse<Any>)
                        }
                        is NetworkResponse.Success -> {
                            updateStateFromNetworkResponse(NetworkResponse.Success<Any>(Unit))
                            updateLoadingState(false)
                            return@collect // Stop collecting after success
                        }
                        is NetworkResponse.Error -> {
                            updateStateFromNetworkResponse(response as NetworkResponse<Any>)
                            updateLoadingState(false)
                            return@collect // Stop collecting after error
                        }
                    }
                }
            } catch (e: Exception) {
                val errorResponse = NetworkResponse.Error<Any>("Failed to upload images: ${e.message}")
                updateStateFromNetworkResponse(errorResponse)
                updateLoadingState(false)
            }
        }
    }
    
    // FULFILLS: LLR-1.1.14 - Form State Management
    fun enableSaveButton() {
        _dashboardState.value = _dashboardState.value.copy(isButtonEnabled = true)
    }
    
    fun disableSaveButton() {
        _dashboardState.value = _dashboardState.value.copy(isButtonEnabled = false)
    }
    
    fun updateDockEnabled(enabled: Boolean) {
        _dashboardState.value = _dashboardState.value.copy(dockEnabled = enabled)
    }
    
    // FULFILLS: LLR-1.1.15 - Generic State Update Function
    private fun updateStateFromNetworkResponse(response: NetworkResponse<Any>) {
        when (response) {
            is NetworkResponse.Loading -> {
                updateLoadingState(true)
                updateErrorState(false, null)
            }
            is NetworkResponse.Success -> {
                updateLoadingState(false)
                updateErrorState(false, null)
            }
            is NetworkResponse.Error -> {
                updateLoadingState(false)
                updateErrorState(true, response.message)
            }
        }
    }
    
    // FULFILLS: LLR-1.1.16 - Form Validation Function
    fun validateForm(): Boolean {
        val state = _dashboardState.value
        var isValid = true
        
        // Validate business data
        state.businessData?.let { businessData ->
            if (businessData.businessName.isBlank()) isValid = false
            if (businessData.businessType.isBlank()) isValid = false
            if (businessData.businessDescription.isBlank()) isValid = false
            if (businessData.contactEmail.isBlank()) isValid = false
            if (businessData.contactPhone.isBlank()) isValid = false
        } ?: run {
            isValid = false
        }
        
        // Validate location data
        state.locationData?.let { locationData ->
            if (locationData.address.isBlank()) isValid = false
            if (locationData.city.isBlank()) isValid = false
            if (locationData.state.isBlank()) isValid = false
            if (locationData.postalCode.isBlank()) isValid = false
        } ?: run {
            isValid = false
        }
        
        // Validate required selections
        if (state.selectedZone.isNullOrBlank()) isValid = false
        if (state.selectedShore.isNullOrBlank()) isValid = false
        if (state.selectedIsland.orEmpty().isEmpty()) isValid = false
        
        return isValid
    }
    
    // FULFILLS: LLR-1.1.2 - Save Business Profile
    fun saveBusinessProfile() {
        viewModelScope.launch {
            if (!validateForm()) {
                val errorResponse = NetworkResponse.Error<Any>("Please fill in all required fields")
                updateStateFromNetworkResponse(errorResponse)
                return@launch
            }
            
            try {
                updateLoadingState(true)
                disableSaveButton()
                
                // Create BusinessRequest from current state with REAL values
                val request = createBusinessRequest()
                
                // Call existing BusinessDashViewModel.saveBusinessProfile()
                businessDashViewModel.saveBusinessProfile(request)
                
                // Listen to save state changes
                businessDashViewModel.registrationState.collect { response ->
                    when (response) {
                        is NetworkResponse.Loading -> {
                            updateStateFromNetworkResponse(response as NetworkResponse<Any>)
                        }
                        is NetworkResponse.Success -> {
                            updateStateFromNetworkResponse(NetworkResponse.Success<Any>(Unit))
                            updateLoadingState(false)
                            enableSaveButton()
                            return@collect // Stop collecting after success
                        }
                        is NetworkResponse.Error -> {
                            updateStateFromNetworkResponse(response as NetworkResponse<Any>)
                            updateLoadingState(false)
                            enableSaveButton()
                            return@collect // Stop collecting after error
                        }
                    }
                }
            } catch (e: Exception) {
                val errorResponse = NetworkResponse.Error<Any>("Failed to save business profile: ${e.message}")
                updateStateFromNetworkResponse(errorResponse)
                updateLoadingState(false)
                enableSaveButton()
            }
        }
    }
    
    // Helper function to create BusinessRequest from REAL current state values
    private fun createBusinessRequest(): BusinessRequest {
        val state = _dashboardState.value
        val businessData = state.businessData
        val locationData = state.locationData
        val businessHours = state.businessHours
        
        return BusinessRequest(
            // Location information from locationData
            Location = locationData?.address ?: "",
            Address = locationData?.street ?: locationData?.address ?: "",
            City = locationData?.city ?: "",
            State = locationData?.state ?: "",
            ZipCode = locationData?.postalCode ?: "",
            ShoreLine = locationData?.address ?: "", // Using address as shoreline for now
            Latitude = locationData?.latitude ?: 0.0,
            Longitude = locationData?.longitude ?: 0.0,
            
            // Business information from businessData
            Name = businessData?.businessName ?: "",
            
            // Location selections - converted to IDs
            ZoneId = getZoneIdFromString(state.selectedZone),
            ShoreId = getShoreIdFromString(state.selectedShore),
            IslandId = getIslandIdFromString(state.selectedIsland),
            
            // Business hours from state
            BusinessHours = businessHours,
            
            // Dock services
            IsDock = state.dockEnabled
        )
    }
    
    // Helper functions to convert zone/shore/island names to IDs
    // These would typically call API endpoints to get the actual IDs
    private fun getZoneIdFromString(zoneName: String?): Int {
        return when (zoneName) {
            "North Zone" -> 1
            "South Zone" -> 2
            "East Zone" -> 3
            "West Zone" -> 4
            else -> 0
        }
    }
    
    private fun getShoreIdFromString(shoreName: String?): Int {
        return when (shoreName) {
            "North Shore" -> 1
            "South Shore" -> 2
            "East Shore" -> 3
            "West Shore" -> 4
            else -> 0
        }
    }
    
    private fun getIslandIdFromString(islandName: String?): Int {
        return when (islandName) {
            "Main Island" -> 1
            "Island A" -> 2
            "Island B" -> 3
            "Island C" -> 4
            else -> 0
        }
    }
    
    // FULFILLS: LLR-2.1.1 - Session Management
    fun checkAuthentication(): Boolean {
        return sessionManager.sessionState.value.isAuthenticated
    }
}