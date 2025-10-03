package com.boatit.boatsharing.ui.business.model

/**
 * LocationData - Business location information container
 * 
 * FULFILLS: LLR-0.5.1 - LocationData Field Layout
 * 
 * This data class contains all the business location information including
 * address details, geographical coordinates, and location characteristics.
 * 
 * @property address String containing the full business address
 * @property street String containing the street address
 * @property city String containing the city name
 * @property state String containing the state/province name
 * @property country String containing the country name
 * @property postalCode String containing the postal/zip code
 * @property latitude Double containing the latitude coordinate
 * @property longitude Double containing the longitude coordinate
 * @property zone String containing the geographical zone
 * @property shore String containing the shore information
 * @property island String containing the island information
 * @property isWaterfront Boolean indicating if the location is waterfront
 * @property hasParking Boolean indicating if the location has parking facilities
 * @property hasAccessibility Boolean indicating if the location is accessible
 * @property isActive Boolean indicating if the location is currently active
 */
data class LocationData(
    val address: String = "",
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val postalCode: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val zone: String = "",
    val shore: String = "",
    val island: String = "",
    val isWaterfront: Boolean = false,
    val hasParking: Boolean = false,
    val hasAccessibility: Boolean = false,
    val isActive: Boolean = true
)
