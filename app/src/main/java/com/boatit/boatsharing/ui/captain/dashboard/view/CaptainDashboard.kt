package com.boatit.boatsharing.ui.captain.dashboard.view


import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.LocationViewModel
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.AcceptRequestViewModel
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.StartVoyageViewModel
import com.boatit.boatsharing.ui.login.viewmodel.NotificationViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyageNotification
import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.permissions.PermissionsToAccessLocation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CaptainDashboard(navController: NavController ,
     viewModel: LocationViewModel = koinViewModel(),
     viewModelN: NotificationViewModel = koinViewModel(),
     viewModelR: AcceptRequestViewModel = koinViewModel(),
     viewModelStart: StartVoyageViewModel = koinViewModel()) {

    val context = LocalContext.current
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val defaultLatLng = LatLng(40.792240, -73.138260)
    var currentLatLng by remember { mutableStateOf(defaultLatLng) }
    val seaRoute = listOf(
        LatLng(40.65209, -73.13763), // Start
        LatLng(40.70000, -73.10000), // Waypoint 1 (ocean path)
        LatLng(40.73000, -73.05000), // Waypoint 2 (ocean path)
        LatLng(40.75808, -73.01926)  // End
    )

    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val coroutineScope = rememberCoroutineScope()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 17f)
    }

    val markerState = remember { MarkerState(position = defaultLatLng) }
    var notification by remember { mutableStateOf<VoyageNotification?>(null) }
    var showVoyagerRequest by remember { mutableStateOf(false) }
    var showVoyagerDetal by remember { mutableStateOf(false) }
    var showFindBoat by rememberSaveable { mutableStateOf(false) }

    val notificationState by viewModelN.notificationState.collectAsStateWithLifecycle()
    val userLocation by viewModel.userLocation.collectAsState()
    val requestState by viewModelR.loginState.collectAsState()
    val startState by viewModelStart.loginState.collectAsState()

    when (requestState) {
        is NetworkResponse.Success -> {
            if (isLoading) {
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, "Voyage Accepted. Waiting For Payment", Toast.LENGTH_SHORT).show()
                showVoyagerRequest = false
                showVoyagerDetal = true
                AppConstants.Voyage_ID = notification?.Id
                viewModelR.resetNearbyPlaces()
            }
        }
        is NetworkResponse.Error -> {
            if (isLoading) {
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, requestState.message, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    when (startState) {
        is NetworkResponse.Success -> {
            if (isLoading) {
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, "Voyage Started.", Toast.LENGTH_SHORT).show()
                showVoyagerDetal = false
                navController.navigate(NavigationManager.VOYAGE_STARTED_SCREEN)
            }
        }
        is NetworkResponse.Error -> {
            if (isLoading) {
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, "Unable To Start", Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    LaunchedEffect(notificationState) {
        if (notificationState != null) {
            if(notificationState!!.Name != null){
                val boundsBuilder = LatLngBounds.Builder()
                seaRoute.forEach { boundsBuilder.include(it) }
                val bounds = boundsBuilder.build()
                cameraPositionState.move(
                    update = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                )
                showFindBoat = true
                notification = notificationState
                showVoyagerRequest = true
            }
        }
    }

    PermissionsToAccessLocation(
        fusedLocationProviderClient = fusedLocationProviderClient,
        onPermissionGranted = { userLatLng ->
            currentLatLng = userLatLng
            markerState.position = userLatLng
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(userLatLng, 17f))
        },
        onPermissionDenied = {}
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        mapType = MapType.NORMAL,
                    )
                ) {
                    Marker(
                        state = markerState,
                        title = "Its me",
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.current_marker)
                    )

                    if(showFindBoat){

                        Marker(
                            state = MarkerState(position = seaRoute.first()),
                            title = "Its me",
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.location_icon_two)
                        )

                        Marker(
                            state = MarkerState(position = seaRoute.last()),
                            title = "Its me",
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.location_icon_two)
                        )

                        Polyline(
                            points = seaRoute,
                            color = Color.Blue,
                            width = 8f,
                            geodesic = true // Smooths the polyline for curved sea paths
                        )
                    }
                }

                userLocation?.let { location ->
                    Text(text = "Latitude: ${location.latitude}")
                    Text(text = "Longitude: ${location.longitude}")
                    currentLatLng = LatLng(location.latitude, location.longitude)
                }
            }

        }

        CaptainStatus( navController ,stringResource( R.string.online_text))

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
                        modifier = Modifier
                            .padding(8.dp)
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(50))
                    )
                },
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            if (dragAmount > 20) {
                                coroutineScope.launch {
                                    sheetState.partialExpand() // Lock to partially expanded
                                }
                            }
                        }
                    }
            ) {
                AcceptVoyagerRequest(
                    navController, notification,
                    onDeclineClick = {
                        showVoyagerRequest = false
                        showFindBoat = false
                    },
                    onAcceptClick = {
                        isLoading = true
                        isNetworkError = true
                        viewModelR.accept(
                            AcceptVoyageRequest(notification?.Id!!, AppConstants.USER_ID!!, defaultLatLng.latitude, defaultLatLng.longitude)
                        )
                    }
                )
            }
        }

        if (showVoyagerDetal) {
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
                        modifier = Modifier
                            .padding(8.dp)
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(50))
                    )
                },
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            if (dragAmount > 20) {
                                coroutineScope.launch {
                                    sheetState.partialExpand() // Lock to partially expanded
                                }
                            }
                        }
                    }
            ) {
                CaptainVoyageDetails(
                    navController, notification?.Name!!,
                    onDeclineClick = {
                        showVoyagerRequest = false
                    },
                    onAcceptClick = { otp ->
                        isLoading = true
                        isNetworkError = true
                        viewModelStart.startvoyage(
                            VoyageStartRequest(notification?.Id!!, otp)
                        )
                    }
                )
            }
        }
    }
}