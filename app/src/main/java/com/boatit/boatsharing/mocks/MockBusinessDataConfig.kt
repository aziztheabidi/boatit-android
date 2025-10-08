package com.boatit.boatsharing.mocks

import com.boatit.boatsharing.ui.business.model.*

/**
 * MockBusinessDataConfig - Comprehensive mock data for BusinessDashboard testing
 * 
 * This file provides realistic mock data to test all BusinessDashboard functionality
 * without requiring actual backend connectivity or authentication.
 * 
 * Data includes:
 * - Complete business profile information
 * - Location data with various zones/shores/islands
 * - Business hours for all days of the week
 * - Dock services with realistic marina data
 * - Gallery images (placeholder URLs)
 * - Dropdown options for zones, shores, and islands
 */
object MockBusinessDataConfig {
    
    /**
     * Mock BusinessProfileInfo - Complete business profile data
     * Matches typical marine business profile
     */
    val mockBusinessProfileInfo = BusinessProfileInfo(
        businessName = "Captain's Harbor Marina",
        businessType = "Marine Services & Boat Storage",
        businessDescription = "Premiere marine facility offering comprehensive boat services, storage, and dock facilities. We specialize in luxury yacht maintenance, fishing boat storage, and family water recreation services. Our experienced staff ensures the highest quality service for all your boating needs.",
        yearEstablished = 1985,
        contactEmail = "info@captainsmarina.com",
        contactPhone = "+1 (555) 123-MARINA",
        website = "https://www.captainsmarina.com",
        licenseNumber = "MAR-1985-001",
        taxId = "85-1234567",
        logoPath = "https://via.placeholder.com/150x150?text=Capitain's+Marina",
        isActive = true
    )
    
    /**
     * Mock LocationData - Comprehensive location information
     * Located in coastal area with proper marine coordinates
     */
    val mockLocationData = LocationData(
        address = "1234 Harbor View Drive",
        street = "1234 Harbor View Drive",
        city = "Sausalito",
        state = "California",
        country = "United States",
        postalCode = "94965",
        latitude = 37.8591,
        longitude = -122.4853,
        zone = "North Bay Zone",
        shore = "Marin County Shore",
        island = "Angel Island Area",
        isWaterfront = true,
        hasParking = true,
        hasAccessibility = true,
        isActive = true
    )
    
    /**
     * Mock BusinessHour list - Realistic marina business hours
     * Includes different hours for weekends and weekdays
     */
    val mockBusinessHours = listOf(
        BusinessHour("Monday", "06:00", "20:00"),
        BusinessHour("Tuesday", "06:00", "20:00"),
        BusinessHour("Wednesday", "06:00", "20:00"),
        BusinessHour("Thursday", "06:00", "20:00"),
        BusinessHour("Friday", "06:00", "21:00"),
        BusinessHour("Saturday", "05:30", "22:00"),
        BusinessHour("Sunday", "07:00", "20:00")
    )
    
    /**
     * Mock DockData - Comprehensive marina dock information
     * Realistic capacity and pricing data
     */
    val mockDockData = DockData(
        dockName = "Captain's Harbor Marina Main Dock",
        dockType = "Full-Service Marina",
        maxBoatLength = 120.0, // 120 feet
        maxBoatWidth = 25.0,   // 25 feet
        maxBoatDraft = 18.0,   // 18 feet deep water
        availableSlips = 45,
        totalSlips = 75,
        hourlyRate = 15.0,
        dailyRate = 85.0,
        monthlyRate = 1200.0,
        hasPower = true,
        hasWater = true,
        hasWifi = true,
        hasRestrooms = true,
        hasShowers = true,
        hasFuel = true,
        hasPumpout = true,
        isActive = true
    )
    
    /**
     * Mock Image URLs - Gallery images for business
     * Using placeholder services for realistic demonstration
     */
    val mockImageList = listOf(
        "https://via.placeholder.com/800x600/0066CC/FFFFFF?text=Marina+Dock+View",
        "https://via.placeholder.com/800x600/33AA44/FFFFFF?text=Boat+Storage",
        "https://via.placeholder.com/800x600/FF6600/FFFFFF?text=Fuel+Dock",
        "https://via.placeholder.com/800x600/9900CC/FFFFFF?text=Boat+Ramp",
        "https://via.placeholder.com/800x600/FF3366/FFFFFF?text=Luxury+Yachts",
        "https://via.placeholder.com/800x600/0099AA/FFFFFF?text=Restaurant+Deck"
    )
    
