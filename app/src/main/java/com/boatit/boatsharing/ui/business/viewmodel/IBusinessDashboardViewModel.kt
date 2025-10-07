package com.boatit.boatsharing.ui.business.viewmodel

import androidx.compose.runtime.State
import com.boatit.boatsharing.ui.business.model.*
import com.boatit.boatsharing.utils.session.SessionEvent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharedFlow
import android.content.Context
import android.net.Uri

/**
 * IBusinessDashboardViewModel - Common interface for BusinessDashboard ViewModels
 * 
 * This interface allows both the real and mock ViewModels to be used interchangeably
 * in the UI composables.
 */
interface IBusinessDashboardViewModel {
    val dashboardState: StateFlow<BusinessDashboardState>
    
    // State update functions
    fun updateLoadingState(isLoading: Boolean)
    fun updateErrorState(isError: Boolean, errorMessage: String?)
    fun updateBusinessData(businessData: BusinessProfileInfo?)
    fun updateSelectedZone(zone: String?)
    fun updateSelectedShore(shore: String?)
    fun updateSelectedIsland(island: String?)
    fun updateImageList(imageList: List<String>)
    fun updateBusinessDescription(description: String)
    fun updateLocationData(locationData: LocationData?)
    fun updateBusinessHours(businessHours: List<BusinessHour>)
    fun updateDockEnabled(enabled: Boolean)
    
    // Business logic functions
    fun checkAuthentication(): Boolean
    fun initializeDashboardData()
    fun validateForm(): Boolean
    fun saveBusinessProfile()
    fun saveBusinessHours(hours: List<BusinessHour>)
    fun uploadImagesToBackend(selectedUris: List<Uri>, context: Context)
    fun loadDropdownData()
    fun loadBusinessData()
    fun createDefaultBusinessHours(): List<BusinessHour>
    
    // Session management
    fun getSessionEvents(): SharedFlow<SessionEvent>
    
    // Button state management
    fun enableSaveButton()
    fun disableSaveButton()
}
