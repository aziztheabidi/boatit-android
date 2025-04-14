package com.boatit.boatsharing.ui.voyager.dashbaord.view

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.CalculateFairViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDobField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar


@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateVoyageScreen(navController: NavController,
   viewModel: CalculateFairViewModel = koinViewModel()) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val dobFocusRequester = remember { FocusRequester() }
    val paypalFocusRequester = remember { FocusRequester() }


    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var paypalEmail by remember { mutableStateOf("") }
    val showDialog = mutableStateOf(false)
    val showTimer = mutableStateOf(false)
    val showEndTimer = mutableStateOf(false)
    var bookingDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    val isEmailValid = paypalEmail.contains("@") && paypalEmail.contains(".")
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var getingData by remember { mutableStateOf(true) }
    var isNetworkError by remember { mutableStateOf(false) }

    var travelNowSwitchState by remember { mutableStateOf(false) }
    var spendTimeSwitchState by remember { mutableStateOf(false) }


    val isValidate =
            dob.isNotEmpty()
            && startTime.isNotEmpty()

    val handleError = {
        errorMessage = null
        isError = false
    }

    val registrationState by viewModel.registrationState.collectAsState()

    fun performLogin(){
        AppConstants.Event_Time = endTime
        navController.navigate(NavigationManager.CREATE_VOYAGE_RATE_CALC_SCREEN)
    }

    when (registrationState) {
        is NetworkResponse.Success -> {
            if(isLoading){
                isLoading = false
                isNetworkError = false
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

                if (showTimer.value) {
                    MyTimePickerDialog(
                        onDateSelected = {
                            startTime = it },
                        onDismiss = { showTimer.value = false }
                    )
                }

                if (showEndTimer.value) {
                    MyTimePickerDialog(
                        onDateSelected = {
                            endTime = it },
                        onDismiss = { showEndTimer.value = false }
                    )
                }

                Spacer(Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        text = "Want to travel now?"
                    )

                    Switch(
                        checked = travelNowSwitchState,
                        onCheckedChange = {
                            travelNowSwitchState = it
                            AppConstants.Travel_Now = travelNowSwitchState},
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = colorResource(id = R.color.button_normal),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFD9D9D9)
                        )
                    )
                }





                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    text = "Want to book a voyage?"
                )

                Spacer(modifier = Modifier.height(20.dp))


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

                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = Modifier.clickable { showTimer.value = true }
                ) {
                    CustomDobField(
                        textValue = startTime,
                        placeholderText = "Start Time",
                        onTextChange = { startTime = it },
                        keyboardType = KeyboardType.Text,
                        maxChars = 40,
                        errorMessage = if (startTime.isNotEmpty() && startTime.length <= 3) stringResource(R.string.dob_validation_text) else null,
                        isError = startTime.isNotEmpty() && startTime.length <= 3,
                        onClearError = handleError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(onNext = { paypalFocusRequester.requestFocus() }),
                        focusRequester = dobFocusRequester,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        text = "Want to spend time on water?"
                    )
                    Switch(
                        checked = spendTimeSwitchState,
                        onCheckedChange = { spendTimeSwitchState = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = colorResource(id = R.color.button_normal),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFD9D9D9)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                if(spendTimeSwitchState){
                    Text(
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        text = "If you want to stay on water, please select duration by entering an end time"
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier.clickable { showEndTimer.value = true }
                    ) {
                        CustomDobField(
                            textValue = endTime,
                            placeholderText = "End Time",
                            onTextChange = { endTime = it },
                            keyboardType = KeyboardType.Text,
                            maxChars = 40,
                            errorMessage = null,
                            isError = false,
                            onClearError = handleError,
                            imeAction = ImeAction.Next,
                            keyboardActions = KeyboardActions(onNext = { paypalFocusRequester.requestFocus() }),
                            focusRequester = dobFocusRequester,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = "Proceed",
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {
                        if (!spendTimeSwitchState){
                            endTime = "0"
                        }
                        else{
                            endTime = "5"
                        }
                        viewModel.CalculateFairFunc("1", "2", endTime)
                        isButtonEnabled = true
                        isLoading = true
                        focusManager.clearFocus()
                        println("perform network call")
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePickerDialog(onDateSelected: (String) -> Unit, onDismiss: () -> Unit
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    val formattedTime =
        "%02d:%02d".format(
            timePickerState.hour,
            timePickerState.minute,
        )

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(formattedTime)
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
        TimePicker(
            state = timePickerState
        )
    }
}

@Preview
@Composable
fun CreateVoyage() {
    CreateVoyageScreen(navController = rememberNavController())
}