    /**
     * Mock Zone Dropdown Options
     * Representing different maritime zones in the area
     */
    val mockZones = listOf(
        DockDropdownItem(ParentId = 0, Id = 1, Name = "North Bay Zone"),
        DockDropdownItem(ParentId = 0, Id = 2, Name = "South Bay Zone"),
        DockDropdownItem(ParentId = 0, Id = 3, Name = "East Bay Zone"),
        DockDropdownItem(ParentId = 0, Id = 4, Name = "West Bay Zone"),
        DockDropdownItem(ParentId = 0, Id = 5, Name = "Peninsula Zone"),
        DockDropdownItem(ParentId = 0, Id = 6, Name = "Delta Zone")
    )
    
    /**
     * Mock Shore Dropdown Options
     * Representing different shoreline areas
     */
    val mockShores = listOf(
        DockDropdownItem(ParentId = 1, Id = 11, Name = "Marin County Shore"),
        DockDropdownItem(ParentId = 2, Id = 21, Name = "San Mateo County Shore"),
        DockDropdownItem(ParentId = 3, Id = 31, Name = "Alameda County Shore"),
        DockDropdownItem(ParentId = 4, Id = 41, Name = "San Francisco Shore"),
        DockDropdownItem(ParentId = 5, Id = 51, Name = "Contra Costa Shore"),
        DockDropdownItem(ParentId = 6, Id = 61, Name = "Sacramento Delta Shore")
    )
    
    /**
     * Mock Island Dropdown Options
     * Representing islands and key locations in the bay area
     */
    val mockIslands = listOf(
        DockDropdownItem(ParentId = 11, Id = 111, Name = "Angel Island Area"),
        DockDropdownItem(ParentId = 21, Id = 211, Name = "Treasure Island Area"),
        DockDropdownItem(ParentId = 31, Id = 311, Name = "Oakland Harbor Area"),
        DockDropdownItem(ParentId = 41, Id = 411, Name = "Fisherman's Wharf Area"),
        DockDropdownItem(ParentId = 51, Id = 511, Name = "Richmond Harbor Area"),
        DockDropdownItem(ParentId = 61, Id = 611, Name = "Martinez Area")
    )
    
    /**
     * Mock AppConstants values
     * Providing realistic constant values for testing
     */
    object MockAppConstants {
        const val USER_ID = "MOCK_USER_12345"
        const val IMG_PATH = "https://via.placeholder.com/"
        var Busines_Location = mockLocationData.address
        var Busines_DOCK = true
        var Busines_State = mockLocationData.state
        var Busines_City = mockLocationData.city
        var Busines_Zip = mockLocationData.postalCode
        var Busines_Lat = mockLocationData.latitude
        var Busines_Lont = mockLocationData.longitude
        
        // Mock hour list for time picker testing
        val hourList = listOf(
            "00:00", "00:30", "01:00", "01:30", "02:00", "02:30",
            "03:00", "03:30", "04:00", "04:30", "05:00", "05:30",
            "06:00", "06:30", "07:00", "07:30", "08:00", "08:30",
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
            "18:00", "18:30", "19:00", "19:30", "20:00", "20:30",
            "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"
        )
    }
    
    /**
     * Helper function to get mock business hours formatted as they appear in OldBusinessDashboard
     */
    fun getFormattedBusinessHours(): String {
        return buildString {
            mockBusinessHours.forEach { hour ->
                appendLine("${hour.Day}: ${hour.StartTime} - ${hour.EndTimeTime}")
            }
        }.trim()
    }
    
    /**
     * Helper function to create mock business description matching OldBusinessDashboard format
     */
    fun getShortBusinessDescription(): String {
        return "Captain's Harbor Marina, established in 1985, provides premium marine services for luxury yachts and fishing boats in the beautiful Sausalito harbor area."
    }
}
