package com.boatit.boatsharing.ui.voyager.dashbaord.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.ui.login.viewmodel.NotificationViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.GetActiveVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.NearByVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.PaymentSheetConfigViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.PaymentViewModel
import com.boatit.boatsharing.uihelpers.CustomDialog
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
import com.skydoves.flexible.bottomsheet.material.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.FlexibleSheetValue
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun DashboardScreen(navController: NavController, value: String?,
    viewModel: NearByVoyagesViewModel = koinViewModel(),
    viewModelFind: FindBoatViewModel = koinViewModel(),
    viewModelCurrent: GetActiveVoyageViewModel = koinViewModel(),
    viewModelN: NotificationViewModel = koinViewModel(),
    viewModelStripe: PaymentSheetConfigViewModel = koinViewModel(),
    viewModelP: PaymentViewModel = koinViewModel(), ) {


    val context = LocalContext.current
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val seaRoute = listOf(
        LatLng(40.65209, -73.13763), // Start
        LatLng(40.70000, -73.10000), // Waypoint 1 (ocean path)
        LatLng(40.73000, -73.05000), // Waypoint 2 (ocean path)
        LatLng(40.75808, -73.01926)  // End
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)
    var selectedLocation by rememberSaveable { mutableStateOf<List<String>?>(null) }
    val defaultLatLng = LatLng(40.792240, -73.138260)
    var currentLatLng by rememberSaveable { mutableStateOf(defaultLatLng) }

    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(defaultLatLng, 17f)
        position = CameraPosition.fromLatLngZoom(seaRoute.first(), 4f)
    }

    var customerConfig by remember { mutableStateOf<PaymentSheet.CustomerConfiguration?>(null) }
    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }
    var isMenuIconVisible by rememberSaveable { mutableStateOf(true) }
    var showFindBoat by rememberSaveable { mutableStateOf(false) }
    var showConfirmBooking by rememberSaveable { mutableStateOf(false) }
    var showVoyageDetails by rememberSaveable { mutableStateOf(false) }
    var pickupLocation by rememberSaveable { mutableStateOf("") }
    var dropOffLocation by rememberSaveable { mutableStateOf("") }
    var totalPassengers by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("Open date picker dialog") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var showWaitingResponsePrompt by rememberSaveable { mutableStateOf(false) }
    var waitingResponsePromptValue by rememberSaveable { mutableStateOf("") }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val markerState = remember { MarkerState(position = defaultLatLng) }
    var currentSheetTarget by remember {
        mutableStateOf(FlexibleSheetValue.IntermediatelyExpanded)
    }
    val sheetStateF = rememberFlexibleBottomSheetState(
        flexibleSheetSize = FlexibleSheetSize(
            fullyExpanded = 0.9f,
            intermediatelyExpanded = 0.5f,
            slightlyExpanded = 0.18f,
        ),
        isModal = false,
        skipSlightlyExpanded = false,
        skipHiddenState = true
    )

    val handleError = {
        errorMessage = null
        isError = false
    }

    if (navController.currentBackStackEntry?.savedStateHandle?.contains("result_key") == true) {
        val keyData = navController.currentBackStackEntry!!.savedStateHandle.get<String>("result_key") ?: ""
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
        onPermissionDenied = {}
    )

    val nearbyPlacesState by viewModel.nearbyPlaces.collectAsState()
    val currentState by viewModelCurrent.loginState.collectAsState()
    val findState by viewModelFind.nearbyPlaces.collectAsState()
    val paymentState by viewModelP.loginState.collectAsState()
    val stripeState by viewModelStripe.loginState.collectAsState()
    val notificationState by viewModelN.notificationState.collectAsStateWithLifecycle()

    when (findState) {
        is NetworkResponse.Success -> {
            if (showWaitingResponsePrompt) {
                showWaitingResponsePrompt = false
                showFindBoat = false
                viewModelFind.resetNearbyPlaces()
                Toast.makeText(context, "Finding the Boat", Toast.LENGTH_SHORT).show()
            }
        }
        is NetworkResponse.Error -> {
            if (showWaitingResponsePrompt) {
                showWaitingResponsePrompt = false
                showFindBoat = false
                Toast.makeText(context, findState.message, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    when (stripeState) {
        is NetworkResponse.Success -> {
            if (showWaitingResponsePrompt) {
                showWaitingResponsePrompt = false
                showConfirmBooking = false
                showVoyageDetails = true
                showFindBoat = true
                val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)
                paymentIntentClientSecret = "pi_3RGIXgIiYO00MT0y2JsZIk3L_secret_VQVooKaU2tZ6DcqOvoA8d6ZeG"
                customerConfig = PaymentSheet.CustomerConfiguration(
                    id = "cus_RuWb1Rjvzm01Nk",
                    ephemeralKeySecret = "ephkey_1RGIXhIiYO00MT0yL2ADgNCx"
                )
                val publishableKey = "pk_test_51N8B6wIiYO00MT0yE2hZ0oQEf1VyHKzAtZyGuiFCRrx8eo5swxsYKzBKBNEGWuO4hzqHnHCzX9EYBJDLt1mmmsX000BtNUImoB"
                PaymentConfiguration.init(context, publishableKey)
                presentPaymentSheet(paymentSheet, customerConfig!!, paymentIntentClientSecret!!)
            }
        }
        is NetworkResponse.Error -> {
            if (showWaitingResponsePrompt) {
                showWaitingResponsePrompt = false
                showConfirmBooking = false
                Toast.makeText(context, findState.message, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    when (currentState) {
        is NetworkResponse.Success -> {
            if(currentState.data?.obj?.Status?.equals("Started")!!){
//                Toast.makeText(context, "Anni Deya", Toast.LENGTH_SHORT).show()
//                showConfirmBooking = false
//                showFindBoat = false
//                showVoyageDetails = false
//                navController.navigate(NavigationManager.VOYAGE_STARTED_SCREEN_Voyager)
            }else if(currentState.data?.obj?.Status?.equals("Accepted")!!){
                showConfirmBooking = true
                showFindBoat = false
                showVoyageDetails = false
                AppConstants.Voyage_ID = currentState.data!!.obj.Id
            }else if(currentState.data?.obj?.Status?.equals("Paid")!!){
                showVoyageDetails = true
                showConfirmBooking = false
                showFindBoat = false
            }
            viewModelCurrent.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {
        }
        else -> {}
    }

    LaunchedEffect(notificationState) {
        viewModel.fetchNearbyPlaces()
//        viewModelCurrent.voyages()
    }

    LaunchedEffect(value) {
        if (value != null && value == "True") { // You can define the condition here
            showConfirmBooking = true
            showFindBoat = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()
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
                        mapType = MapType.NORMAL
                    )
                ) {
                    Marker(
                        state = markerState,
                        title = "Its me",
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.current_marker)
                    )

                    if(showFindBoat){
                        Polyline(
                            points = seaRoute,
                            color = Color.Blue,
                            width = 8f,
                            geodesic = true // Smooths the polyline for curved sea paths
                        )
                    }

                    when (nearbyPlacesState) {
                        is NetworkResponse.Loading -> {  }
                        is NetworkResponse.Error -> { }
                        is NetworkResponse.Success -> {
                            nearbyPlacesState.data?.forEach { place ->
                                val position = LatLng(place.Latitude, place.Longitude)
                                println(position)
                                Marker(
                                    state = MarkerState(position = position),
                                    title = place.Name,
                                    icon = BitmapDescriptorFactory.fromResource(R.drawable.location_icon_two)
                                )
                            }
                        }
                    }
                }
            }

        }

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .width(80.dp)
                .height(100.dp)
                .padding(start = 20.dp, top = 40.dp)
                .then(if (isMenuIconVisible) Modifier else Modifier.alpha(0f)),
                 contentAlignment = Alignment.TopStart,
            )  {

            Image(
                painter = painterResource(id = R.drawable.wheel_icon),
                contentDescription = "Icon Image",
                modifier = Modifier
                    .size(width = 80.dp, height = 80.dp)
                    .clickable {
                        navController.navigate(NavigationManager.MENU_OPTIONS_SCREEN)
                    }
            )

        }

        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .width(130.dp)
            .height(130.dp)
                , contentAlignment = Alignment.BottomCenter,) {

            Column {
                Image(
                    painter = painterResource(id = R.drawable.wheel_icon),
                    contentDescription = "Icon Image",
                    modifier = Modifier
                        .size(width = 120.dp, height = 120.dp)
                        .clickable {
//                            presentPaymentSheet(paymentSheet, customerConfig!!, paymentIntentClientSecret!!)
                            navController.navigate(NavigationManager.FIND_LOCATION_SCREEN)
                        }
                )
                Spacer(Modifier.height(30.dp))
            }

        }

        if(showFindBoat){
            FlexibleBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch { sheetState.partialExpand() // Always revert to partial expansion
                    }
                    showFindBoat = false
                    selectedLocation = null
                },
                sheetState = sheetStateF,
                onTargetChanges = { sheetValue ->
                    currentSheetTarget = sheetValue
                },
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = Color.Transparent,
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
                if (showConfirmBooking){
                    ConfirmBooking(
                        navController,
                        onCancelClick = {
                            showConfirmBooking= false
                            selectedLocation = null
                            isMenuIconVisible = true
                        },
                        onPayNowClick = {
                            showWaitingResponsePrompt = true
                            waitingResponsePromptValue = "pay_now"
                            viewModelStripe.paymentConfig("67EDA486-6BF8-469A-BBF6-5EF72CC7CED1")
                        },
                    )

                }
                else if (showVoyageDetails){
                    VoyageDetails(navController, 17890, "sassasa", "maskataza", "6969" )
                }
                else{
                    FindBoat(
                        navController, modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.75f),
                        pickupLocation, dropOffLocation, totalPassengers,
                        onCancelClick = {
                            showFindBoat = false
                            selectedLocation = null
                            isMenuIconVisible = true
                            println(totalPassengers)
                        },
                        onFindBoatClick = {
                            AppConstants.Pick_Up_Loc = pickupLocation
                            AppConstants.Drop_Off_Loc = dropOffLocation
                            showWaitingResponsePrompt = true
                            waitingResponsePromptValue = "find_boat"
                            navController.navigate(NavigationManager.CREATE_VOYAGE_SCREEN)
                        }
                    )
                }
            }
        }

        if (!selectedLocation.isNullOrEmpty()){
            showFindBoat = true
            isMenuIconVisible = false
            val boundsBuilder = LatLngBounds.Builder()
            seaRoute.forEach { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()
            cameraPositionState.move(
                update = CameraUpdateFactory.newLatLngBounds(bounds, 100)
            )
        }

        if (showWaitingResponsePrompt){
            CustomDialog(
                value = waitingResponsePromptValue,
                onDismiss = {
                    if (waitingResponsePromptValue=="pay_now"){
                        showVoyageDetails = true
                        showWaitingResponsePrompt = false
                        showConfirmBooking =false
                    }
                    else{
                        showWaitingResponsePrompt = false
                        showConfirmBooking =true
                    }
                },
                )
        }
    }

}

private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String
) {
    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = "Boat App",
            customer = customerConfig,
            allowsDelayedPaymentMethods = true
        )
    )
}

private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
    when(paymentSheetResult) {
        is PaymentSheetResult.Canceled -> {
            print("Canceled")
        }
        is PaymentSheetResult.Failed -> {
            print("Error: ${paymentSheetResult.error}")
        }
        is PaymentSheetResult.Completed -> {
//            viewModelP.payment(VoyagePaymentRequest(AppConstants.Voyage_ID!!))
            print("Completed")
        }
    }
}

@Preview
@Composable
fun PreviewDashboardScreen() {
    DashboardScreen(
        navController = rememberNavController(),
        value = null)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(onDateSelected: (String) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDateMillis = datePickerState.selectedDateMillis
    val selectedDate = selectedDateMillis?.let {
        val calendar = Calendar.getInstance().apply { timeInMillis = it }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Months are 0-based, so add 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        String.format("%04d-%02d-%02d", year, day, month)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate.toString())
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}
