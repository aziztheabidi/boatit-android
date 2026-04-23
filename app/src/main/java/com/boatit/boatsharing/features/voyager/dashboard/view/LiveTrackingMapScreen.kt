@file:Suppress(
    "ktlint:standard:function-naming",
)

package com.boatit.boatsharing.features.voyager.dashboard.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.TrackingLocationViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiveTrackingMapScreen(viewModel: TrackingLocationViewModel = koinViewModel()) {
    val context = LocalContext.current

    val mapUiSettings = remember { MapUiSettings(zoomControlsEnabled = true) }
    val mapProperties = remember { MapProperties(isMyLocationEnabled = true) }

    val trackState by viewModel.uiState.collectAsState()
    val userLocation = trackState.userLocation
    val routePolyline = trackState.routePolyline
    val estimatedTime = trackState.estimatedTime

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = mapProperties,
        uiSettings = mapUiSettings,
        onMapLoaded = {
            viewModel.startLocationUpdates()
        },
    ) {
        // Show user location
        userLocation?.let { location ->
            Marker(
                state = MarkerState(position = LatLng(40.75808, -73.01926)),
                title = "Your Location",
            )
        }

        // Draw route polyline
        routePolyline?.let { polyline ->
            Polyline(
                points = polyline,
                color = Color.Blue,
                width = 10f,
            )
        }

        // Show ETA
        userLocation?.let { location ->
            estimatedTime?.let { time ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = "ETA: $time mins",
                )
            }
        }
    }
}
