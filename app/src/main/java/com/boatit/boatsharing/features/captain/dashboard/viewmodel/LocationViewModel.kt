package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.ViewModel
import com.boatit.boatsharing.features.captain.dashboard.model.LocationData
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationViewModel(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val userSessionStore: UserSessionStore,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation = _userLocation.asStateFlow()

    init {
        startLocationUpdates()
        listenForLocationUpdates()
    }

    @SuppressLint("MissingPermission") // Ensure to request permissions properly in UI
    private fun startLocationUpdates() {
        val locationRequest =
            LocationRequest.create().apply {
                interval = 5000 // 5 seconds
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

        val locationCallback =
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        _userLocation.value = location
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
                        _userLocation.value = location
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            },
        )
    }
}
