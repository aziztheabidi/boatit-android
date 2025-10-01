package com.boatit.boatsharing.ui.voyager.dashboard.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.platform.LocalContext
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.TrackingLocationViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiveTrackingMapScreen(
    viewModel: TrackingLocationViewModel = koinViewModel()
) {

    val context = LocalContext.current

    val mapUiSettings = remember { MapUiSettings(zoomControlsEnabled = true) }
    val mapProperties = remember { MapProperties(isMyLocationEnabled = true) }

    val userLocation by viewModel.userLocation.collectAsState()
    val routePolyline by viewModel.routePolyline.collectAsState()
    val estimatedTime by viewModel.estimatedTime.collectAsState()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = mapProperties,
        uiSettings = mapUiSettings,
        onMapLoaded = {
            viewModel.startLocationUpdates()
        }
    ) {
        // Show user location
        userLocation?.let { location ->
            Marker(
                state = MarkerState(position = LatLng(40.75808, -73.01926)),
                title = "Your Location"
            )
        }

        // Draw route polyline
        routePolyline?.let { polyline ->
            Polyline(
                points = polyline,
                color = Color.Blue,
                width = 10f
            )
        }

        // Show ETA
        userLocation?.let { location ->
            estimatedTime?.let { time ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = "ETA: $time mins"
                )
            }
        }
    }
}
