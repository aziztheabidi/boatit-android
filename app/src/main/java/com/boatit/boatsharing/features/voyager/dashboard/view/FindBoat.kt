@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:discouraged-comment-location",
    "ktlint:standard:max-line-length",
    "ktlint:standard:curly-spacing",
)

package com.boatit.boatsharing.features.voyager.dashboard.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.Place
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageCategory
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FindBoatBusinessPrefill
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.IFindBoatViewModel
import com.boatit.boatsharing.ui.components.CustomDobField
import com.boatit.boatsharing.ui.components.CustomTextField
import com.boatit.boatsharing.ui.components.SessionDialog
import com.boatit.boatsharing.ui.components.getDate
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun FindBoat(
    navController: NavController,
    modifier: Modifier,
    pickupLocation: String,
    dropOffLocation: String,
    totalPassengers: String,
    voyagerUserId: String,
    pickupDockId: Int? = null,
    dropOffDockId: Int? = null,
    categoryOptions: List<VoyageCategory> = emptyList(),
    dockOptions: List<Place> = emptyList(),
    businessPrefill: FindBoatBusinessPrefill = FindBoatBusinessPrefill(),
    onBusinessPrefillConsumed: () -> Unit = {},
    viewModel: IFindBoatViewModel = koinViewModel<FindBoatViewModel>(),
    onCancelClick: () -> Unit,
    onFindBoatClick: () -> Unit,
) {
    val contractState by viewModel.uiState.collectAsState()
    val currentDate = getDate()

    val handleError = {}

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                FindBoatUiEffect.NavigateCreateVoyage -> onFindBoatClick()
                FindBoatUiEffect.NavigateDashboardAfterFindBoat -> Unit
                is FindBoatUiEffect.ShowFindBoatError -> Unit
            }
        }
    }

    LaunchedEffect(pickupLocation, dropOffLocation, totalPassengers, currentDate) {
        viewModel.onEvent(
            FindBoatUiEvent.Initialize(
                voyagerUserId = voyagerUserId,
                pickupLocation = pickupLocation,
                pickupDockId = pickupDockId,
                dropOffLocation = dropOffLocation,
                dropOffDockId = dropOffDockId,
                passengerCount = totalPassengers,
                bookingDate = currentDate,
            ),
        )
    }

    LaunchedEffect(categoryOptions, dockOptions) {
        viewModel.onEvent(FindBoatUiEvent.SetCategoryOptions(categoryOptions))
        viewModel.onEvent(FindBoatUiEvent.SetDockOptions(dockOptions))
    }

    LaunchedEffect(businessPrefill) {
        if (businessPrefill.pending && businessPrefill.dockId != null) {
            if (businessPrefill.target == "Pick") {
                viewModel.onEvent(
                    FindBoatUiEvent.SetPickupLocation(
                        name = businessPrefill.dockName,
                        dockTypeId = businessPrefill.dockId,
                    ),
                )
            } else if (businessPrefill.target == "Drop") {
                viewModel.onEvent(
                    FindBoatUiEvent.SetDropOffLocation(
                        name = businessPrefill.dockName,
                        dockTypeId = businessPrefill.dockId,
                    ),
                )
            }
            onBusinessPrefillConsumed()
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 40.dp),
            shape =
                RoundedCornerShape(
                    topStart = 45.dp,
                    topEnd = 45.dp,
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Spacer(Modifier.height(30.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Please confirm your details before booking",
                    style =
                        TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                        ),
                    modifier =
                        Modifier
                            .weight(1f),
                )
//
//                Card(
//                    modifier = Modifier
//                        .width(90.dp)
//                        .height(60.dp)
//                        .padding(3.dp),
//                    shape = RoundedCornerShape(8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//                    colors = CardDefaults.cardColors(containerColor = Color.White)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(0.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//
//                        if (showDialog.value) {
//                            MyDatePickerDialog(
//                                onDateSelected = { bookingDate = it },
//                                onDismiss = { showDialog.value = false }
//                            )
//                        }
//                        Icon(
//                            painter = painterResource(id = R.drawable.event_calender),
//                            contentDescription = "Icon",
//                            modifier = Modifier
//                                .size(30.dp)
//                                .clickable {
//                                    showDialog.value = true
//                                },
//                            tint = colorResource(R.color.button_normal)
//                        )
//
//                        Text(
//                            text = "Create Event",
//                            style = TextStyle(
//                                fontSize = 10.sp,
//                                fontWeight = FontWeight.Normal,
//                                color = Color.Black
//                            ),
//                            modifier = Modifier.padding(top = 4.dp)
//                        )
//                    }
//                }
            }

            Column(
                modifier =
                    Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 25.dp,
                        )
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
            ) {
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
                        text = stringResource(R.string.booking_date),
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        style =
                            TextStyle(
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                        text = contractState.bookingDate,
                    )
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                    text = stringResource(R.string.categoy),
                )
                Spacer(Modifier.height(10.dp))

                Box(modifier = Modifier.clickable { viewModel.onEvent(FindBoatUiEvent.ToggleCategoryDropdown(true)) }) {
                    CustomDobField(
                        textValue = contractState.category,
                        placeholderText = stringResource(R.string.categoy),
                        onTextChange = { viewModel.onEvent(FindBoatUiEvent.SetCategory(it, contractState.categoryId)) },
                        keyboardType = KeyboardType.Text,
                        maxChars = 100,
                        errorMessage = null,
                        isError = false,
                        onClearError = handleError,
                        imeAction = ImeAction.Next,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.boat_icon),
                                contentDescription = "Icon",
                                modifier = Modifier.size(20.dp),
                                tint = colorResource(R.color.button_normal),
                            )
                        },
                    )

                    DropdownMenu(
                        expanded = contractState.isCategoryDropdownExpanded,
                        onDismissRequest = { viewModel.onEvent(FindBoatUiEvent.ToggleCategoryDropdown(false)) },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        contractState.categoryOptions.forEach { categories ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.onEvent(FindBoatUiEvent.SetCategory(categories.Name, categories.Id))
                                },
                                text = {
                                    Text(
                                        text = categories.Name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 4.dp),
                                    )
                                },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color.White,
                                        ),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                    text = stringResource(R.string.pickup_location_lbl),
                )

                Spacer(Modifier.height(10.dp))

                Box(modifier = Modifier.clickable { viewModel.onEvent(FindBoatUiEvent.TogglePickupDropdown(true)) }) {
                    CustomDobField(
                        textValue = contractState.pickupLocation,
                        placeholderText = stringResource(R.string.pickup_location_lbl),
                        onTextChange = { viewModel.onEvent(FindBoatUiEvent.SetPickupLocation(it, contractState.pickupDockId)) },
                        keyboardType = KeyboardType.Text,
                        maxChars = 100,
                        errorMessage = null,
                        isError = false,
                        onClearError = handleError,
                        imeAction = ImeAction.Next,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.location_icon),
                                contentDescription = "Icon",
                                modifier = Modifier.size(20.dp),
                                tint = colorResource(R.color.button_normal),
                            )
                        },
                    )

                    DropdownMenu(
                        expanded = contractState.isPickupDropdownExpanded,
                        onDismissRequest = { viewModel.onEvent(FindBoatUiEvent.TogglePickupDropdown(false)) },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        contractState.dockOptions.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.onEvent(
                                        FindBoatUiEvent.SetPickupLocation(category.Name, category.DockTypeId),
                                    )
                                },
                                text = {
                                    Text(
                                        text = category.Name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 4.dp),
                                    )
                                },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color.White,
                                        ),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                    text = stringResource(R.string.drop_off_location_lbl),
                )

                Spacer(Modifier.height(10.dp))

                Box(modifier = Modifier.clickable { viewModel.onEvent(FindBoatUiEvent.ToggleDropOffDropdown(true)) }) {
                    CustomDobField(
                        textValue = contractState.dropOffLocation,
                        placeholderText = stringResource(R.string.drop_off_location_lbl),
                        onTextChange = { viewModel.onEvent(FindBoatUiEvent.SetDropOffLocation(it, contractState.dropOffDockId)) },
                        keyboardType = KeyboardType.Text,
                        maxChars = 100,
                        errorMessage = null,
                        isError = false,
                        onClearError = handleError,
                        imeAction = ImeAction.Next,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.drop_off_loc_icon),
                                contentDescription = "Icon",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Unspecified,
                            )
                        },
                    )
                    DropdownMenu(
                        expanded = contractState.isDropOffDropdownExpanded,
                        onDismissRequest = { viewModel.onEvent(FindBoatUiEvent.ToggleDropOffDropdown(false)) },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 26.dp, vertical = 5.dp),
                    ) {
                        contractState.dockOptions.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.onEvent(
                                        FindBoatUiEvent.SetDropOffLocation(category.Name, category.DockTypeId),
                                    )
                                },
                                text = {
                                    Text(
                                        text = category.Name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 4.dp),
                                    )
                                },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color.White,
                                        ),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                    text = stringResource(R.string.num_off_voyagers_lbl),
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = contractState.passengerCount,
                    placeholderText = stringResource(R.string.num_off_voyagers_lbl),
                    onTextChange = { input ->
                        viewModel.onEvent(FindBoatUiEvent.SetPassengerCount(input))
                    },
                    keyboardType = KeyboardType.Number,
                    errorMessage =
                        if (contractState.passengerCount.isNotEmpty() && contractState.passengerCount.length <= 1) {
                            stringResource(
                                R.string.num_off_voyagers_text,
                            )
                        } else {
                            null
                        },
                    isError = contractState.passengerCount.isNotEmpty() && contractState.passengerCount.length <= 1,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.passengers),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = colorResource(R.color.button_normal),
                        )
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                Spacer(Modifier.height(15.dp))

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp), // Adds spacing between buttons
                ) {
                    Button(
                        onClick = {
                            viewModel.onEvent(FindBoatUiEvent.Submit)
                        },
                        enabled = contractState.category.isNotEmpty() && contractState.passengerCount.isNotEmpty() && contractState.dropOffLocation.isNotEmpty() && contractState.pickupLocation.isNotEmpty(),
                        shape = RoundedCornerShape(10.dp),
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 1.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal)),
                    ) {
                        Text(
                            text = stringResource(R.string.find_boat_button_text),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }

                    Button(
                        onClick = { onCancelClick() },
                        shape = RoundedCornerShape(10.dp), // Corner radius
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(50.dp)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.button_normal), // Border color
                                    shape = RoundedCornerShape(10.dp), // Apply same corner radius to border
                                ),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = R.color.button_normal), // Text color matches border
                        )
                    }
                }
            }

            if (contractState.showPassengerLimitDialog)
                {
                    SessionDialog(
                        text = "This selected category requires a different number of passengers. Please review the limits.",
                        onCancel = {},
                        onPressOk = {
                            viewModel.onEvent(FindBoatUiEvent.DismissPassengerDialog)
                        },
                        showCancelButton = false,
                    )
                }
        }

        Image(
            painter = painterResource(id = R.drawable.wheel_icon),
            contentDescription = "Floating Icon",
            contentScale = ContentScale.FillBounds,
            modifier =
                Modifier
                    .size(90.dp)
                    .clickable { navController.navigate(NavigationManager.MENU_OPTIONS_SCREEN) },
        )
    }
}

@Preview
@Composable
fun PreviewFindBoat() {
    FindBoat(
        navController = rememberNavController(),
        modifier = Modifier,
        pickupLocation = "",
        dropOffLocation = "",
        totalPassengers = "",
        voyagerUserId = "",
        onCancelClick = {},
        onFindBoatClick = {},
    )
}
