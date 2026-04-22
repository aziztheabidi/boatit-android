package com.boatit.boatsharing.mocks

import com.boatit.boatsharing.features.business.model.BusinessDashboardState
import com.boatit.boatsharing.features.business.model.BusinessData
import com.boatit.boatsharing.features.business.model.BusinessHour
import com.boatit.boatsharing.features.business.model.DockData
import com.boatit.boatsharing.features.business.model.DockDropdownItem
import com.boatit.boatsharing.features.business.model.LocationData

object MockBusinessDataConfig {
    val businessProfileInfo =
        BusinessData(
            LogoPath = null,
            BusinessType = "Marina Services",
            YearOfEstablishment = 2012,
            Description = "A full-service marine business offering docking and charter support.",
            ImagesPath = listOf(),
            Location = "22 Harbor Street, Bay City",
            BusinessHours = listOf(),
            IsDock = true,
            Name = "Blue Harbor Marine",
            ShoreId = 1,
            ShoreName = "Main Shore",
            ZoneId = 1,
            ZoneName = "Zone A",
            IslandId = 1,
            IslandName = "Island One",
            State = "Florida",
            City = "Miami",
            ZipCode = "33101",
            Address = "Pier 4, Bay City",
            Latitude = 25.7617,
            Longitude = -80.1918,
            UserId = "mock-user-id",
            ChangedOn = "",
            ChangedBy = "",
        )

    val imageList =
        listOf(
            "https://picsum.photos/seed/boat1/400/300",
            "https://picsum.photos/seed/boat2/400/300",
        )

    val locationData =
        LocationData(
            location = "22 Harbor Street, Bay City",
            state = "Florida",
            city = "Miami",
            zipCode = "33101",
            latitude = 25.7617,
            longitude = -80.1918,
        )

    val businessHours =
        listOf(
            BusinessHour("Mon", "09:00:00", "17:00:00"),
            BusinessHour("Tue", "09:00:00", "17:00:00"),
            BusinessHour("Wed", "09:00:00", "17:00:00"),
            BusinessHour("Thu", "09:00:00", "17:00:00"),
            BusinessHour("Fri", "09:00:00", "17:00:00"),
        )

    val dockData =
        DockData(
            name = "Blue Harbor Dock",
            address = "Pier 4, Bay City",
            description = "Primary dock and boat prep area",
        )

    val dockEnabled = true
    val selectedZone = "Zone A"
    val selectedShore = "Main Shore"
    val selectedIsland = "Island One"

    val zones = listOf(DockDropdownItem(0, 1, "Zone A"), DockDropdownItem(0, 2, "Zone B"))
    val shores = listOf(DockDropdownItem(0, 1, "Main Shore"), DockDropdownItem(0, 2, "North Shore"))
    val islands = listOf(DockDropdownItem(0, 1, "Island One"), DockDropdownItem(0, 2, "Island Two"))

    val initialState =
        BusinessDashboardState(
            businessData = businessProfileInfo,
            imageList = imageList,
            locationData = locationData,
            businessHours = businessHours,
            dockData = dockData,
            dockEnabled = dockEnabled,
            selectedZone = selectedZone,
            selectedShore = selectedShore,
            selectedIsland = selectedIsland,
            selectedZoneId = 1,
            selectedShoreId = 1,
            selectedIslandId = 1,
            zones = zones,
            shores = shores,
            islands = islands,
            isButtonEnabled = true,
            isLoading = false,
            isError = false,
            errorMessage = null,
        )
}
