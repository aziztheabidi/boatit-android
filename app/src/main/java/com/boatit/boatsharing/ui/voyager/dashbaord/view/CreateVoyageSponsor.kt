package com.boatit.boatsharing.ui.voyager.dashbaord.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.signup.general.repository.GetVoyagerProfileViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BookVoyageRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.Sponser
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.BookVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDialog
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateVoyageSponsorScreen(navController: NavController,
      viewModelFind: FindBoatViewModel = koinViewModel(),
      viewModel: BookVoyageViewModel = koinViewModel()) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    var firstName by remember { mutableStateOf("") }
    var findBoat by remember { mutableStateOf("Find Boat") }
    var dob by remember { mutableStateOf("") }
    var paypalEmail by remember { mutableStateOf("") }
    val showDialog = mutableStateOf(false)
    var bookingDate by remember { mutableStateOf("") }


    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var getingData by remember { mutableStateOf(true) }
    var isNetworkError by remember { mutableStateOf(false) }
    var showWaitingResponsePrompt by rememberSaveable { mutableStateOf(false) }
    var waitingResponsePromptValue by rememberSaveable { mutableStateOf("") }
    var splitPaymentSwitchState by rememberSaveable { mutableStateOf(false) }

    val isValidate = true

    val handleError = {
        errorMessage = null
        isError = false
    }

    val registrationState by viewModel.nearbyPlaces.collectAsState()
    val findState by viewModelFind.nearbyPlaces.collectAsState()

    fun performLogin(){
        navController.navigate(NavigationManager.VOYAGE_BOOKED_SCREEN)
    }

    when (registrationState) {
        is NetworkResponse.Success -> {
            if(isLoading){
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, registrationState.data?.Message , Toast.LENGTH_SHORT).show()
                println("Message" + AppConstants.Voyage_ID)
                AppConstants.Voyage_ID = registrationState.data?.obj
                performLogin()
            }
        }
        is NetworkResponse.Error -> {
            if(isLoading){
                isLoading = false
                isNetworkError = true
                errorMessage = "Network error, please try again."
                Toast.makeText(context, (registrationState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
                println("Message" + AppConstants.Voyage_ID)
                performLogin()
            }
        }
        else -> {}
    }

    when (findState) {
        is NetworkResponse.Success -> {
            if (showWaitingResponsePrompt) {
                showWaitingResponsePrompt = false
                isLoading = false
                isNetworkError = false
                viewModelFind.resetNearbyPlaces()
                Toast.makeText(context, "Finding the Boat", Toast.LENGTH_SHORT).show()
                navController.navigate(route = "$DASHBOARD_SCREEN/True")
            }
        }
        is NetworkResponse.Error -> {
            if (showWaitingResponsePrompt) {
                showWaitingResponsePrompt = false
                isLoading = false
                isNetworkError = true
                Toast.makeText(context, findState.message, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = "Create Voyage", onImageClick = {
                println("clicked...")
            })

        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding() + 15.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = innerPadding.calculateTopPadding() + 25.dp,
                    )
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                FormStepsViews(
                    numberOfViews = 1,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 1
                )

                if (showDialog.value) {
                    MyDatePickerDialog(
                        onDateSelected = {
                            bookingDate = it
                            dob = bookingDate },
                        onDismiss = { showDialog.value = false }
                    )
                }

                Spacer(Modifier.height(30.dp))



                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Total Fair"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = firstName,
                    placeholderText = "Select Date",
                    onTextChange = { firstName = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (firstName.isNotEmpty()&& firstName.length <= 3) stringResource(R.string.firstname_validation_text) else null,
                    isError = firstName.isNotEmpty()&& firstName.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.dollar),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {  Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    text = "Split Payment"
                )
                    Switch(
                        checked = splitPaymentSwitchState,
                        onCheckedChange = { splitPaymentSwitchState = it
                          if(splitPaymentSwitchState){
                              findBoat = "Book Voyage"
                              AppConstants.sponsorList.add(Sponser(VoyagerUserId = AppConstants.USER_ID!!))
                          }else{
                              findBoat = "Find Boat"
                          }

                                          },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = colorResource(id = R.color.button_normal),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFD9D9D9)
                        )
                    )                }
                Spacer(Modifier.height(15.dp))


                if (splitPaymentSwitchState){
                    Text(
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500
                        ),
                        text = "Add Sponsors"
                    )

                    Spacer(Modifier.height(10.dp))

                    Card(
                        modifier = Modifier
                            .width(70.dp)
                            .height(70.dp)
                            .padding(3.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            if (showDialog.value) {
                                MyDatePickerDialog(
                                    onDateSelected = { bookingDate = it },
                                    onDismiss = { showDialog.value = false }
                                )
                            }

                            Icon(
                                painter = painterResource(id = R.drawable.add_sponsor),
                                contentDescription = "Icon",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        showDialog.value = true
                                        navController.navigate(NavigationManager.SPONSOR_SCREEN)

                                    },
                                tint = colorResource(R.color.button_normal)
                            )


                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    Text(
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500
                        ),
                        text = "Number of Sponsors"
                    )

                    Spacer(Modifier.height(5.dp))


                    CustomTextField(
                        textValue = firstName,
                        placeholderText = "Select Date",
                        onTextChange = { firstName = it },
                        keyboardType = KeyboardType.Text,
                        maxChars = 100,
                        errorMessage = if (firstName.isNotEmpty()&& firstName.length <= 3) stringResource(R.string.firstname_validation_text) else null,
                        isError = firstName.isNotEmpty()&& firstName.length <= 3,
                        onClearError = handleError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { lastNameFocusRequester.requestFocus() }
                        ),
                        focusRequester = firstNameFocusRequester,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.passengers),
                                contentDescription = "Icon",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Unspecified
                            )
                        }
                    )
                }

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Individuals"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = firstName,
                    placeholderText = "Select Date",
                    onTextChange = { firstName = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (firstName.isNotEmpty()&& firstName.length <= 3) stringResource(R.string.firstname_validation_text) else null,
                    isError = firstName.isNotEmpty()&& firstName.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.dollar),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Pickup"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = AppConstants.Pick_Up_Loc!!,
                    placeholderText = AppConstants.Pick_Up_Loc!!,
                    onTextChange = { firstName = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.location_icon),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Dropoff"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = AppConstants.Drop_Off_Loc!!,
                    placeholderText = AppConstants.Drop_Off_Loc!!,
                    onTextChange = { firstName = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.drop_off_loc_icon),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(Modifier.height(15.dp))

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = findBoat,
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {
                        if(splitPaymentSwitchState){
                            viewModel.bookVoyageVMfunc(
                                BookVoyageRequest(
                                    VoyagerUserId = AppConstants.USER_ID!!,
                                    PickupDockId = 1,
                                    DropOffDockId= 2,
                                    NoOfVoyagers= 3,
                                    IsImmediately= false,
                                    IsSplitPayment = true,
                                    BookingDate = "2025-05-30",
                                    StartTime = "10:00:00",
                                    IsStayOnWater =  false,
                                    EndTime= "12:12:00",
                                    PerHourRate= 123.0,
                                    DurationInHours= 4.0,
                                    NoOfSponsers= 2,
                                    EstimatedCost= 34.0,
                                    IndvidualAmount= 23.0,
                                    Sponsers = AppConstants.sponsorList,
                                ))
                        }else{
                            viewModelFind.fetchNearbyPlaces(
                                FindBoatRequest(
                                    VoyagerUserId = AppConstants.USER_ID!!,
                                    PickupDockId = 1,
                                    DropOffDockId= 2,
                                    NoOfVoyagers= 3
                                )
                            )
                        }
                        showWaitingResponsePrompt = true
                        waitingResponsePromptValue = "find_boat"
                        isButtonEnabled = true
                        isLoading = true
                        focusManager.clearFocus()
                        println("perform network call")
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (showWaitingResponsePrompt){
                CustomDialog(
                    value = waitingResponsePromptValue,
                    onDismiss = {
                        if (waitingResponsePromptValue=="pay_now"){
                            showWaitingResponsePrompt = false
                        }
                        else{
                            showWaitingResponsePrompt = false
                        }
                    },
                )
            }
        },
    )
}

@Preview
@Composable
fun CreateVoyageSponsorScreen() {
    CreateVoyageSponsorScreen(navController = rememberNavController())
}