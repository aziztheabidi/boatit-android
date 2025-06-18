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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
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
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.CalculateFairViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDobField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel
import android.text.format.DateFormat
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateVoyageScreen(
    navController: NavController,
    viewModel: CalculateFairViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val registrationState by viewModel.registrationState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val dobFocusRequester = remember { FocusRequester() }
    val paypalFocusRequester = remember { FocusRequester() }

    LaunchedEffect(registrationState) {
        if (registrationState is NetworkResponse.Success && uiState.isLoading) {
            navController.navigate(NavigationManager.CREATE_VOYAGE_RATE_CALC_SCREEN)
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = "Create Voyage", onImageClick = { println("clicked...") })
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

                if (uiState.showDatePicker) {
                    val datePickerState = rememberDatePickerState()
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    val selectedDate = selectedDateMillis?.let {
                        val calendar = Calendar.getInstance().apply { timeInMillis = it }
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH) + 1
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        String.format("%04d-%02d-%02d", year, month, day)
                    } ?: ""

                    DatePickerDialog(
                        onDismissRequest = { viewModel.onShowDatePicker(false) },
                        confirmButton = {
                            Button(onClick = {
                                viewModel.onDobChange(selectedDate)
                                viewModel.onShowDatePicker(false)
                            }) { Text("OK") }
                        },
                        dismissButton = {
                            Button(onClick = { viewModel.onShowDatePicker(false) }) { Text("Cancel") }
                        }
                    ) {
                        DatePicker(state = datePickerState, showModeToggle = false)
                    }
                }

                if (uiState.showStartTimePicker) {
                    MyTimePickerDialog(
                        onDateSelected = {
                            viewModel.onStartTimeChange(it + ":00")
                            viewModel.onShowStartTimePicker(false)
                        },
                        onDismiss = { viewModel.onShowStartTimePicker(false) }
                    )
                }

                if (uiState.showEndTimePicker) {
                    MyTimePickerDialog(
                        onDateSelected = {
                            viewModel.onEndTimeChange(it + ":00")
                            viewModel.onShowEndTimePicker(false)
                        },
                        onDismiss = { viewModel.onShowEndTimePicker(false) }
                    )
                }

                Spacer(Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        checked = uiState.travelNowSwitchState,
                        onCheckedChange = { viewModel.onTravelNowSwitchChange(it) },
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
                    modifier = Modifier.clickable {
                        if (!uiState.travelNowSwitchState) viewModel.onShowDatePicker(true)
                    }
                ) {
                    CustomDobField(
                        textValue = uiState.dob,
                        placeholderText = stringResource(R.string.dob_placeholder),
                        onTextChange = viewModel::onDobChange,
                        keyboardType = KeyboardType.Text,
                        maxChars = 40,
                        errorMessage = if (uiState.dob.isNotEmpty() && uiState.dob.length <= 3) stringResource(R.string.dob_validation_text) else null,
                        isError = uiState.dob.isNotEmpty() && uiState.dob.length <= 3,
                        onClearError = viewModel::clearError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(onNext = { paypalFocusRequester.requestFocus() }),
                        focusRequester = dobFocusRequester,
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = Modifier.clickable {
                        if (!uiState.travelNowSwitchState) viewModel.onShowStartTimePicker(true)
                    }
                ) {
                    CustomDobField(
                        textValue = uiState.startTime,
                        placeholderText = "Start Time",
                        onTextChange = viewModel::onStartTimeChange,
                        keyboardType = KeyboardType.Text,
                        maxChars = 40,
                        errorMessage = if (uiState.startTime.isNotEmpty() && uiState.startTime.length <= 3) stringResource(R.string.dob_validation_text) else null,
                        isError = uiState.startTime.isNotEmpty() && uiState.startTime.length <= 3,
                        onClearError = viewModel::clearError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(onNext = { paypalFocusRequester.requestFocus() }),
                        focusRequester = dobFocusRequester,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        text = "Spend Time on the voyage"
                    )
                    Switch(
                        checked = uiState.spendTimeSwitchState,
                        onCheckedChange = viewModel::onSpendTimeSwitchChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = colorResource(id = R.color.button_normal),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFD9D9D9)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                if (uiState.spendTimeSwitchState) {
                    Box(
                        modifier = Modifier.clickable {
                            viewModel.onShowEndTimePicker(true)
                        }
                    ) {
                        CustomDobField(
                            textValue = uiState.endTime,
                            placeholderText = "End Time",
                            onTextChange = viewModel::onEndTimeChange,
                            keyboardType = KeyboardType.Text,
                            maxChars = 40,
                            errorMessage = if (uiState.endTime.isNotEmpty() && uiState.endTime.length <= 3) stringResource(R.string.dob_validation_text) else null,
                            isError = uiState.endTime.isNotEmpty() && uiState.endTime.length <= 3,
                            onClearError = viewModel::clearError,
                            imeAction = ImeAction.Next,
                            keyboardActions = KeyboardActions(onNext = { paypalFocusRequester.requestFocus() }),
                            focusRequester = paypalFocusRequester,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.calculateFare(fromDockId = "someFromId", toDockId = "someToId")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.button_normal)
                    ),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(text = "Calculate Fare")
                    }
                }

                uiState.errorMessage?.let { error ->
                    Text(text = error, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
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
            timePickerState.minute
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
        },

        colors = DatePickerDefaults.colors(
            containerColor = Color.White

        )
    )
    {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            TimeInput(
                state = timePickerState
            )
        }
    }


}

@Preview
@Composable
fun CreateVoyage() {
    CreateVoyageScreen(navController = rememberNavController())
}
fun convertMillisToDate(millis: Long): String {
    val formatter = java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(millis))
}