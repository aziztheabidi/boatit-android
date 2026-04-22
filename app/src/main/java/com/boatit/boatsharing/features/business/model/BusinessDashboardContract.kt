package com.boatit.boatsharing.features.business.model

import java.io.File

typealias BusinessDashboardUiState = BusinessDashboardState

sealed interface BusinessDashboardUiEvent {
    data object Initialize : BusinessDashboardUiEvent

    data class UpdateImageList(val imageList: List<String>) : BusinessDashboardUiEvent

    data class UploadGalleryImages(val files: List<File>) : BusinessDashboardUiEvent

    data class UploadLogo(val imageUrl: String, val file: File) : BusinessDashboardUiEvent

    data class RemoveImage(val imageUrl: String) : BusinessDashboardUiEvent

    data class UpdateLocationData(val locationData: LocationData) : BusinessDashboardUiEvent

    data class UpdateSelectedZone(val zoneId: Int, val zoneName: String) : BusinessDashboardUiEvent

    data class UpdateSelectedShore(val shoreId: Int, val shoreName: String) : BusinessDashboardUiEvent

    data class UpdateSelectedIsland(val islandId: Int, val islandName: String) : BusinessDashboardUiEvent

    data class UpdateDockEnabled(val enabled: Boolean) : BusinessDashboardUiEvent

    data class UpdateDockData(val dockData: DockData) : BusinessDashboardUiEvent

    data class SaveBusinessHours(val hours: List<BusinessHour>) : BusinessDashboardUiEvent

    data object SaveBusinessProfile : BusinessDashboardUiEvent
}

sealed interface BusinessDashboardUiEffect {
    data class ShowToast(val message: String) : BusinessDashboardUiEffect

    data object SessionExpired : BusinessDashboardUiEffect
}
