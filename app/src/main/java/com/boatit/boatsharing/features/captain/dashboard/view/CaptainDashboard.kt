package com.boatit.boatsharing.features.captain.dashboard.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.AcceptRequestUiEffect
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.AcceptRequestViewModel
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.LocationViewModel
import com.boatit.boatsharing.features.login.viewmodel.NotificationViewModel
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageNotification
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.VoyageSessionStore
import com.boatit.boatsharing.utils.permissions.PermissionsToAccessLocation
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptainDashboard(
    navController: NavController,
    viewModel: LocationViewModel = koinViewModel(),
    viewModelN: NotificationViewModel = koinViewModel(),
    viewModelR: AcceptRequestViewModel = koinViewModel(),
    voyageSessionStore: VoyageSessionStore = get(VoyageSessionStore::class.java),
    userSessionStore: UserSessionStore = get(UserSessionStore::class.java),
) {
    val context = LocalContext.current
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val defaultLatLng = LatLng(40.792240, -73.138260)
    var currentLatLng by remember { mutableStateOf(defaultLatLng) }

    var isLoading by remember { mutableStateOf(false) }

    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = false,
        )
    val coroutineScope = rememberCoroutineScope()

    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(defaultLatLng, 17f)
        }

    val markerState = remember { MarkerState(position = defaultLatLng) }
    var notification by remember { mutableStateOf<VoyageNotification?>(null) }
    var showVoyagerRequest by rememberSaveable { mutableStateOf(false) }

    val notificationState by viewModelN.notificationState.collectAsStateWithLifecycle()
    val userLocation by viewModel.userLocation.collectAsState()

    LaunchedEffect(viewModelR) {
        viewModelR.uiEffect.collectLatest { effect ->
            when (effect) {
                is AcceptRequestUiEffect.Accepted -> {
                    isLoading = false
                    showVoyagerRequest = false
                    voyageSessionStore.setVoyageId(effect.voyageId)
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    viewModelR.resetNearbyPlaces()
                }

                is AcceptRequestUiEffect.Declined -> {
                    isLoading = false
                    showVoyagerRequest = false
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    viewModelR.resetNearbyPlaces()
                }

                is AcceptRequestUiEffect.ShowToast -> {
                    isLoading = false
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(notificationState) {
        if (notificationState?.Name != null) {
            notification = notificationState
            showVoyagerRequest = true
        }
    }

    PermissionsToAccessLocation(
        fusedLocationProviderClient = fusedLocationProviderClient,
        onPermissionGranted = { userLatLng ->
            currentLatLng = userLatLng
            markerState.position = userLatLng
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(userLatLng, 17f))
        },
        onPermissionDenied = {},
        content = {
            // Composable to show once permission is granted
            Text("Permission Granted")
        },
    )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 0.dp),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties =
                        MapProperties(
                            mapType = MapType.NORMAL,
                        ),
                ) {
                    Marker(
                        state = markerState,
                        title = "Its me",
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.current_marker),
                    )
                }

                userLocation?.let { location ->
                    Text(text = "Latitude: ${location.latitude}")
                    Text(text = "Longitude: ${location.longitude}")
                    currentLatLng = LatLng(location.latitude, location.longitude)
                }
            }
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .width(80.dp)
                    .height(100.dp)
                    .padding(start = 20.dp, top = 40.dp),
            contentAlignment = Alignment.TopStart,
        ) {
            Image(
                painter = painterResource(id = R.drawable.wheel_icon),
                contentDescription = "Icon Image",
                modifier =
                    Modifier
                        .size(width = 80.dp, height = 80.dp)
                        .clickable {
                            navController.navigate(NavigationManager.CAPTAIN_MENU_OPTIONS_SCREEN)
                        },
            )
        }

        CaptainStatus(navController, stringResource(R.string.online_text))

        if (showVoyagerRequest) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch {
                        sheetState.partialExpand()
                    }
                    showVoyagerRequest = false
                },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = Color.Transparent,
                tonalElevation = 16.dp,
                dragHandle = {
                    Box(
                        modifier =
                            Modifier
                                .padding(8.dp)
                                .width(50.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(50)),
                    )
                },
                modifier =
                    Modifier
                        .pointerInput(Unit) {
                            detectVerticalDragGestures { _, dragAmount ->
                                if (dragAmount > 20) {
                                    coroutineScope.launch {
                                        sheetState.partialExpand() // Lock to partially expanded
                                    }
                                }
                            }
                        },
            ) {
                AcceptVoyagerRequest(
                    navController,
                    notification,
                    onDeclineClick = {
                        showVoyagerRequest = false
                        isLoading = true
                        val userId = userSessionStore.currentUserId()
                        viewModelR.decline(
                            AcceptVoyageRequest(notification?.Id.orEmpty(), userId, defaultLatLng.latitude, defaultLatLng.longitude),
                        )
                    },
                    onAcceptClick = {
                        isLoading = true
                        val userId = userSessionStore.currentUserId()
                        viewModelR.accept(
                            AcceptVoyageRequest(notification?.Id.orEmpty(), userId, defaultLatLng.latitude, defaultLatLng.longitude),
                        )
                    },
                )
            }
        }
    }
}
