package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.features.voyager.dashboard.repository.GoogleDirectionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrackingLocationViewModel(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val googleDirectionsApi: GoogleDirectionsApi,
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation = _userLocation.asStateFlow()

    private val _routePolyline = MutableStateFlow<List<LatLng>?>(null)
    val routePolyline = _routePolyline.asStateFlow()

    private val _estimatedTime = MutableStateFlow<String?>(null)
    val estimatedTime = _estimatedTime.asStateFlow()

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest =
            LocationRequest.create().apply {
                interval = 5000
                fastestInterval = 3000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

        val locationCallback =
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { location ->
                        _userLocation.value = location
                        fetchRouteAndETA(location)
                    }
                }
            }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun fetchRouteAndETA(location: Location) {
        viewModelScope.launch {
            val destination = LatLng(40.65209, -73.13763) // Example destination
            val response =
                googleDirectionsApi.getRoute(
                    origin = LatLng(40.75808, -73.01926),
                    destination = destination,
                )
            response?.let {
                _routePolyline.value = it.polylinePoints
                _estimatedTime.value = it.estimatedTime
            }
        }
    }
}
