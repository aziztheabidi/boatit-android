package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.location.Location
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.captain.dashboard.model.LocationData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

data class ShipTrackingUiState(
    val userLocation: Location? = null,
) : UiState

sealed interface ShipTrackingUiEvent : UiEvent {
    data object StartListening : ShipTrackingUiEvent

    data object StopListening : ShipTrackingUiEvent

    data class LocationUpdated(val location: Location) : ShipTrackingUiEvent
}

sealed interface ShipTrackingUiEffect : UiEffect {
    data object NoOpEffect : ShipTrackingUiEffect
}

class ShipTrackingViewModel(
    private val database: FirebaseDatabase,
    private val userId: String,
) : BaseViewModel<ShipTrackingUiState, ShipTrackingUiEvent, ShipTrackingUiEffect>(ShipTrackingUiState()) {
    private val userRef: DatabaseReference = database.reference.child("user_locations").child(userId)
    private var locationListener: ValueEventListener? = null
    private var isListening = false

    override fun onEvent(event: ShipTrackingUiEvent) {
        when (event) {
            ShipTrackingUiEvent.StartListening -> startListening()
            ShipTrackingUiEvent.StopListening -> stopListening()
            is ShipTrackingUiEvent.LocationUpdated -> {
                updateState { copy(userLocation = event.location) }
            }
        }
    }

    fun startListening() {
        if (isListening) return
        isListening = true

        locationListener =
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(LocationData::class.java)?.let { locationData ->
                        val location =
                            Location("").apply {
                                latitude = locationData.latitude
                                longitude = locationData.longitude
                            }
                        viewModelScope.launch {
                            onEvent(ShipTrackingUiEvent.LocationUpdated(location))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    isListening = false
                }
            }

        locationListener?.let { userRef.addValueEventListener(it) }
    }

    fun stopListening() {
        locationListener?.let { userRef.removeEventListener(it) }
        isListening = false
    }
}
