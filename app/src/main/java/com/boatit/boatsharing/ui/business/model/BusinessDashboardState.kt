package com.boatit.boatsharing.ui.business.model

/**
 * BusinessDashboardState - Main state container for BusinessDashboard
 * 
 * FULFILLS: LLR-0.1.1 - BusinessDashboardState Field Layout
 * FULFILLS: LLR-2.6.1 - Docks Data Loading Integration (dropdown options)
 * FULFILLS: LLR-2.6.2 - Voyages Data Loading Integration (business data)
 * 
 * This data class represents the complete state of the BusinessDashboard screen,
 * including loading states, error states, business data, and user selections.
 * 
 * @property isLoading Boolean indicating if any async operation is in progress
 * @property isError Boolean indicating if an error has occurred
 * @property errorMessage String containing the error message if isError is true
 * @property businessData BusinessProfileInfo containing the business profile information
 * @property selectedZone String containing the currently selected zone
 * @property selectedShore String containing the currently selected shore
 * @property selectedIsland String containing the currently selected island
 * @property isButtonEnabled Boolean indicating if the save button should be enabled
 * @property imageList List<String> containing URLs of business images
 * @property dockEnabled Boolean indicating if dock services are enabled
 * @property locationData LocationData containing business location information
 * @property businessHours List<BusinessHour> containing business operating hours
 * @property zones List<com.boatit.boatsharing.ui.business.model.DockDropdownItem> containing zone options
 * @property shores List<com.boatit.boatsharing.ui.business.model.DockDropdownItem> containing shore options
 * @property islands List<com.boatit.boatsharing.ui.business.model.DockDropdownItem> containing island options
 */
data class BusinessDashboardState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val businessData: BusinessProfileInfo? = null,
    val selectedZone: String? = null,
    val selectedShore: String? = null,
    val selectedIsland: String? = null,
    val isButtonEnabled: Boolean = false,
    val imageList: List<String> = emptyList(),
    val dockEnabled: Boolean = false,
    val dockData: DockData? = null,
    val locationData: LocationData? = null,
    val businessHours: List<BusinessHour> = emptyList(),
    val zones: List<com.boatit.boatsharing.ui.business.model.DockDropdownItem> = emptyList(),
    val shores: List<com.boatit.boatsharing.ui.business.model.DockDropdownItem> = emptyList(),
    val islands: List<com.boatit.boatsharing.ui.business.model.DockDropdownItem> = emptyList()
)
