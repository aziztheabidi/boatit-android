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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextOverflow
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
import com.boatit.boatsharing.routes.NavigationManager.CREATE_ACCOUNT_STEP_TWO_SCREEN
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.ui.signup.general.repository.GetVoyagerProfileViewModel
import com.boatit.boatsharing.ui.signup.general.repository.VoyagerProfileViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDobField
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar


@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateVoyageRateCalcScreen(navController: NavController, viewModel: VoyagerProfileViewModel = koinViewModel(), viewModelfeth: GetVoyagerProfileViewModel = koinViewModel()) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var paypalEmail by remember { mutableStateOf("") }
    val showDialog = mutableStateOf(false)
    var bookingDate by remember { mutableStateOf("") }
//    var eventTime by remember { mutableStateOf(AppConstants.Event_Time) }
    var eventTime = AppConstants.Event_Time


    val isEmailValid = paypalEmail.contains("@") && paypalEmail.contains(".")
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var getingData by remember { mutableStateOf(true) }
    var isNetworkError by remember { mutableStateOf(false) }


    val isValidate = true

    val handleError = {
        errorMessage = null
        isError = false
    }

    val registrationState by viewModel.registrationState.collectAsState()
    val fetchState by viewModelfeth.registrationState.collectAsState()

    fun performLogin(){
        navController.popBack()
    }

    when (registrationState) {
        is NetworkResponse.Success -> {
            if(isLoading){
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, registrationState.data?.Message , Toast.LENGTH_SHORT).show()
                performLogin()
            }
        }
        is NetworkResponse.Error -> {
            if(isLoading){
                isLoading = false
                isNetworkError = true
                errorMessage = "Network error, please try again."
                Toast.makeText(context, (registrationState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    when (fetchState) {
        is NetworkResponse.Success -> {
            if(getingData) {
                phoneNumber = fetchState.data?.obj?.PhoneNumber.toString()
                firstName = fetchState.data?.obj?.FirstName.toString()
                lastName = fetchState.data?.obj?.LastName.toString()
                address = fetchState.data?.obj?.Address.toString()
                dob = fetchState.data?.obj?.DateOfBirth.toString()
                paypalEmail = fetchState.data?.obj?.StripeEmail.toString()
                getingData = false
            }
        }
        is NetworkResponse.Error -> {
            getingData = false
        }
        else -> {}
    }

    LaunchedEffect(getingData) {
        viewModelfeth.GetVoyagerProfile()
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = "Create Voyage", onImageClick = {
                println("clicked...")
            })

        },
        content = { innerPadding ->
            if (getingData) {
                Dialog(
                    onDismissRequest = {},
                    DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                ){
                    Box(
                        contentAlignment=  Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .background(White, shape = RoundedCornerShape(8.dp))
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kindly fill in the relevant details for the Voyage",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    Spacer(Modifier.width(4.dp))

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
                                painter = painterResource(id = R.drawable.event_calender),
                                contentDescription = "Icon",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        showDialog.value = true
                                    },
                                tint = colorResource(R.color.button_normal)
                            )

                            Text(
                                text = bookingDate,
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                ),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }


                Spacer(Modifier.height(30.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Name of the Event"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = AppConstants.Event_Name!!,
                    placeholderText = AppConstants.Event_Name!!,
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

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "No. of Voyagers"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = AppConstants.No_Of_Voyagers.toString()!!,
                    placeholderText = AppConstants.No_Of_Voyagers.toString()!!,
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

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Per Hour Rate"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = AppConstants.Per_Hour_Rate.toString()!!,
                    placeholderText = AppConstants.Per_Hour_Rate.toString()!!,
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
                    text = "Estimated Cost"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = AppConstants.Estimated_Cost.toString()!!,
                    placeholderText = AppConstants.Estimated_Cost.toString()!!,
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
                            painter = painterResource(id = R.drawable.calculator),
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
                    text = "Time"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = eventTime!!,
                    placeholderText = eventTime,
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
                            painter = painterResource(id = R.drawable.clock),
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
                    isError =false,
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
                    text = "Drop off"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue =  AppConstants.Drop_Off_Loc!!,
                    placeholderText =  AppConstants.Drop_Off_Loc!!,
                    onTextChange = { firstName = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError =false,
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





                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = "Proceed",
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {
                        viewModel.saveProfile(VoyagerProfileRequest(
                            UserId = AppConstants.USER_ID,
                            PhoneNumber = phoneNumber,
                            FirstName = firstName,
                            LastName = lastName,
                            Address = address,
                            DateOfBirth = dob,
                            StripeEmail = paypalEmail)
                        )
                        isButtonEnabled = true
                        isLoading = true
                        focusManager.clearFocus()
                        println("perform network call")
                        navController.navigate(NavigationManager.CREATE_VOYAGE_SPONSOR_SCREEN)

                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        },
    )
}

@Preview
@Composable
fun CreateVoyageRateCalc() {
    CreateVoyageRateCalcScreen(navController = rememberNavController())
}