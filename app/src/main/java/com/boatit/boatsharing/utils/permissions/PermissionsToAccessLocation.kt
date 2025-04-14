package com.boatit.boatsharing.utils.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.boatit.boatsharing.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng

@Composable
fun PermissionsToAccessLocation(
    fusedLocationProviderClient: FusedLocationProviderClient,
    onPermissionGranted: (LatLng) -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    var locationPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission request launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted
        if (isGranted) {
            fetchAndUpdateLocation(fusedLocationProviderClient, onPermissionGranted)
        } else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(locationPermissionGranted) {
        if (locationPermissionGranted) {
            fetchAndUpdateLocation(fusedLocationProviderClient, onPermissionGranted)
        }
    }

    if (!locationPermissionGranted) {
        Button(
            onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Grant Location Permission")
        }
    }

}

fun fetchAndUpdateLocation(
    fusedLocationProviderClient: FusedLocationProviderClient,
    onLocationUpdate: (LatLng) -> Unit
) {
    try {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val userLatLng = LatLng(it.latitude, it.longitude)
                onLocationUpdate(userLatLng)
            }
        }
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}

@Composable
fun EnableLocationDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Location Services Disabled") },
        text = { Text("Please enable location services for the app to function properly.") },
        confirmButton = {
            TextButton(
                onClick = {
                    // Use context here to start activity
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                }
            ) {
                Text("Enable")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("Cancel")
            }
        }
    )
}








