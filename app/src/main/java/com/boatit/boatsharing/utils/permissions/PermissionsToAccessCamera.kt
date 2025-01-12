package com.boatit.boatsharing.utils.permissions

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.io.File

@Composable

fun PermissionsToAccessCamera(
    onImageCaptured: (Uri?) -> Unit
) {
    val context = LocalContext.current
    val permission = Manifest.permission.CAMERA
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            capturedImageUri?.let {
                onImageCaptured(it)
            }
        } else {
            Toast.makeText(context, "Image capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val photoUri = Uri.fromFile(File(context.cacheDir, "photo.jpg"))
            capturedImageUri = photoUri // Save the URI of the captured photo
            cameraLauncher.launch(photoUri)
        } else {
            Toast.makeText(context, "Permission Denied. Cannot access camera.", Toast.LENGTH_SHORT).show()
        }
    }

    AppPermissions (
        permission = permission,
        permissionLauncher = permissionLauncher,
        onPermissionGranted = {
            val photoUri = Uri.fromFile(File(context.cacheDir, "photo.jpg"))
            capturedImageUri = photoUri
            cameraLauncher.launch(photoUri)
        },
        onPermissionDenied = {
            Toast.makeText(context, "Permission Denied. Cannot access camera.", Toast.LENGTH_SHORT).show()
        }
    )
}
