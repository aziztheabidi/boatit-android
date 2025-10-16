package com.boatit.boatsharing.ui.signup.general.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.CREATE_ACCOUNT_STEP_TWO_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
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
fun UserAccountInfoScreen(navController: NavController,
                          value: String?, viewModel: VoyagerProfileViewModel = koinViewModel(), viewModelfeth: GetVoyagerProfileViewModel = koinViewModel()) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }
    val addressFocusRequester = remember { FocusRequester() }
    val dobFocusRequester = remember { FocusRequester() }
    val paypalFocusRequester = remember { FocusRequester() }


    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var paypalEmail by remember { mutableStateOf("") }
    val showDialog = mutableStateOf(false)
    var bookingDate by remember { mutableStateOf("") }



    val isEmailValid = paypalEmail.contains("@") && paypalEmail.contains(".")
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var getingData by remember { mutableStateOf(true) }
    var isNetworkError by remember { mutableStateOf(false) }


    val isValidate = firstName.isNotEmpty()
            && lastName.isNotEmpty()
            && phoneNumber.isNotEmpty()
            && address.isNotEmpty()
            && dob.isNotEmpty()
            && paypalEmail.isNotEmpty()
            && isEmailValid

    val handleError = {
        errorMessage = null
        isError = false
    }

    val registrationState by viewModel.registrationState.collectAsState()
    val fetchState by viewModelfeth.registrationState.collectAsState()

    fun performLogin(){
        if(value.equals("voyagerRole")){
            navController.navigate(route = "$DASHBOARD_SCREEN/null")
        }else{ navController.popBack() }

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
            Toast.makeText(context, fetchState.message, Toast.LENGTH_SHORT).show()
            getingData = false
        }
        else -> {}
    }

    LaunchedEffect(getingData) {
        viewModelfeth.GetVoyagerProfile()
    }

    Scaffold(
        containerColor = White,
        topBar = {
            if (value.toString() == "captainRole"){
                CustomTopBar(text = stringResource(R.string.add_your_acc_info)+" 1/3", onImageClick = {
                    navController.popBackStack()
                })
            }
            else if (value.toString() == "businessRole"){
                CustomTopBar(text = stringResource(R.string.add_your_acc_info)+" 1/4", onImageClick = {
                    
                })
            }
            else{
                CustomTopBar(text = stringResource(R.string.add_your_acc_info), onImageClick = {
                    
                    navController.popBackStack()
                })
            }

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

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.firstname_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = firstName,
                    placeholderText = stringResource(R.string.firstname_placeholder),
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
                    focusRequester = firstNameFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.lastname_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = lastName,
                    placeholderText = stringResource(R.string.lastname_placeholder),
                    onTextChange = { lastName = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (lastName.isNotEmpty()&& lastName.length <= 3) stringResource(R.string.lastname_validation_text) else null,
                    isError = lastName.isNotEmpty()&& lastName.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { phoneNumberFocusRequester.requestFocus() }
                    ),
                    focusRequester = lastNameFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))



                Text(
                    text = stringResource(R.string.phone_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = phoneNumber,
                    placeholderText = stringResource(R.string.phone_placeholder),
                    onTextChange = { phoneNumber = it },
                    keyboardType = KeyboardType.Number,
                    maxChars = 15,
                    errorMessage = if (phoneNumber.isNotEmpty() && phoneNumber.length <= 3) stringResource(
                        R.string.phone_validation_text
                    ) else null,
                    isError = phoneNumber.isNotEmpty()&& phoneNumber.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { addressFocusRequester.requestFocus() }
                    ),
                    focusRequester = phoneNumberFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))
//
//                Text(
//                    text = stringResource(R.string.address_label),
//                    style = TextStyle(
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Normal,
//                        color = Color.Black
//                    )
//                )


                // Observe value from map_picker result
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val selectedAddress = navBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("selected_address")

                LaunchedEffect(selectedAddress) {
                    if (!selectedAddress.isNullOrBlank()) {
                        address = selectedAddress
                        navBackStackEntry?.savedStateHandle?.remove<String>("selected_address")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.address_label),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            
                            navController.navigate("map_picker")
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.location_icon), // your drawable
                            contentDescription = "Edit",
                            tint = colorResource(R.color.button_normal),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Pick location",
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(R.color.button_normal)
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = address,
                    placeholderText = stringResource(R.string.address_placeholder),
                    onTextChange = { address = it },
                    keyboardType = KeyboardType.Text,
                    singleLine = false,
                    maxLines = 3,
                    maxChars = 200,
                    errorMessage = if (address.isNotEmpty() && address.length <= 3) stringResource(R.string.address_validation_text) else null,
                    isError = address.isNotEmpty() && address.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { dobFocusRequester.requestFocus() }
                    ),
                    focusRequester = addressFocusRequester
                )


                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.dob_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier.clickable { showDialog.value = true }
                ) {
                    CustomDobField(
                        textValue = dob,
                        placeholderText = stringResource(R.string.dob_placeholder),
                        onTextChange = { dob = it },
                        keyboardType = KeyboardType.Text,
                        maxChars = 40,
                        errorMessage = if (dob.isNotEmpty() && dob.length <= 3) stringResource(R.string.dob_validation_text) else null,
                        isError = dob.isNotEmpty() && dob.length <= 3,
                        onClearError = handleError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(onNext = { paypalFocusRequester.requestFocus() }),
                        focusRequester = dobFocusRequester,
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.paypal_email_label)
                )

                Spacer(Modifier.height(10.dp))


                CustomTextField(
                    textValue = paypalEmail,
                    placeholderText = stringResource(R.string.email_placeholder),
                    onTextChange = { paypalEmail = it },
                    keyboardType = KeyboardType.Email,
                    maxChars = 100,
                    errorMessage = if (!isEmailValid && paypalEmail.isNotEmpty()) stringResource(R.string.email_validation_text) else null,
                    isError = !isEmailValid && paypalEmail.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    ),
                    focusRequester = paypalFocusRequester
                )



                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = stringResource(R.string.save_button_label),
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
                        
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        },
    )
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
        String.format("%04d-%02d-%02d", year, month, day)
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

@Preview
@Composable
fun PreviewVoyagerAccountInfo() {
    UserAccountInfoScreen(navController = rememberNavController(),"")
}