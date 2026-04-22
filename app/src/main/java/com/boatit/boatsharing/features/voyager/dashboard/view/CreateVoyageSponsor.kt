@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:curly-spacing",
    "ktlint:standard:no-line-break-after-else",
)

package com.boatit.boatsharing.features.voyager.dashboard.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.navigation.navigateToVoyagerDashboard
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageSponsorUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.BookVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CreateVoyageSponsorViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.IBookVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.ICreateVoyageSponsorViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.IFindBoatViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.VoyageSessionStore
import com.boatit.boatsharing.ui.components.CustomButton
import com.boatit.boatsharing.ui.components.CustomTextField
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.FormStepsViews
import com.boatit.boatsharing.ui.components.SessionDialog
import com.boatit.boatsharing.ui.components.VoyageBookDialog
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateVoyageSponsorScreen(
    navController: NavController,
    splitPayment: Boolean,
    viewModelFind: IFindBoatViewModel = koinViewModel<FindBoatViewModel>(),
    viewModel: IBookVoyageViewModel = koinViewModel<BookVoyageViewModel>(),
    sponsorViewModel: ICreateVoyageSponsorViewModel = koinViewModel<CreateVoyageSponsorViewModel>(),
    voyageSessionStore: VoyageSessionStore = get(VoyageSessionStore::class.java),
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }

    val bookUiState by viewModel.uiState.collectAsState()
    val findUiState by viewModelFind.uiState.collectAsState()
    val sponsorUiState by sponsorViewModel.uiState.collectAsState()

    val isValidate = true

    LaunchedEffect(Unit) {
        sponsorViewModel.onEvent(CreateVoyageSponsorUiEvent.Initialize)
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEffects.collectLatest { effect ->
            when (effect) {
                is BookVoyageUiEffect.BookedSuccess -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    voyageSessionStore.setVoyageId(effect.voyageId.orEmpty())
                    navController.navigate(NavigationManager.VOYAGE_BOOKED_SCREEN)
                    viewModel.onEvent(BookVoyageUiEvent.ResetRequestState)
                }
                is BookVoyageUiEffect.BookedError -> Unit
            }
        }
    }

    LaunchedEffect(viewModelFind) {
        viewModelFind.uiEffects.collectLatest { effect ->
            when (effect) {
                FindBoatUiEffect.NavigateCreateVoyage -> Unit
                FindBoatUiEffect.NavigateDashboardAfterFindBoat -> {
                    viewModelFind.onEvent(FindBoatUiEvent.ResetRequestState)
                    Toast.makeText(context, "Finding the Boat", Toast.LENGTH_SHORT).show()
                    navController.navigateToVoyagerDashboard("True")
                }
                is FindBoatUiEffect.ShowFindBoatError -> Unit
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

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        ),
                    text = "Total Fair",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = sponsorUiState.totalFare,
                    placeholderText = sponsorUiState.totalFare,
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
                if (sponsorUiState.splitPaymentEnabled)
                    {
                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500,
                                ),
                            text = "Add Sponsors",
                        )

                        Spacer(Modifier.height(10.dp))

                        Card(
                            modifier =
                                Modifier
                                    .width(70.dp)
                                    .height(70.dp)
                                    .padding(3.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = White),
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_sponsor),
                                    contentDescription = "Icon",
                                    modifier =
                                        Modifier
                                            .size(30.dp)
                                            .clickable {
                                                navController.navigate(NavigationManager.SPONSOR_SCREEN)
                                            },
                                    tint = colorResource(R.color.button_normal),
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500,
                                ),
                            text = "Number of Sponsors",
                        )

                        Spacer(Modifier.height(5.dp))

                        CustomTextField(
                            textValue = sponsorUiState.sponsorCount,
                            placeholderText = sponsorUiState.sponsorCount,
                            onTextChange = {},
                            keyboardType = KeyboardType.Text,
                            maxChars = 100,
                            errorMessage = null,
                            isError = false,
                            onClearError = {},
                            isEditable = false,
                            imeAction = ImeAction.Next,
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
                    }

                Spacer(Modifier.height(15.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        ),
                    text = "Individuals",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = sponsorUiState.individualFare,
                    placeholderText = sponsorUiState.individualFare,
                    onTextChange = { },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = { },
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
                    text = "Pickup",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = sponsorUiState.pickup,
                    placeholderText = sponsorUiState.pickup,
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
                    text = "Dropoff",
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = sponsorUiState.dropOff,
                    placeholderText = sponsorUiState.dropOff,
                    onTextChange = { },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = {},
                    isEditable = false,
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

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = sponsorUiState.actionText,
                    isValidate = isValidate,
                    isLoading = bookUiState.isSubmitting || findUiState.isSubmitting,
                    onButtonClick = {
                        if (sponsorUiState.splitPaymentEnabled) {
                            viewModel.onEvent(
                                BookVoyageUiEvent.SubmitBookVoyage(
                                    BookVoyageRequest(
                                        VoyagerUserId = sponsorUiState.voyagerUserId,
                                        Name = sponsorUiState.eventName,
                                        VoyageCategoryId = sponsorUiState.voyageCategoryId,
                                        PickupDockId = sponsorUiState.pickupDockId,
                                        DropOffDockId = sponsorUiState.dropOffDockId,
                                        NoOfVoyagers = sponsorUiState.noOfVoyagers,
                                        IsImmediately = sponsorUiState.isImmediately,
                                        IsSplitPayment = true,
                                        BookingDate = sponsorUiState.bookingDate,
                                        StartTime = sponsorUiState.startTime,
                                        IsStayOnWater = sponsorUiState.isStayOnWater,
                                        EndTime = sponsorUiState.endTime,
                                        PerHourRate = sponsorUiState.perHourRate,
                                        DurationInHours = sponsorUiState.durationInHours,
                                        NoOfSponsers = sponsorUiState.sponsorEntries.size,
                                        EstimatedCost = sponsorUiState.totalCostAmount,
                                        IndvidualAmount = sponsorUiState.individualFare.toDoubleOrNull() ?: 0.0,
                                        Sponsers = sponsorUiState.sponsorEntries,
                                    ),
                                ),
                            )
                        } else
                            {
                                viewModelFind.onEvent(
                                    FindBoatUiEvent.SubmitFindBoatRequest(
                                        FindBoatRequest(
                                            VoyagerUserId = sponsorUiState.voyagerUserId,
                                            Name = sponsorUiState.eventName,
                                            VoyageCategoryId = sponsorUiState.voyageCategoryId,
                                            PickupDockId = sponsorUiState.pickupDockId,
                                            DropOffDockId = sponsorUiState.dropOffDockId,
                                            NoOfVoyagers = sponsorUiState.noOfVoyagers,
                                            EstimatedCost = sponsorUiState.totalCostAmount,
                                            IsImmediately = true,
                                            IsSplitPayment = false,
                                            BookingDate = sponsorUiState.bookingDate,
                                        ),
                                    ),
                                )
                            }
                        focusManager.clearFocus()
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (bookUiState.showErrorDialog) {
                    VoyageBookDialog(
                        name = bookUiState.errorMessage,
                        onPayNow = {
                            viewModel.onEvent(BookVoyageUiEvent.DismissErrorDialog)
                        },
                        onDismissRequest = { },
                    )
                }

                if (findUiState.showSponsorErrorDialog) {
                    SessionDialog(
                        text = findUiState.sponsorErrorMessage,
                        onCancel = {},
                        onPressOk = {
                            viewModelFind.onEvent(FindBoatUiEvent.DismissSponsorErrorDialog)
                        },
                        showCancelButton = false,
                    )
                }
            }
        },
    )
}
