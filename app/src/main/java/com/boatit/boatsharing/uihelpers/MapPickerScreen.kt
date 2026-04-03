package com.boatit.boatsharing.uihelpers

import android.location.Geocoder
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boatit.boatsharing.utils.AppConstants
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale


@Composable

fun MapPickerScreen(navController: NavController) {
    val context = LocalContext.current
    val defaultLatLng = LatLng(40.792240, -73.138260) // Default location
    var selectedLatLng by remember { mutableStateOf(defaultLatLng) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 14f)
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                selectedLatLng = latLng
            }
        ) {
            Marker(
                state = MarkerState(position = selectedLatLng),
                title = "Selected Location"
            )

        }

        Button(
            onClick = {
                val geocoder = Geocoder(context, Locale.getDefault())
                val address = try {
                    geocoder.getFromLocation(
                        selectedLatLng.latitude,
                        selectedLatLng.longitude,
                        1
                    )?.firstOrNull()
                } catch (e: Exception) {
                    null
                }

                AppConstants.Busines_Location = address?.getAddressLine(0) ?: "Address N/A"
                AppConstants.Busines_City = address?.locality ?: "City N/A"
                AppConstants.Busines_State = address?.adminArea ?: "State N/A"
                AppConstants.Busines_Zip = address?.getAddressLine(0)?.split(",")?.get(3)
                AppConstants.Busines_Lat = selectedLatLng.latitude
                AppConstants.Busines_Lont = selectedLatLng.longitude

                val fullAddressText = ("""
                        Address: """ + AppConstants.Busines_Location + """
                        City: """ + AppConstants.Busines_City + """
                        State: """ + AppConstants.Busines_State + """
                        Latitude: """ + AppConstants.Busines_Lat + """
                        Longitude: """ + AppConstants.Busines_Lont + """
                    """).trimIndent()

                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("selected_address", fullAddressText)

                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Use This Location")
        }

    }
}

