@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:discouraged-comment-location",
    "ktlint:standard:curly-spacing",
    "ktlint:standard:no-line-break-after-else",
    "ktlint:standard:if-else-wrapping",
)

package com.boatit.boatsharing.features.voyager.dashboard.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.popBack
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.features.signup.general.viewmodel.GetVoyagerProfileUiEffect
import com.boatit.boatsharing.features.signup.general.viewmodel.GetVoyagerProfileUiEvent
import com.boatit.boatsharing.features.signup.general.viewmodel.GetVoyagerProfileViewModel
import com.boatit.boatsharing.features.signup.general.viewmodel.VoyagerProfileUiEffect
import com.boatit.boatsharing.features.signup.general.viewmodel.VoyagerProfileUiEvent
import com.boatit.boatsharing.features.signup.general.viewmodel.VoyagerProfileViewModel
import com.boatit.boatsharing.ui.components.CustomButton
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.FormStepsViews
import com.boatit.boatsharing.ui.components.MyDatePickerDialog
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@SuppressLint("UnrememberedMutableState")
@Composable
fun ConfirmVoyageScreen(
    navController: NavController,
    viewModel: VoyagerProfileViewModel = koinViewModel(),
    viewModelfeth: GetVoyagerProfileViewModel = koinViewModel(),
    userSessionStore: UserSessionStore = get(UserSessionStore::class.java),
) {
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

    val isValidate =
        firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            phoneNumber.isNotEmpty() &&
            address.isNotEmpty() &&
            dob.isNotEmpty() &&
            paypalEmail.isNotEmpty() &&
            isEmailValid

    val handleError = {
        errorMessage = null
        isError = false
    }

    val saveUiState by viewModel.uiState.collectAsState()
    val fetchUiState by viewModelfeth.uiState.collectAsState()

    fun performLogin() {
        navController.popBack()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is VoyagerProfileUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                VoyagerProfileUiEffect.SaveSuccess -> {
                    isNetworkError = false
                    performLogin()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModelfeth.uiEffect.collectLatest { effect ->
            when (effect) {
                is GetVoyagerProfileUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    getingData = false
                }
            }
        }
    }

    LaunchedEffect(fetchUiState.profile) {
        if (getingData && fetchUiState.profile != null) {
            val profile = fetchUiState.profile
            phoneNumber = profile?.phoneNumber.orEmpty()
            firstName = profile?.firstName.orEmpty()
            lastName = profile?.lastName.orEmpty()
            address = profile?.address.orEmpty()
            dob = profile?.dateOfBirth.orEmpty()
            paypalEmail = profile?.stripeEmail.orEmpty()
            getingData = false
        }
    }

    LaunchedEffect(fetchUiState.errorMessage) {
        if (getingData && !fetchUiState.errorMessage.isNullOrBlank()) {
            getingData = false
        }
    }

    LaunchedEffect(getingData) {
        viewModelfeth.onEvent(GetVoyagerProfileUiEvent.Load)
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = "Confirming Voyage", onImageClick = {
            })
        },
        content = { innerPadding ->
            if (getingData || fetchUiState.isLoading) {
                Dialog(
                    onDismissRequest = {},
                    DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier =
                            Modifier
                                .size(100.dp)
                                .background(White, shape = RoundedCornerShape(8.dp)),
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
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

                if (showDialog.value) {
                    MyDatePickerDialog(
                        onDateSelected = {
                            bookingDate = it
                            dob = bookingDate
                        },
                        onDismiss = { showDialog.value = false },
                    )
                }

                Spacer(Modifier.height(30.dp))

                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
//                        .height(200.dp) // Set a fixed height for the Card
                            .border(1.dp, Color.Blue, RoundedCornerShape(8.dp)),
                    // Add blue border with rounded corners
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), // Add elevation
                    colors = CardDefaults.cardColors(containerColor = Color.White), // Set background color to white
                ) {
                    Column(Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                style =
                                    TextStyle(
                                        color = Color.Black,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Normal,
                                    ),
                                text = "Sunday, 12 April | 10:00 am",
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.pending),
                                    contentDescription = "Status Icon",
                                    modifier =
                                        Modifier
                                            .size(25.dp) // Adjust size as needed
                                            .padding(end = 5.dp),
                                    // Add some space between text and icon
                                    tint = Color.Blue, // Change color of the icon
                                )
                                Text(
                                    style =
                                        TextStyle(
                                            color = Color(0xFF797979),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                        ),
                                    text = "Pending",
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            style =
                                TextStyle(
                                    color = Color(0xFF6A6969),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.W500,
                                ),
                            text = "Event Conference",
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            style =
                                TextStyle(
                                    color = Color(0xFF6A6969),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.W500,
                                ),
                            text = "2025",
                        )

                        Spacer(Modifier.height(15.dp))

                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500,
                                ),
                            text = "Voyagees details",
                        )
                        Spacer(Modifier.height(15.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            Card(
                                modifier =
                                    Modifier
                                        .padding(5.dp)
                                        .height(205.dp)
                                        .width(155.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                            ) {
                                Column(
                                    modifier =
                                        Modifier
                                            .padding(16.dp)
                                            .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly, // Ensures space is even between the rows
                                ) {
                                    // First row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.passengers),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Blue,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500,
                                                ),
                                            text = "Home",
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp,
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.money_icon),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Blue,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500,
                                                ),
                                            text = "Home",
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp,
                                    )

                                    // Third row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.clock),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Blue,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500,
                                                ),
                                            text = "Home",
                                        )
                                    }
                                }
                            }

                            Card(
                                modifier =
                                    Modifier
                                        .padding(5.dp)
                                        .height(205.dp)
                                        .width(155.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                            ) {
                                Column(
                                    modifier =
                                        Modifier
                                            .padding(16.dp)
                                            .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly, // Ensures space is even between the rows
                                ) {
                                    // First row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.location_icon),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Blue,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500,
                                                ),
                                            text = "Home",
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp,
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.drop_off_loc_icon),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Red,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500,
                                                ),
                                            text = "Home",
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp,
                                    )

                                    // Third row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.flag),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(300.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Blue,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500,
                                                ),
                                            text = "Home",
                                        )
                                    }
                                }
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
                            text = "Sponsors",
                        )

                        Spacer(Modifier.height(5.dp))

                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(200.dp) // Set a fixed height for the inner Card
                                    .padding(5.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // Add elevation
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Person Icon",
                                        modifier =
                                            Modifier
                                                .size(25.dp) // Adjust icon size
                                                .clip(CircleShape) // Make the icon circular
                                                .background(Color.Gray), // Optional: Add background color to the circle
                                    )
                                    Text(
                                        style =
                                            TextStyle(
                                                color = Color.Black,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.W400,
                                            ),
                                        text = "Myself",
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp)) // Space between rows

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Phone Icon",
                                        modifier =
                                            Modifier
                                                .size(25.dp) // Adjust icon size
                                                .clip(CircleShape) // Make the icon circular
                                                .background(Color.Gray), // Optional: Add background color to the circle
                                    )
                                    Text(
                                        style =
                                            TextStyle(
                                                color = Color.Black,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.W400,
                                            ),
                                        text = "Chadwick",
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp)) // Space between rows

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Email Icon",
                                        modifier =
                                            Modifier
                                                .size(25.dp) // Adjust icon size
                                                .clip(CircleShape) // Make the icon circular
                                                .background(Color.Gray), // Optional: Add background color to the circle
                                    )
                                    Text(
                                        style =
                                            TextStyle(
                                                color = Color.Black,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.W400,
                                            ),
                                        text = "Anderson",
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                CustomButton(
                    text = "Confirm Voyage",
                    isValidate = isValidate,
                    isLoading = saveUiState.isLoading,
                    onButtonClick = {
                        viewModel.onEvent(
                            VoyagerProfileUiEvent.Submit(
                                VoyagerProfileRequest(
                                    UserId = userSessionStore.currentUserId().ifBlank { null },
                                    PhoneNumber = phoneNumber,
                                    FirstName = firstName,
                                    LastName = lastName,
                                    Address = address,
                                    DateOfBirth = dob,
                                    StripeEmail = paypalEmail,
                                ),
                            ),
                        )
                        isButtonEnabled = true
                        isLoading = true
                        focusManager.clearFocus()
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        },
    )
}

@Preview
@Composable
fun ConfirmVoyageScreen() {
    ConfirmVoyageScreen(navController = rememberNavController())
}
