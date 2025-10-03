package com.boatit.boatsharing.ui.business.model

/**
 * DockData - Dock service information container
 * 
 * FULFILLS: LLR-0.4.1 - DockData Field Layout
 * 
 * This data class contains information about dock services offered by the business,
 * including dock details, services, and pricing information.
 * 
 * @property dockName String containing the name of the dock
 * @property dockType String containing the type of dock (e.g., "Marina", "Pier")
 * @property maxBoatLength Double containing the maximum boat length the dock can accommodate
 * @property maxBoatWidth Double containing the maximum boat width the dock can accommodate
 * @property maxBoatDraft Double containing the maximum boat draft the dock can accommodate
 * @property availableSlips Int containing the number of available slips
 * @property totalSlips Int containing the total number of slips
 * @property hourlyRate Double containing the hourly rate for dock usage
 * @property dailyRate Double containing the daily rate for dock usage
 * @property monthlyRate Double containing the monthly rate for dock usage
 * @property hasPower Boolean indicating if the dock provides electrical power
 * @property hasWater Boolean indicating if the dock provides water
 * @property hasWifi Boolean indicating if the dock provides WiFi
 * @property hasRestrooms Boolean indicating if the dock has restroom facilities
 * @property hasShowers Boolean indicating if the dock has shower facilities
 * @property hasFuel Boolean indicating if the dock provides fuel services
 * @property hasPumpout Boolean indicating if the dock provides pumpout services
 * @property isActive Boolean indicating if the dock is currently active
 */
data class DockData(
    val dockName: String = "",
    val dockType: String = "",
    val maxBoatLength: Double = 0.0,
    val maxBoatWidth: Double = 0.0,
    val maxBoatDraft: Double = 0.0,
    val availableSlips: Int = 0,
    val totalSlips: Int = 0,
    val hourlyRate: Double = 0.0,
    val dailyRate: Double = 0.0,
    val monthlyRate: Double = 0.0,
    val hasPower: Boolean = false,
    val hasWater: Boolean = false,
    val hasWifi: Boolean = false,
    val hasRestrooms: Boolean = false,
    val hasShowers: Boolean = false,
    val hasFuel: Boolean = false,
    val hasPumpout: Boolean = false,
    val isActive: Boolean = true
)
