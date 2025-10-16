package com.boatit.boatsharing.ui.voyager.dashbaord.view

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.draw.clip
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
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.chat.viewmodel.VoyagersListViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.model.Sponser
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagerProfile
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FollowedVoyagerViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.uihelpers.MyDatePickerDialog
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun SponsorScreen(navController: NavController,
                  viewModel: VoyagersListViewModel = koinViewModel()) {

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
    var isChecked by remember { mutableStateOf(false) }
    val searchQuery = viewModel.searchQuery
    var followed = viewModel.filteredBoatListFollowed

    val isValidate = true

    val handleError = {
        errorMessage = null
        isError = false
    }

    val registrationState by viewModel.loginState.collectAsState()

    when (registrationState) {
        is NetworkResponse.Loading -> {
            
        }
        is NetworkResponse.Error -> {
            viewModel.resetNearbyPlaces()
        }
        is NetworkResponse.Success -> {
            viewModel.onBoatList(registrationState.data!!)
            viewModel.resetNearbyPlaces()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = "Add sponsors", onImageClick = {
               navController.popBackStack()
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
                    textValue = searchQuery,
                    placeholderText = "Search",
                    onTextChange = { viewModel.updateSearchQuery(it)},
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage =  null,
                    isError = false,
                    onClearError = {},
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


                Text(
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = "Hey, sponsors are users you follow through the 'Connect with Voyagers' option in the Menu. All registered voyagers are shown in this list."
                )


                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp) // Set a fixed height for the Card
                        .padding(5.dp),
                ) {
                    LazyColumn {
                        items(followed.size) { prediction ->
                            val user = followed[prediction]
                            val isAlreadyAdded = remember { mutableStateOf(
                                AppConstants.sponsorList.any { it.VoyagerUserId == user.UserId }
                            ) }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (!isAlreadyAdded.value) {
                                            AppConstants.sponsorList.add(
                                                Sponser(
                                                    VoyagerUserId = user.UserId,
                                                    VoyagerUserName = "",
                                                    AmountToPay = 0.0,
                                                    Status = ""
                                                )
                                            )
                                            AppConstants.Estimated_Cost =
                                                AppConstants.Estimated_Cost!! / AppConstants.sponsorList.size
                                            isAlreadyAdded.value = true
                                            Toast.makeText(context, "Sponsor Added", Toast.LENGTH_SHORT).show()
                                        } else {
                                            // Optional: If you want to allow removing
                                            AppConstants.sponsorList.removeIf { it.VoyagerUserId == user.UserId }
                                            AppConstants.Estimated_Cost =
                                                if (AppConstants.sponsorList.isNotEmpty())
                                                    AppConstants.Estimated_Cost!! / AppConstants.sponsorList.size
                                                else 0.0
                                            isAlreadyAdded.value = false
                                            Toast.makeText(context, "Sponsor Removed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .padding(vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(50.dp)
                                            .height(50.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE0E0E0)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = user.FirstName.firstOrNull()?.uppercase() ?: "",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = user.FirstName,
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        modifier = Modifier.padding(0.dp)
                                    )
                                }

                                Checkbox(
                                    checked = isAlreadyAdded.value,
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = colorResource(id = R.color.button_normal),
                                        uncheckedColor = Color.Gray,
                                        checkmarkColor = Color.White
                                    )
                                )

                            }
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                CustomButton(
                    text = "Back",
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