@file:Suppress("ktlint:standard:function-naming")

package com.boatit.boatsharing.utils.permissions

import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun AppPermissions(
    permission: String,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    val context = LocalContext.current
    val isPermissionGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    LaunchedEffect(permission) {
        if (!isPermissionGranted) {
            permissionLauncher.launch(permission)
        } else {
            onPermissionGranted()
        }
    }

    if (!isPermissionGranted) {
        onPermissionDenied()
    }
}
