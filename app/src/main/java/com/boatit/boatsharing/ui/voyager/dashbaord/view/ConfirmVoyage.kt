package com.boatit.boatsharing.ui.voyager.dashbaord.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
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
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.ui.signup.general.repository.GetVoyagerProfileViewModel
import com.boatit.boatsharing.ui.signup.general.repository.VoyagerProfileViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.ui.draw.clip


@SuppressLint("UnrememberedMutableState")
@Composable
fun ConfirmVoyageScreen(navController: NavController, viewModel: VoyagerProfileViewModel = koinViewModel(), viewModelfeth: GetVoyagerProfileViewModel = koinViewModel()) {

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
            CustomTopBar(text = "Confirming Voyage", onImageClick = {
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

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
//                        .height(200.dp) // Set a fixed height for the Card
                        .border(1.dp, Color.Blue, RoundedCornerShape(8.dp)), // Add blue border with rounded corners
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), // Add elevation
                    colors = CardDefaults.cardColors(containerColor = Color.White) // Set background color to white
                ) {
                    Column(Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                text = "Sunday, 12 April | 10:00 am"
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                painter = painterResource(id = R.drawable.pending),
                                contentDescription = "Status Icon",
                                modifier = Modifier
                                    .size(25.dp) // Adjust size as needed
                                    .padding(end = 5.dp), // Add some space between text and icon
                                tint = Color.Blue // Change color of the icon
                            )
                                Text(
                                    style = TextStyle(
                                        color = Color(0xFF797979),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500
                                    ),
                                    text = "Pending"
                                )

                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            style = TextStyle(
                                color = Color(0xFF6A6969),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W500
                            ),
                            text = "Event Conference"
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            style = TextStyle(
                                color = Color(0xFF6A6969),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W500
                            ),
                            text = "2025"
                        )

                        Spacer(Modifier.height(15.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500
                            ),
                            text = "Voyagees details"
                        )
                        Spacer(Modifier.height(15.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .height(205.dp)
                                    .width(155.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly // Ensures space is even between the rows
                                ) {
                                    // First row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.passengers),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Blue
                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = "Home"
                                        )
                                    }

                                    Divider(color = Color(0xFFA0A0A0),
                                        thickness = 1.dp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.money_icon),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Blue
                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = "Home"
                                        )
                                    }

                                    Divider(color = Color(0xFFA0A0A0),
                                       thickness = 1.dp)

                                    // Third row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.clock),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Blue
                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = "Home"
                                        )
                                    }
                                }
                            }

                            Card(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .height(205.dp)
                                    .width(155.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly // Ensures space is even between the rows
                                ) {
                                    // First row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.location_icon),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Blue
                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = "Home"
                                        )
                                    }

                                    Divider(color = Color(0xFFA0A0A0),
                                        thickness = 1.dp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.drop_off_loc_icon),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Red

                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = "Home"
                                        )
                                    }

                                    Divider(color = Color(0xFFA0A0A0),
                                        thickness = 1.dp)

                                    // Third row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.flag),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(300.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Blue
                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = "Home"
                                        )
                                    }
                                }
                            }
                        }



                        Spacer(Modifier.height(10.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500
                            ),
                            text = "Sponsors"
                        )

                        Spacer(Modifier.height(5.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp) // Set a fixed height for the inner Card
                                .padding(5.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Add elevation
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Person Icon",
                                        modifier = Modifier
                                            .size(25.dp) // Adjust icon size
                                            .clip(CircleShape) // Make the icon circular
                                            .background(Color.Gray) // Optional: Add background color to the circle
                                    )
                                    Text(
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.W400
                                        ),
                                        text = "Myself"
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp)) // Space between rows

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Phone Icon",
                                        modifier = Modifier
                                            .size(25.dp) // Adjust icon size
                                            .clip(CircleShape) // Make the icon circular
                                            .background(Color.Gray) // Optional: Add background color to the circle
                                    )
                                    Text(
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.W400
                                        ),
                                        text = "Chadwick"
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp)) // Space between rows

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Email Icon",
                                        modifier = Modifier
                                            .size(25.dp) // Adjust icon size
                                            .clip(CircleShape) // Make the icon circular
                                            .background(Color.Gray) // Optional: Add background color to the circle
                                    )
                                    Text(
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.W400
                                        ),
                                        text = "Anderson"
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
                    }
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