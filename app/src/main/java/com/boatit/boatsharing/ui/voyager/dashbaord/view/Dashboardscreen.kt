package com.boatit.boatsharing.ui.voyager.dashbaord.view

import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.RestrictionsManager.RESULT_ERROR
import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.application.StripeSheetActivity
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.VOYAGER_FEEDBACK_SCREEN
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.ui.login.viewmodel.NotificationViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ActiveVoyageDetails
import com.boatit.boatsharing.ui.voyager.dashbaord.model.CancelBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.model.PaymentConfirmationRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.GetActiveVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.NearByVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.PaymentSheetConfigViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.PaymentViewModel
import com.boatit.boatsharing.uihelpers.CustomDialog
import com.boatit.boatsharing.uihelpers.SessionDialog
import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.permissions.PermissionsToAccessLocation
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
import com.skydoves.flexible.core.FlexibleSheetValue
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun DashboardScreen(navController: NavController, value: String?,
                    viewModel: NearByVoyagesViewModel = koinViewModel(),
                    viewModelCancel: CancelBookedVoyageViewModel = koinViewModel(),
                    viewModelFind: FindBoatViewModel = koinViewModel(),
                    viewModelCurrent: GetActiveVoyageViewModel = koinViewModel(),
                    viewModelN: NotificationViewModel = koinViewModel(),
                    viewModelStripe: PaymentSheetConfigViewModel = koinViewModel(),
                    viewModelP: PaymentViewModel = koinViewModel(), ) {

    val context = LocalContext.current
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedLocation by rememberSaveable { mutableStateOf<List<String>?>(null) }
    val defaultLatLng = LatLng(40.792240, -73.138260)
    var currentLatLng by rememberSaveable { mutableStateOf(defaultLatLng) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 17f)
    }

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
    var totalPassengers by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("Open date picker dialog") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var showWaitingResponsePrompt by rememberSaveable { mutableStateOf(false) }
    var waitingResponsePromptValue by rememberSaveable { mutableStateOf("") }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val CancelState by viewModelCancel.nearbyPlaces.collectAsState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val markerState = remember { MarkerState(position = defaultLatLng) }
    val logoutEvent by viewModel.logoutEvent.collectAsState()
    var currentSheetTarget by remember {
        mutableStateOf(FlexibleSheetValue.IntermediatelyExpanded)
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
        onPermissionDenied = {},
        content = {
            // Composable to show once permission is granted
            Text("Permission Granted")
        }
    )


    val nearbyPlacesState by viewModel.nearbyPlaces.collectAsState()
    val currentState by viewModelCurrent.loginState.collectAsState()
    val findState by viewModelFind.nearbyPlaces.collectAsState()
    val paymentState by viewModelP.loginState.collectAsState()
    val stripeState by viewModelStripe.loginState.collectAsState()
    val notificationState by viewModelN.notificationState.collectAsStateWithLifecycle()

    val stripeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){  result: ActivityResult ->
        if(result.resultCode == RESULT_OK) {
            showWaitingResponsePrompt = true
            waitingResponsePromptValue = "pay_now"
            viewModelP.payment(PaymentConfirmationRequest(
                AppConstants.Voyage_ID!!,
                PaymentIntentid!!,
                ""
            ))
        }else if(result.resultCode == RESULT_CANCELED){
        }else if(result.resultCode == RESULT_ERROR){
        }else{
        }
    }

    when (CancelState) {
        is NetworkResponse.Success -> {
            Toast.makeText(context, CancelState.message.toString(), Toast.LENGTH_SHORT).show()
            viewModelCancel.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {
            Toast.makeText(context, CancelState.message, Toast.LENGTH_SHORT).show()
            viewModelCancel.resetNearbyPlaces()
        }
        else -> {}
    }

    when (paymentState) {
        is NetworkResponse.Success -> {
            showWaitingResponsePrompt = false
            waitingResponsePromptValue = "pay_now"
            viewModelCurrent.voyages()
            viewModelP.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {
                showFindBoat = false
        }
        else -> {}
    }

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
                viewModelStripe.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {
                showWaitingResponsePrompt = false
                showConfirmBooking = false
                Toast.makeText(context, findState.message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    when (currentState) {
        is NetworkResponse.Success -> {
            Toast.makeText(context, currentState.data?.obj?.Status.toString(), Toast.LENGTH_SHORT).show()
            if(currentState.data?.obj?.Status?.equals("Started")!!){
                showVoyageDetails = false
                showConfirmBooking = false
                showStartBooking = true
                showFindBoat = true
                voyageDetail = currentState.data?.obj
                AppConstants.Voyage_ID = currentState.data!!.obj.Id
                AppConstants.Estimated_Cost = currentState.data!!.obj.AmountToPay
                AppConstants.Event_Time = currentState.data!!.obj.AmountToPay.toString()
            }else if(currentState.data?.obj?.Status?.equals("Accepted")!!){
                showConfirmBooking = true
                showFindBoat = true
                showVoyageDetails = false
                voyageDetail = currentState.data?.obj
                AppConstants.Voyage_ID = currentState.data!!.obj.Id
                AppConstants.Estimated_Cost = currentState.data!!.obj.AmountToPay
                AppConstants.Event_Time = currentState.data!!.obj.AmountToPay.toString()
            }else if(currentState.data?.obj?.Status?.equals("Paid")!!){
                showVoyageDetails = true
                showConfirmBooking = false
                showFindBoat = true
                voyageDetail = currentState.data?.obj
                println("ID" + voyageDetail?.Id)
            }

            else if(currentState.data?.obj?.Status?.equals("Completed")!!){
                voyageDetail = currentState.data?.obj
                showConfirmBooking = false
                showFindBoat = false
                showVoyageDetails = false
                navController.navigate(route = "$VOYAGER_FEEDBACK_SCREEN/" +  currentState.data?.obj?.Id)

            }else if(AppConstants.BusinessDock!!){
                showFindBoat = true
            }
            viewModelCurrent.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {
            println(currentState.message)
        }
        else -> {}
    }

    LaunchedEffect(notificationState) {
        if(AppConstants.BusinessDock!!){
            showFindBoat = true
        }
        viewModel.fetchNearbyPlaces()
        viewModel.fetchCategories()
        viewModelCurrent.voyages()
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
                        if(!logoutEvent){
                            navController.navigate(NavigationManager.MENU_OPTIONS_SCREEN)
                        }

                    }
            )

        }

        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .width(130.dp)
            .height(130.dp)
            ,contentAlignment = Alignment.BottomCenter) {

            Column {
                Image(
                    painter = painterResource(id = R.drawable.wheel_icon),
                    contentDescription = "Icon Image",
                    modifier = Modifier
                        .size(width = 120.dp, height = 120.dp)
                        .clickable { showFindBoat = true }
                )
                Spacer(Modifier.height(30.dp))
            }

        }
        if(showFindBoat){
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
                if(showConfirmBooking){
                    ConfirmBooking(
                        navController,
                        voyageDetail!!,
                        onCancelClick = {
                            viewModelCancel.fetchNearbyPlaces(CancelBookedVoyages(voyageDetail?.Id!!,""))
                            showConfirmBooking= false
                            selectedLocation = null
                            isMenuIconVisible = true
                        },
                        onPayNowClick = {
                            viewModelStripe.paymentConfig(AppConstants.Voyage_ID!!)
                        },
                    )
                }
                else if(showStartBooking){
                    StartVoyage(navController, voyageDetail!!)
                }
                else if (showVoyageDetails){
                    VoyageDetails(navController, voyageDetail!!, voyageDetail?.OTP, voyageDetail?.CaptainName, voyageDetail?.BoatName, voyageDetail?.BoatModel)
                }
                else{
                    FindBoat(
                        navController, modifier = Modifier.fillMaxWidth().height(screenHeight * 0.9f),
                        "", "", "",
                        onCancelClick = {
                            showFindBoat = false
                            selectedLocation = null
                            isMenuIconVisible = true
                            println(totalPassengers)
                        },
                        onFindBoatClick = {
                            navController.navigate(NavigationManager.CREATE_VOYAGE_SCREEN)
                        }
                    )
                }
            }
        }
        if (showWaitingResponsePrompt){
            CustomDialog(
                value = waitingResponsePromptValue,
                onDismiss = {
                    if (waitingResponsePromptValue=="pay_now"){
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
        if(logoutEvent){
            SessionDialog(
                text = "Session expired, please login Again",
                onCancel = {},
                onPressOk = {
                    navController.navigateWithClearStack(NavigationManager.LOGIN_SCREEN, clearStack = true)
                },
                showCancelButton = false
            )
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
