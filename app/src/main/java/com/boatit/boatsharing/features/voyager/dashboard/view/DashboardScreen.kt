@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:property-naming",
    "ktlint:standard:curly-spacing",
    "ktlint:standard:no-line-break-after-else",
    "ktlint:standard:if-else-wrapping",
)

package com.boatit.boatsharing.features.voyager.dashboard.view

import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.RestrictionsManager.RESULT_ERROR
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.application.StripeSheetActivity
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.navigation.InteractionRoutes
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.navigation.navigateWithClearStack
import com.boatit.boatsharing.features.login.viewmodel.NotificationViewModel
import com.boatit.boatsharing.features.voyager.dashboard.model.ActiveVoyageDetails
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyages
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentConfirmationRequest
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FindBoatPrefillStore
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.GetActiveVoyageUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.GetActiveVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.NearByVoyagesViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.PaymentSheetConfigUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.PaymentSheetConfigViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.PaymentViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.VoyageSessionStore
import com.boatit.boatsharing.ui.components.CustomDialog
import com.boatit.boatsharing.ui.components.SessionDialog
import com.boatit.boatsharing.utils.permissions.PermissionsToAccessLocation
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.boatit.boatsharing.data.local.session.SessionController
import com.boatit.boatsharing.data.local.session.SessionEvent
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    value: String?,
    viewModel: NearByVoyagesViewModel = koinViewModel(),
    viewModelCancel: CancelBookedVoyageViewModel = koinViewModel(),
    viewModelFind: FindBoatViewModel = koinViewModel(),
    viewModelCurrent: GetActiveVoyageViewModel = koinViewModel(),
    viewModelN: NotificationViewModel = koinViewModel(),
    viewModelStripe: PaymentSheetConfigViewModel = koinViewModel(),
    viewModelP: PaymentViewModel = koinViewModel(),
    findBoatPrefillStore: FindBoatPrefillStore = get(FindBoatPrefillStore::class.java),
    voyageSessionStore: VoyageSessionStore = get(VoyageSessionStore::class.java),
    userSessionStore: UserSessionStore = get(UserSessionStore::class.java),
    sessionController: SessionController = get(SessionController::class.java),
) {
    val context = LocalContext.current
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedLocation by rememberSaveable { mutableStateOf<List<String>?>(null) }
    var currentLatLng by rememberSaveable { mutableStateOf(LatLng(40.792240, -73.138260)) }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(currentLatLng, 17f) }
    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }
    var publishableKey by remember { mutableStateOf<String?>(null) }
    var id by remember { mutableStateOf<String?>(null) }
    var PaymentIntentid by remember { mutableStateOf<String?>(null) }
    var ephemeralKeySecret by remember { mutableStateOf<String?>(null) }
    var voyageDetail by remember { mutableStateOf<ActiveVoyageDetails?>(null) }
    var isMenuIconVisible by rememberSaveable { mutableStateOf(true) }
    var showFindBoat by remember { mutableStateOf(false) }
    var showConfirmBooking by remember { mutableStateOf(false) }
    var showStartBooking by remember { mutableStateOf(false) }
    var showVoyageDetails by remember { mutableStateOf(false) }
    var pickupLocation by rememberSaveable { mutableStateOf("") }
    var dropOffLocation by rememberSaveable { mutableStateOf("") }
    var pickupDockId by rememberSaveable { mutableStateOf<Int?>(null) }
    var dropOffDockId by rememberSaveable { mutableStateOf<Int?>(null) }
    var showWaitingResponsePrompt by rememberSaveable { mutableStateOf(false) }
    var waitingResponsePromptValue by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val markerState = remember { MarkerState(position = currentLatLng) }
    val nearbyVm by viewModel.uiState.collectAsState()
    val logoutEvent = nearbyVm.logoutEvent
    if (navController.currentBackStackEntry?.savedStateHandle?.contains("result_key") == true) {
        val keyData = navController.currentBackStackEntry?.savedStateHandle?.get<String>("result_key").orEmpty()
        selectedLocation = keyData.split(":")
        selectedLocation?.forEachIndexed { index, part ->
            if (index == 0) {
                pickupLocation = part
            }
            if (index == 1) {
                dropOffLocation = part
            }
        }
        navController.currentBackStackEntry?.savedStateHandle?.remove<String>("result_key")
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
            Text("Permission Granted")
        },
    )

    val nearbyPlacesState = nearbyVm.nearbyPlaces
    val categoryState = nearbyVm.categories
    val activeVoyageUi by viewModelCurrent.uiState.collectAsState()
    val currentState = activeVoyageUi.voyageResult
    val findVm by viewModelFind.uiState.collectAsState()
    val findState = findVm.findBoatRequest
    val paymentUi by viewModelP.uiState.collectAsState()
    val paymentState = paymentUi.networkState
    val stripeUi by viewModelStripe.uiState.collectAsState()
    val stripeState = stripeUi.paymentSheetConfigState
    val notificationVm by viewModelN.uiState.collectAsStateWithLifecycle()
    val notificationState = notificationVm.notification
    val findBoatPrefillState by findBoatPrefillStore.state.collectAsState()
    val activeVoyageId by voyageSessionStore.voyageId.collectAsState()

    val dockOptions = (nearbyPlacesState as? NetworkResponse.Success)?.data ?: emptyList()
    val categoryOptions = (categoryState as? NetworkResponse.Success)?.data?.obj ?: emptyList()

    val stripeLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                showWaitingResponsePrompt = true
                waitingResponsePromptValue = "pay_now"
                if (activeVoyageId.isNotBlank()) {
                    viewModelP.payment(
                        PaymentConfirmationRequest(
                            activeVoyageId,
                            PaymentIntentid.orEmpty(),
                            "",
                        ),
                    )
                }
            } else if (result.resultCode == RESULT_CANCELED)
                {
                } else if (result.resultCode == RESULT_ERROR)
                {
                } else
                {}
        }

    LaunchedEffect(paymentState) {
        when (paymentState) {
            is NetworkResponse.Success -> {
                showWaitingResponsePrompt = false
                waitingResponsePromptValue = "pay_now"
                viewModelCurrent.onEvent(GetActiveVoyageUiEvent.FetchActiveVoyage)
                viewModelP.resetNearbyPlaces()
            }

            is NetworkResponse.Error -> {
                showFindBoat = false
            }

            else -> Unit
        }
    }

    LaunchedEffect(findState) {
        when (findState) {
            is NetworkResponse.Success -> {
                if (showWaitingResponsePrompt) {
                    showWaitingResponsePrompt = false
                    showFindBoat = false
                    viewModelFind.resetFindBoatRequestState()
                }
            }

            is NetworkResponse.Error -> {
                if (showWaitingResponsePrompt) {
                    showWaitingResponsePrompt = false
                    showFindBoat = false
                }
            }

            else -> Unit
        }
    }

    LaunchedEffect(stripeState) {
        when (stripeState) {
            is NetworkResponse.Success -> {
                showWaitingResponsePrompt = false
                showConfirmBooking = false
                showVoyageDetails = false
                showFindBoat = false
                paymentIntentClientSecret = stripeState.data?.obj?.ClientSecret
                id = stripeState.data?.obj?.CustomerId
                ephemeralKeySecret = stripeState.data?.obj?.EphemeralKey_Secret
                publishableKey = stripeState.data?.obj?.PublishableKey
                PaymentIntentid = stripeState.data?.obj?.PaymentIntentId
                val intent = Intent(context, StripeSheetActivity::class.java)
                intent.putExtra("publishableKey", publishableKey)
                intent.putExtra("ClientSecret", paymentIntentClientSecret)
                intent.putExtra("customerId", id)
                intent.putExtra("ephemeralKey", ephemeralKeySecret)
                stripeLauncher.launch(intent)
                viewModelStripe.onEvent(PaymentSheetConfigUiEvent.ResetPaymentSheetState)
            }

            is NetworkResponse.Error -> {
                showWaitingResponsePrompt = false
                showConfirmBooking = false
            }

            else -> Unit
        }
    }

    LaunchedEffect(currentState) {
        when (currentState) {
            is NetworkResponse.Success -> {
                val status = currentState.data?.obj?.Status
                when (status) {
                    "Started" -> {
                        showVoyageDetails = false
                        showConfirmBooking = false
                        showStartBooking = true
                        showFindBoat = true
                        voyageDetail = currentState.data?.obj
                        voyageSessionStore.setVoyageId(currentState.data?.obj?.Id.orEmpty())
                    }

                    "Accepted" -> {
                        showConfirmBooking = true
                        showFindBoat = true
                        showVoyageDetails = false
                        voyageDetail = currentState.data?.obj
                        voyageSessionStore.setVoyageId(currentState.data?.obj?.Id.orEmpty())
                    }

                    "Paid" -> {
                        showVoyageDetails = true
                        showConfirmBooking = false
                        showFindBoat = true
                        voyageDetail = currentState.data?.obj
                    }

                    "Completed" -> {
                        voyageDetail = currentState.data?.obj
                        showConfirmBooking = false
                        showFindBoat = false
                        showVoyageDetails = false
                        navController.navigate(route = InteractionRoutes.voyagerFeedback(currentState.data?.obj?.Id))
                    }
                }
                viewModelCurrent.onEvent(GetActiveVoyageUiEvent.Reset)
            }

            is NetworkResponse.Error,
            is NetworkResponse.Loading,
            -> Unit
        }
    }

    LaunchedEffect(notificationState) {
        viewModel.fetchNearbyPlaces()
        viewModel.fetchCategories()
        viewModelCurrent.onEvent(GetActiveVoyageUiEvent.FetchActiveVoyage)
    }

    LaunchedEffect(findBoatPrefillState) {
        if (findBoatPrefillState.pending) {
            showFindBoat = true
            if (findBoatPrefillState.target == "Pick") {
                pickupLocation = findBoatPrefillState.dockName
                pickupDockId = findBoatPrefillState.dockId
            } else if (findBoatPrefillState.target == "Drop") {
                dropOffLocation = findBoatPrefillState.dockName
                dropOffDockId = findBoatPrefillState.dockId
            }
        }
    }

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

                    when (nearbyPlacesState) {
                        is NetworkResponse.Loading -> { }
                        is NetworkResponse.Error -> { }
                        is NetworkResponse.Success -> {
                            nearbyPlacesState.data?.forEach { place ->
                                val position = LatLng(place.Latitude, place.Longitude)
                                Marker(
                                    state = MarkerState(position = position),
                                    title = place.Name,
                                    icon = BitmapDescriptorFactory.fromResource(R.drawable.location_icon_two),
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .width(80.dp)
                    .height(100.dp)
                    .padding(start = 20.dp, top = 40.dp)
                    .then(if (isMenuIconVisible) Modifier else Modifier.alpha(0f)),
            contentAlignment = Alignment.TopStart,
        ) {
            Image(
                painter = painterResource(id = R.drawable.wheel_icon),
                contentDescription = "Icon Image",
                modifier =
                    Modifier
                        .size(width = 80.dp, height = 80.dp)
                        .clickable {
                            if (!logoutEvent)
                                {
                                    navController.navigate(NavigationManager.MENU_OPTIONS_SCREEN)
                                }
                        },
            )
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .width(130.dp)
                    .height(130.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.wheel_icon),
                    contentDescription = "Icon Image",
                    modifier =
                        Modifier
                            .size(width = 120.dp, height = 120.dp)
                            .clickable { showFindBoat = true },
                )
                Spacer(Modifier.height(30.dp))
            }
        }
        if (showFindBoat)
            {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        showFindBoat = false
                        selectedLocation = null
                    },
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    containerColor = Color.Transparent,
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
                    if (showConfirmBooking)
                        {
                            voyageDetail?.let { detail ->
                                ConfirmBooking(
                                    navController,
                                    detail,
                                    onCancelClick = {
                                        viewModelCancel.fetchNearbyPlaces(CancelBookedVoyages(detail.Id.orEmpty(), ""))
                                        showConfirmBooking = false
                                        selectedLocation = null
                                        isMenuIconVisible = true
                                    },
                                    onPayNowClick = {
                                        if (activeVoyageId.isNotBlank()) {
                                            viewModelStripe.onEvent(
                                                PaymentSheetConfigUiEvent.LoadPaymentSheetConfig(activeVoyageId),
                                            )
                                        }
                                    },
                                )
                            }
                        } else if (showStartBooking)
                        {
                            voyageDetail?.let { StartVoyage(navController, it) }
                        } else if (showVoyageDetails)
                        {
                            voyageDetail?.let { detail ->
                                VoyageDetails(
                                    navController,
                                    detail,
                                    detail.OTP,
                                    detail.CaptainName,
                                    detail.BoatName,
                                    detail.BoatModel,
                                )
                            }
                        } else
                        {
                            FindBoat(
                                navController, modifier = Modifier.fillMaxWidth().height(screenHeight * 0.9f),
                                pickupLocation, dropOffLocation, "",
                                voyagerUserId = userSessionStore.currentUserId(),
                                pickupDockId = pickupDockId,
                                dropOffDockId = dropOffDockId,
                                categoryOptions = categoryOptions,
                                dockOptions = dockOptions,
                                businessPrefill = findBoatPrefillState,
                                onBusinessPrefillConsumed = { findBoatPrefillStore.consume() },
                                onCancelClick = {
                                    showFindBoat = false
                                    selectedLocation = null
                                    isMenuIconVisible = true
                                },
                                onFindBoatClick = {
                                    navController.navigate(NavigationManager.CREATE_VOYAGE_SCREEN)
                                },
                            )
                        }
                }
            }
        if (showWaitingResponsePrompt)
            {
                CustomDialog(
                    value = waitingResponsePromptValue,
                    onDismiss = {
                        if (waitingResponsePromptValue == "pay_now")
                            {
                                showWaitingResponsePrompt = false
                                showConfirmBooking = false
                            } else
                            {
                                showWaitingResponsePrompt = false
                                showConfirmBooking = true
                            }
                    },
                )
            }
        if (logoutEvent)
            {
                SessionDialog(
                    text = "Session expired, please login Again",
                    onCancel = {},
                    onPressOk = {
                        val loginRoute = sessionController.resolveRedirectRoute(SessionEvent.SessionExpired)
                        if (loginRoute != null) {
                            navController.navigateWithClearStack(loginRoute, clearStack = true)
                        }
                    },
                    showCancelButton = false,
                )
            }
    }
}
