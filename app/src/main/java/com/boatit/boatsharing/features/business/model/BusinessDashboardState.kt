package com.boatit.boatsharing.features.business.model

import com.boatit.boatsharing.core.presentation.UiState

data class LocationData(
    val location: String = "",
    val state: String = "",
    val city: String = "",
    val zipCode: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)

data class DockData(
    val name: String = "",
    val address: String = "",
    val description: String = "",
)

data class BusinessDashboardState(
    val businessData: BusinessData? = null,
    val imageList: List<String> = emptyList(),
    val locationData: LocationData = LocationData(),
    val businessHours: List<BusinessHour> = emptyList(),
    val dockData: DockData = DockData(),
    val dockEnabled: Boolean = false,
    val selectedZone: String = "",
    val selectedShore: String = "",
    val selectedIsland: String = "",
    val selectedZoneId: Int = 0,
    val selectedShoreId: Int = 0,
    val selectedIslandId: Int = 0,
    val zones: List<DockDropdownItem> = emptyList(),
    val shores: List<DockDropdownItem> = emptyList(),
    val islands: List<DockDropdownItem> = emptyList(),
    val isButtonEnabled: Boolean = false,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = null,
) : UiState
