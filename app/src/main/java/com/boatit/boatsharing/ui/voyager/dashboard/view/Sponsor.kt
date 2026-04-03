package com.boatit.boatsharing.ui.voyager.dashboard.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.voyager.dashboard.model.Sponser
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagerProfile
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.FollowedVoyagerViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun SponsorScreen(navController: NavController, viewModel: FollowedVoyagerViewModel = koinViewModel()) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }
    val addressFocusRequester = remember { FocusRequester() }
    val dobFocusRequester = remember { FocusRequester() }
    val paypalFocusRequester = remember { FocusRequester() }

    var PLACES: List<VoyagerProfile> = emptyList()
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


    val isValidate = true

    val handleError = {
        errorMessage = null
        isError = false
    }

    val registrationState by viewModel.registrationState.collectAsState()

    fun performLogin(){}

    LaunchedEffect(Unit) {
        viewModel.FollowedVoyagerFunc()
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = "Add sponsors", onImageClick = {
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

                Spacer(Modifier.height(30.dp))


                CustomTextField(
                    textValue = firstName,
                    placeholderText = "Search",
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

                Spacer(Modifier.height(20.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Add Sponsors"
                )

                Spacer(Modifier.height(5.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp) // Set a fixed height for the Card
                        .padding(5.dp),
                ) {
                    LazyColumn {
                        when (registrationState) {
                            is NetworkResponse.Loading -> {
                                println("Loading")
                            }

                            is NetworkResponse.Error -> {
                                println("Error")
                            }

                            is NetworkResponse.Success -> {
                                items( registrationState.data?.obj?.Followed!!.size) { prediction ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                AppConstants.sponsorList.add(Sponser(VoyagerUserId = registrationState.data?.obj?.Followed!!.get(prediction).UserId))
                                                Toast.makeText(context, "Sponsor Added" , Toast.LENGTH_SHORT).show()
                                            }
                                            .padding(vertical = 8.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Icon(
                                                    painter = painterResource(id = R.drawable.current_marker),
                                                    contentDescription = null,
                                                    tint = Color.Unspecified,
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                        .padding(end = 5.dp)
                                                )
                                                Text(
                                                    text =  registrationState.data?.obj?.Followed!!.get(prediction).FirstName,
                                                    fontSize = 16.sp,
                                                    color = Color.Black,
                                                    modifier = Modifier.padding(0.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                CustomButton(
                    text = "Invite to Sponsor",
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {
                        navController.popBack()
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        },
    )
}


@Preview
@Composable
fun SponsorScreen() {
    SponsorScreen(navController = rememberNavController())
}