@file:Suppress("ktlint:standard:function-naming")

package com.boatit.boatsharing.features.voyager.dashboard.view

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CalculateFairViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.ICreateVoyageViewModel
import com.boatit.boatsharing.ui.components.CustomDobField
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.FormStepsViews
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateVoyageScreen(
    navController: NavController,
    viewModel: ICreateVoyageViewModel = koinViewModel<CalculateFairViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val dobFocusRequester = remember { FocusRequester() }
    val paypalFocusRequester = remember { FocusRequester() }

    LaunchedEffect(viewModel) {
        viewModel.uiEffects.collectLatest { effect ->
            when (effect) {
                CreateVoyageUiEffect.NavigateToRateCalculation -> {
                    navController.navigate(NavigationManager.CREATE_VOYAGE_RATE_CALC_SCREEN)
                    viewModel.onEvent(CreateVoyageUiEvent.ResetRequestState)
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = "Create Voyage", onImageClick = { navController.popBackStack() })
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(
                            top = innerPadding.calculateTopPadding() + 15.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = innerPadding.calculateTopPadding() + 25.dp,
                        )
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
            ) {
                FormStepsViews(
                    numberOfViews = 1,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 1,
                )

                if (uiState.showDatePicker) {
                    val datePickerState = rememberDatePickerState()
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    val selectedDate =
                        selectedDateMillis?.let {
                            val calendar = Calendar.getInstance().apply { timeInMillis = it }
                            val year = calendar.get(Calendar.YEAR)
                            val month = calendar.get(Calendar.MONTH) + 1
                            val day = calendar.get(Calendar.DAY_OF_MONTH)
                            String.format("%04d-%02d-%02d", year, month, day)
                        } ?: ""

                    DatePickerDialog(
                        onDismissRequest = { viewModel.onEvent(CreateVoyageUiEvent.ShowDatePicker(false)) },
                        confirmButton = {
                            Button(onClick = {
                                viewModel.onEvent(CreateVoyageUiEvent.DobChanged(selectedDate))
                                viewModel.onEvent(CreateVoyageUiEvent.ShowDatePicker(false))
                            }) { Text("OK") }
                        },
                        dismissButton = {
                            Button(onClick = { viewModel.onEvent(CreateVoyageUiEvent.ShowDatePicker(false)) }) { Text("Cancel") }
                        },
                    ) {
                        DatePicker(state = datePickerState, showModeToggle = false)
                    }
                }

                if (uiState.showStartTimePicker) {
                    MyTimePickerDialog(
                        onDateSelected = {
                            viewModel.onEvent(CreateVoyageUiEvent.StartTimeChanged(it + ":00"))
                            viewModel.onEvent(CreateVoyageUiEvent.ShowStartTimePicker(false))
                        },
                        onDismiss = { viewModel.onEvent(CreateVoyageUiEvent.ShowStartTimePicker(false)) },
                    )
                }

                if (uiState.showEndTimePicker) {
                    MyTimePickerDialog(
                        onDateSelected = {
                            viewModel.onEvent(CreateVoyageUiEvent.EndTimeChanged(it + ":00"))
                            viewModel.onEvent(CreateVoyageUiEvent.ShowEndTimePicker(false))
                        },
                        onDismiss = { viewModel.onEvent(CreateVoyageUiEvent.ShowEndTimePicker(false)) },
                    )
                }

                Spacer(Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        style =
                            TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                            ),
                        text = "Want to travel now?",
                    )
                    Switch(
                        checked = uiState.travelNowSwitchState,
                        onCheckedChange = {
                            viewModel.onEvent(CreateVoyageUiEvent.TravelNowToggled(it))
                        },
                        colors =
                            SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = colorResource(id = R.color.button_normal),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFD9D9D9),
                                uncheckedBorderColor = Color.Transparent,
                            ),
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                        ),
                    text = "Want to book a voyage?",
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier =
                        Modifier.clickable {
                            if (!uiState.travelNowSwitchState) viewModel.onEvent(CreateVoyageUiEvent.ShowDatePicker(true))
                        },
                ) {
                    CustomDobField(
                        textValue = uiState.dob,
                        placeholderText = stringResource(R.string.dob_placeholder),
                        onTextChange = { viewModel.onEvent(CreateVoyageUiEvent.DobChanged(it)) },
                        keyboardType = KeyboardType.Text,
                        maxChars = 40,
                        errorMessage =
                            if (uiState.dob.isNotEmpty() && uiState.dob.length <= 3) {
                                stringResource(
                                    R.string.dob_validation_text,
                                )
                            } else {
                                null
                            },
                        isError = uiState.dob.isNotEmpty() && uiState.dob.length <= 3,
                        onClearError = { viewModel.onEvent(CreateVoyageUiEvent.ClearError) },
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(onNext = { paypalFocusRequester.requestFocus() }),
                        focusRequester = dobFocusRequester,
                        showBorder = false,
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier =
                        Modifier.clickable {
                            if (!uiState.travelNowSwitchState) viewModel.onEvent(CreateVoyageUiEvent.ShowStartTimePicker(true))
                        },
                ) {
                    CustomDobField(
                        textValue = uiState.startTime,
                        placeholderText = "Start Time",
                        onTextChange = { viewModel.onEvent(CreateVoyageUiEvent.StartTimeChanged(it)) },
                        keyboardType = KeyboardType.Text,
                        maxChars = 40,
                        errorMessage =
                            if (uiState.startTime.isNotEmpty() && uiState.startTime.length <= 3) {
                                stringResource(
                                    R.string.dob_validation_text,
                                )
                            } else {
                                null
                            },
                        isError = uiState.startTime.isNotEmpty() && uiState.startTime.length <= 3,
                        onClearError = { viewModel.onEvent(CreateVoyageUiEvent.ClearError) },
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(onNext = { paypalFocusRequester.requestFocus() }),
                        focusRequester = dobFocusRequester,
                        showBorder = false,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        style =
                            TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                            ),
                        text = "Want to Spend time on water?",
                    )
                    Switch(
                        checked = uiState.spendTimeSwitchState,
                        onCheckedChange = { viewModel.onEvent(CreateVoyageUiEvent.SpendTimeToggled(it)) },
                        colors =
                            SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = colorResource(id = R.color.button_normal),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFD9D9D9),
                                uncheckedBorderColor = Color.Transparent,
                            ),
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                        ),
                    text = "If you want to stay on water, please select duration by entering an end time.",
                )
                Spacer(modifier = Modifier.height(15.dp))

                if (uiState.spendTimeSwitchState) {
                    Box(
                        modifier =
                            Modifier.clickable {
                                viewModel.onEvent(CreateVoyageUiEvent.ShowEndTimePicker(true))
                            },
                    ) {
                        CustomDobField(
                            textValue = uiState.endTime,
                            placeholderText = "End Time",
                            onTextChange = { viewModel.onEvent(CreateVoyageUiEvent.EndTimeChanged(it)) },
                            keyboardType = KeyboardType.Text,
                            maxChars = 40,
                            errorMessage =
                                if (uiState.endTime.isNotEmpty() && uiState.endTime.length <= 3) {
                                    stringResource(
                                        R.string.dob_validation_text,
                                    )
                                } else {
                                    null
                                },
                            isError = uiState.endTime.isNotEmpty() && uiState.endTime.length <= 3,
                            onClearError = { viewModel.onEvent(CreateVoyageUiEvent.ClearError) },
                            imeAction = ImeAction.Next,
                            keyboardActions = KeyboardActions(onNext = { paypalFocusRequester.requestFocus() }),
                            focusRequester = paypalFocusRequester,
                            showBorder = false,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.onEvent(CreateVoyageUiEvent.CalculateFare)
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.button_normal),
                        ),
                    enabled = !uiState.isLoading,
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
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState =
        rememberTimePickerState(
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
            Button(
                onClick = {
                    onDateSelected(formattedTime)
                    onDismiss()
                },
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
        colors =
            DatePickerDefaults.colors(
                containerColor = Color.White,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            TimePicker(
                state = timePickerState,
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
