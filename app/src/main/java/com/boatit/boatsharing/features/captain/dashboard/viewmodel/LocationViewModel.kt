package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.boatit.boatsharing.features.captain.dashboard.model.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class CaptainLocationUiState(
    val userLocation: Location? = null,
) : UiState

sealed interface CaptainLocationUiEvent : UiEvent {
    data object None : CaptainLocationUiEvent

    data class LocationChanged(val location: Location) : CaptainLocationUiEvent
}

sealed interface CaptainLocationUiEffect : UiEffect {
    data object NoOpEffect : CaptainLocationUiEffect
}

class LocationViewModel(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val userSessionStore: UserSessionStore,
) : BaseViewModel<CaptainLocationUiState, CaptainLocationUiEvent, CaptainLocationUiEffect>(
        CaptainLocationUiState(),
    ) {
    init {
        startLocationUpdates()
        listenForLocationUpdates()
    }

    override fun onEvent(event: CaptainLocationUiEvent) {
        when (event) {
            CaptainLocationUiEvent.None -> Unit
            is CaptainLocationUiEvent.LocationChanged -> {
                updateState { copy(userLocation = event.location) }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest =
            LocationRequest.create().apply {
                interval = 5000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

        val locationCallback =
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        onEvent(CaptainLocationUiEvent.LocationChanged(location))
                        saveLocationToFirebase(location.latitude, location.longitude)
                    }
                }
            }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun saveLocationToFirebase(
        lat: Double,
        long: Double,
    ) {
        val userId = userSessionStore.currentUserId()
        if (userId.isBlank()) return

        val locationData =
            mapOf(
                "latitude" to lat,
                "longitude" to long,
                "timestamp" to System.currentTimeMillis(),
            )
        database.reference.child("user_locations").child(userId).setValue(locationData)
    }

    private fun listenForLocationUpdates() {
        val userId = userSessionStore.currentUserId()
        if (userId.isBlank()) return

        val userRef = database.reference.child("user_locations").child(userId)
        userRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(LocationData::class.java)?.let { locationData ->
                        val location =
                            Location("").apply {
                                latitude = locationData.latitude
                                longitude = locationData.longitude
                            }
                        onEvent(CaptainLocationUiEvent.LocationChanged(location))
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            },
        )
    }
}
