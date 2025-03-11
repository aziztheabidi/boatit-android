import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.boatit.boatsharing.ui.captain.dashbaord.model.LocationData
import com.boatit.boatsharing.utils.AppConstants
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationViewModel(private val auth: FirebaseAuth, private val database: FirebaseDatabase, context: Context) : ViewModel() {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation = _userLocation.asStateFlow()

    init {
        startLocationUpdates()
        listenForLocationUpdates()
    }

    @SuppressLint("MissingPermission") // Ensure to request permissions properly in UI
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000 // 5 seconds
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    _userLocation.value = location
                    saveLocationToFirebase(location.latitude, location.longitude)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun saveLocationToFirebase(lat: Double, long: Double) {
        val locationData = mapOf(
            "latitude" to lat,
            "longitude" to long,
            "timestamp" to System.currentTimeMillis()
        )
        database.reference.child("user_locations").child(AppConstants.USER_ID.toString()).setValue(locationData)
    }

    private fun listenForLocationUpdates() {
        val userRef = database.reference.child("user_locations").child(AppConstants.USER_ID.toString())
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(LocationData::class.java)?.let { locationData ->
                    val location = Location("").apply {
                        latitude = locationData.latitude
                        longitude = locationData.longitude
                    }
                    _userLocation.value = location
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
