package com.boatit.boatsharing.ui.voyager.dashbaord.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.CREATE_VOYAGE_SPONSOR_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.ui.voyager.dashbaord.model.Sponser
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants


@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateVoyageRateCalcScreen(navController: NavController) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    var splitPaymentSwitchState by rememberSaveable { mutableStateOf(false) }
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var paypalEmail by remember { mutableStateOf("") }
    val showDialog = mutableStateOf(false)
    var bookingDate = AppConstants.Event_Date
    var eventTime = AppConstants.No_of_Hour

    val isEmailValid = paypalEmail.contains("@") && paypalEmail.contains(".")
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var getingData by remember { mutableStateOf(true) }
    var isNetworkError by remember { mutableStateOf(false) }
    val isValidate = firstName.isNotEmpty()
    val handleError = {
        errorMessage = null
        isError = false
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

                Spacer(Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kindly fill in the relevant details for the Voyage",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    Spacer(Modifier.width(5.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            text = stringResource(R.string.selected_booking_date)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            text = bookingDate.toString()
                        )
                    }
                }


                Spacer(Modifier.height(30.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Name of the Event"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = firstName,
                    placeholderText = firstName,
                    onTextChange = {
                        firstName = it
                        AppConstants.Event_Name = firstName },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.event_calender),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "No. of Voyagers"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = AppConstants.No_Of_Voyagers.toString()!!,
                    placeholderText = AppConstants.No_Of_Voyagers.toString()!!,
                    onTextChange = { },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage =  null,
                    isError = false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    isEditable =  false,
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

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Per Hour Rate"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = AppConstants.Per_Hour_Rate.toString()!!,
                    placeholderText = AppConstants.Per_Hour_Rate.toString()!!,
                    onTextChange = { },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    isEditable =  false,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.dollar),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Estimated Cost"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue = AppConstants.Estimated_Cost.toString()!!,
                    placeholderText = AppConstants.Estimated_Cost.toString()!!,
                    onTextChange = {},
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    isEditable =  false,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.dollar),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Duration"
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = eventTime.toString(),
                    placeholderText = eventTime.toString(),
                    onTextChange = {},
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage =  null,
                    isError = false,
                    isEditable =  false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester,
                            leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.clock),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Pickup"
                )

                Spacer(Modifier.height(5.dp))

                CustomTextField(
                    textValue = AppConstants.Pick_Up_Loc?.second!!,
                    placeholderText = AppConstants.Pick_Up_Loc?.second!!,
                    onTextChange = {},
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError =false,
                    isEditable =  false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.location_icon),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    ),
                    text = "Drop off"
                )

                Spacer(Modifier.height(5.dp))


                CustomTextField(
                    textValue =  AppConstants.Drop_Off_Loc?.second!!,
                    placeholderText =  AppConstants.Drop_Off_Loc?.second!!,
                    onTextChange = { },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError =false,
                    isEditable =  false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.drop_off_loc_icon),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                )

                Spacer(Modifier.height(15.dp))

                if(AppConstants.Travel_Now!!){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {  Text(
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        text = "Split Payment"
                    )

                        Switch(
                            checked = splitPaymentSwitchState,
                            onCheckedChange = {
                                splitPaymentSwitchState = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = colorResource(id = R.color.button_normal),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFD9D9D9),
                                uncheckedBorderColor = Color.Transparent
                            )
                        )


                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = "Proceed",
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {
                        AppConstants.Event_Name = firstName
                        AppConstants.Split = splitPaymentSwitchState
                        if(splitPaymentSwitchState){
                            AppConstants.sponsorList = arrayListOf()
                            AppConstants.sponsorList.add(
                                Sponser(
                                    VoyagerUserId = AppConstants.USER_ID!!,
                                    VoyagerUserName = "",
                                    AmountToPay = 0.0,
                                    Status = ""
                                )
                            )
                        }
                        navController.navigate(route = "$CREATE_VOYAGE_SPONSOR_SCREEN/{$splitPaymentSwitchState}")
                    }
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