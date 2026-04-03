package com.boatit.boatsharing.ui.voyager.dashboard.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.ui.captain.dashboard.model.LocationData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ShipTrackingViewModel(private val database: FirebaseDatabase, private val userId: String) : ViewModel() {

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation

    private val userRef: DatabaseReference = database.reference.child("user_locations").child(userId)
    private var locationListener: ValueEventListener? = null
    private var isListening = false // Flag to track listener state

    fun startListening() {
        if (isListening) return // Prevent multiple listeners
        isListening = true

        locationListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(LocationData::class.java)?.let { locationData ->
                    val location = Location("").apply {
                        latitude = locationData.latitude
                        longitude = locationData.longitude
                    }
                    viewModelScope.launch {
                        _userLocation.emit(location)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                isListening = false // Reset flag on error
            }
        }

        userRef.addValueEventListener(locationListener!!)
    }

    fun stopListening() {
        locationListener?.let { userRef.removeEventListener(it) }
        isListening = false
    }
}
