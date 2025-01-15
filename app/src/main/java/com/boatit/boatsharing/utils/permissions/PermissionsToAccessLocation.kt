package com.boatit.boatsharing.utils.permissions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.boatit.boatsharing.R
import com.boatit.boatsharing.utils.AlertView


@Composable
fun PermissionsToAccessLocation(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    var permissionGranted by remember { mutableStateOf(false) }
    var showPermissionDeniedMessage by remember { mutableStateOf(false) }

    val permission = Manifest.permission.ACCESS_FINE_LOCATION

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
            showPermissionDeniedMessage = true
        }
    }

    AppPermissions(
        permission = permission,
        permissionLauncher = permissionLauncher,
        onPermissionGranted = {
            onPermissionGranted()

            showPermissionDeniedMessage = false
        },
        onPermissionDenied = {
            onPermissionDenied()
            showPermissionDeniedMessage = true
        }
    )

    if (showPermissionDeniedMessage) {
        AlertView(
            title = stringResource(R.string.location_permission_text),
            message =  stringResource(R.string.location_permission_alert_text),
            onConfirm = {
                showPermissionDeniedMessage = false
                permissionLauncher.launch(permission)
            },
            onCancel = {

                showPermissionDeniedMessage = false
                onPermissionDenied()
            }
        )
    }
}
