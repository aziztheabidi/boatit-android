@file:Suppress("ktlint:standard:function-naming")

package com.boatit.boatsharing.features.voyager.dashboard.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.VoyagerFlowRoutes
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageRateCalcUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageRateCalcUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CreateVoyageRateCalcViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.ICreateVoyageRateCalcViewModel
import com.boatit.boatsharing.ui.components.CustomButton
import com.boatit.boatsharing.ui.components.CustomTextField
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.FormStepsViews
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateVoyageRateCalcScreen(
    navController: NavController,
    viewModel: ICreateVoyageRateCalcViewModel = koinViewModel<CreateVoyageRateCalcViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val firstNameFocusRequester = FocusRequester()
    val lastNameFocusRequester = FocusRequester()
    val isValidate = uiState.eventName.isNotEmpty()

    LaunchedEffect(Unit) {
        viewModel.onEvent(CreateVoyageRateCalcUiEvent.Initialize)
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEffects.collectLatest { effect ->
            when (effect) {
                is CreateVoyageRateCalcUiEffect.NavigateToSponsor -> {
                    navController.navigate(route = VoyagerFlowRoutes.createVoyageSponsor(effect.splitPayment))
                }
            }
        }
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

                Spacer(Modifier.height(30.dp))

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Kindly fill in the relevant details for the Voyage",
                        style =
                            TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                    )
                    Spacer(Modifier.width(5.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                ),
                            text = stringResource(R.string.selected_booking_date),
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                ),
                            text = uiState.bookingDate,
                        )
                    }
                }

                Spacer(Modifier.height(30.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        ),
                    text = "Name of the Event",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = uiState.eventName,
                    placeholderText = uiState.eventName,
                    onTextChange = {
                        viewModel.onEvent(CreateVoyageRateCalcUiEvent.EventNameChanged(it))
                    },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = {},
                    imeAction = ImeAction.Next,
                    keyboardActions =
                        KeyboardActions(
                            onNext = { lastNameFocusRequester.requestFocus() },
                        ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.event_calender),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified,
                        )
                    },
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        ),
                    text = "No. of Voyagers",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = uiState.voyagerCount,
                    placeholderText = uiState.voyagerCount,
                    onTextChange = { },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = {},
                    imeAction = ImeAction.Next,
                    isEditable = false,
                    keyboardActions =
                        KeyboardActions(
                            onNext = { lastNameFocusRequester.requestFocus() },
                        ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.passengers),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified,
                        )
                    },
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        ),
                    text = "Per Hour Rate",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = uiState.perHourRate,
                    placeholderText = uiState.perHourRate,
                    onTextChange = { },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = {},
                    imeAction = ImeAction.Next,
                    isEditable = false,
                    keyboardActions =
                        KeyboardActions(
                            onNext = { lastNameFocusRequester.requestFocus() },
                        ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.dollar),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified,
                        )
                    },
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        ),
                    text = "Estimated Cost",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = uiState.estimatedCost,
                    placeholderText = uiState.estimatedCost,
                    onTextChange = {},
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = {},
                    imeAction = ImeAction.Next,
                    isEditable = false,
                    keyboardActions =
                        KeyboardActions(
                            onNext = { lastNameFocusRequester.requestFocus() },
                        ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.dollar),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified,
                        )
                    },
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        ),
                    text = "Time to Spend on Water",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = uiState.eventHours,
                    placeholderText = uiState.eventHours,
                    onTextChange = {},
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    isEditable = false,
                    onClearError = {},
                    imeAction = ImeAction.Next,
                    keyboardActions =
                        KeyboardActions(
                            onNext = { lastNameFocusRequester.requestFocus() },
                        ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.clock),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified,
                        )
                    },
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        ),
                    text = "Pickup",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = uiState.pickup,
                    placeholderText = uiState.pickup,
                    onTextChange = {},
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    isEditable = false,
                    onClearError = {},
                    imeAction = ImeAction.Next,
                    keyboardActions =
                        KeyboardActions(
                            onNext = { lastNameFocusRequester.requestFocus() },
                        ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.location_icon),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified,
                        )
                    },
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        ),
                    text = "Drop off",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = uiState.dropOff,
                    placeholderText = uiState.dropOff,
                    onTextChange = { },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    isEditable = false,
                    onClearError = {},
                    imeAction = ImeAction.Next,
                    keyboardActions =
                        KeyboardActions(
                            onNext = { lastNameFocusRequester.requestFocus() },
                        ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.drop_off_loc_icon),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified,
                        )
                    },
                )

                Spacer(Modifier.height(15.dp))

                if (uiState.isTravelNow) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                            text = "Split Payment",
                        )

                        Switch(
                            checked = uiState.splitPaymentEnabled,
                            onCheckedChange = {
                                viewModel.onEvent(CreateVoyageRateCalcUiEvent.SplitPaymentToggled(it))
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
                }

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = "Proceed",
                    isValidate = isValidate,
                    isLoading = false,
                    onButtonClick = {
                        viewModel.onEvent(CreateVoyageRateCalcUiEvent.Proceed)
                    },
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
