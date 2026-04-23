package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.voyager.dashboard.repository.GoogleDirectionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

data class TrackingLocationUiState(
    val userLocation: Location? = null,
    val routePolyline: List<LatLng>? = null,
    val estimatedTime: String? = null,
) : UiState

sealed interface TrackingLocationUiEvent : UiEvent {
    data object None : TrackingLocationUiEvent
}

sealed interface TrackingLocationUiEffect : UiEffect {
    data object NoOpEffect : TrackingLocationUiEffect
}

class TrackingLocationViewModel(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val googleDirectionsApi: GoogleDirectionsApi,
    @Suppress("unused") private val auth: FirebaseAuth,
    @Suppress("unused") private val database: FirebaseDatabase,
) : BaseViewModel<TrackingLocationUiState, TrackingLocationUiEvent, TrackingLocationUiEffect>(
        TrackingLocationUiState(),
    ) {
    override fun onEvent(event: TrackingLocationUiEvent) {
        when (event) {
            TrackingLocationUiEvent.None -> Unit
        }
    }

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
                        updateState { copy(userLocation = location) }
                        fetchRouteAndETA(location)
                    }
                }
            }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun fetchRouteAndETA(location: Location) {
        viewModelScope.launch {
            val destination = LatLng(40.65209, -73.13763)
            val response =
                googleDirectionsApi.getRoute(
                    origin = LatLng(40.75808, -73.01926),
                    destination = destination,
                )
            response?.let {
                updateState {
                    copy(
                        routePolyline = it.polylinePoints,
                        estimatedTime = it.estimatedTime,
                    )
                }
            }
        }
    }
}
