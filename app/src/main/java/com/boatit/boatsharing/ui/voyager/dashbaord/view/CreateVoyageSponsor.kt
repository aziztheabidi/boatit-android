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
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.signup.general.repository.GetVoyagerProfileViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BookVoyageRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.Sponser
import com.boatit.boatsharing.ui.voyager.dashbaord.model.SponsorVoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.BookVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDialog
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.uihelpers.MissingPaymentDialog
import com.boatit.boatsharing.uihelpers.SessionDialog
import com.boatit.boatsharing.uihelpers.VoyageBookDialog
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateVoyageSponsorScreen(navController: NavController,
      splitPayment: Boolean,
      viewModelFind: FindBoatViewModel = koinViewModel(),
      viewModel: BookVoyageViewModel = koinViewModel()) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }

    var findBoat by remember { mutableStateOf("Find Boat") }
    var dob by remember { mutableStateOf("") }
    var paypalEmail by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }



    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var getingData by remember { mutableStateOf(true) }
    var isNetworkError by remember { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var showWaitingResponsePrompt by rememberSaveable { mutableStateOf(false) }
    var waitingResponsePromptValue by rememberSaveable { mutableStateOf("") }
    var splitPaymentSwitchState by rememberSaveable { mutableStateOf(false) }

    val isValidate = true

    var responseErrorText by remember { mutableStateOf("") }


    val registrationState by viewModel.nearbyPlaces.collectAsState()
    val findState by viewModelFind.nearbyPlaces.collectAsState()

    LaunchedEffect(Unit) {
        if(AppConstants.Split!!){
            splitPaymentSwitchState = true;
            findBoat = "Book Voyage"
        }else if(!AppConstants.Travel_Now!!){
            splitPaymentSwitchState = true;
            findBoat = "Book Voyage"
        }
    }

    fun performLogin(){
        navController.navigate(NavigationManager.VOYAGE_BOOKED_SCREEN)
    }

    when (registrationState) {
        is NetworkResponse.Success -> {
            if(isLoading){
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, registrationState.data?.Message , Toast.LENGTH_SHORT).show()
                AppConstants.Voyage_ID = registrationState.data?.obj
                performLogin()
            }
        }
        is NetworkResponse.Error -> {
            if(isLoading){
                isLoading = false
                isNetworkError = true
                errorMessage = "Network error, please try again."
                showDialog = true
                message = (registrationState as NetworkResponse.Error).message!!
            }
        }
        else -> {}
    }

    when (findState) {
        is NetworkResponse.Success -> {
            if (isLoading) {
                showWaitingResponsePrompt = false
                isLoading = false
                isNetworkError = false
                viewModelFind.resetNearbyPlaces()
                Toast.makeText(context, "Finding the Boat", Toast.LENGTH_SHORT).show()
                navController.navigate(route = "$DASHBOARD_SCREEN/True")
            }
        }
        is NetworkResponse.Error -> {
            if (isLoading) {
                showWaitingResponsePrompt = false
                isLoading = false
                isNetworkError = true
                showErrorDialog = true
                responseErrorText = findState.message.toString()
               // Toast.makeText(context, findState.message, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = "Create Voyage", onImageClick = {
                navController.popBackStack()
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
                    textValue = AppConstants.Total_Cost.toString(),
                    placeholderText = AppConstants.Total_Cost.toString(),
                    onTextChange = {  },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = {},
                    imeAction = ImeAction.Next,
                    isEditable = false,
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
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                painter = painterResource(id = R.drawable.add_sponsor),
                                contentDescription = "Icon",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
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
                        textValue = AppConstants.sponsorList.size.toString(),
                        placeholderText = AppConstants.sponsorList.size.toString(),
                        onTextChange = {},
                        keyboardType = KeyboardType.Text,
                        maxChars = 100,
                        errorMessage = null,
                        isError = false,
                        onClearError = {},
                        isEditable = false,
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
                    textValue = AppConstants.Estimated_Cost.toString(),
                    placeholderText = AppConstants.Estimated_Cost.toString(),
                    onTextChange = {  },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage =  null,
                    isError = false,
                    onClearError = {  },
                    imeAction = ImeAction.Next,
                    isEditable = false,
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
                    textValue = AppConstants.Pick_Up_Loc?.second!!,
                    placeholderText = AppConstants.Pick_Up_Loc?.second!!,
                    onTextChange = { },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = {},
                    imeAction = ImeAction.Next,
                    isEditable = false,
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
                    textValue = AppConstants.Drop_Off_Loc?.second!!,
                    placeholderText = AppConstants.Drop_Off_Loc?.second!!,
                    onTextChange = {  },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = {},
                    isEditable = false,
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
                                    Name = AppConstants.Event_Name!!,
                                    VoyageCategoryId = AppConstants.Cat_id!!,
                                    PickupDockId = AppConstants.Pick_Up_Loc?.first!!,
                                    DropOffDockId= AppConstants.Drop_Off_Loc?.first!!,
                                    NoOfVoyagers= AppConstants.No_Of_Voyagers!!,
                                    IsImmediately= AppConstants.Travel_Now!!,
                                    IsSplitPayment = true,
                                    BookingDate = AppConstants.Event_Date.toString(),
                                    StartTime = AppConstants.Event_Time!!,
                                    IsStayOnWater =  AppConstants.Stay_on_water!!,
                                    EndTime= AppConstants.Event_Time_End!!,
                                    PerHourRate= AppConstants.Per_Hour_Rate!!,
                                    DurationInHours= AppConstants.No_of_Hour!!,
                                    NoOfSponsers= AppConstants.sponsorList.size,
                                    EstimatedCost= AppConstants.Total_Cost!!,
                                    IndvidualAmount= AppConstants.Estimated_Cost!!,
                                    Sponsers = AppConstants.sponsorList,
                                ))
                        }else{
                            viewModelFind.fetchNearbyPlaces(
                                FindBoatRequest(
                                    VoyagerUserId = AppConstants.USER_ID!!,
                                    Name = AppConstants.Event_Name!!,
                                    VoyageCategoryId = AppConstants.Cat_id!!,
                                    PickupDockId = AppConstants.Pick_Up_Loc?.first!!,
                                    DropOffDockId= AppConstants.Drop_Off_Loc?.first!!,
                                    NoOfVoyagers= AppConstants.No_Of_Voyagers!!,
                                    EstimatedCost = AppConstants.Total_Cost!!,
                                    IsImmediately = true,
                                    IsSplitPayment = false,
                                    BookingDate = AppConstants.Event_Date.toString()
                                )
                            )
                        }
                        isButtonEnabled = true
                        isLoading = true
                        focusManager.clearFocus()
                        
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (showDialog) {
                    VoyageBookDialog(
                        name = message,
                        onPayNow = {
                            showDialog = false
                        },
                        onDismissRequest = {  }
                    )
                }


                if(showErrorDialog) {

                    SessionDialog(
                        text = responseErrorText,
                        onCancel = {},
                        onPressOk = {
                            showErrorDialog =false
                        },
                        showCancelButton = false
                    )
                }
            }
        },
    )
}
