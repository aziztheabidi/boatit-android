 package com.boatit.boatsharing.ui.signup.business


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.signup.business.viewmodel.BusinessInfoViewModel
import com.boatit.boatsharing.ui.signup.business.viewmodel.GetBusinessInfoViewModel
import com.boatit.boatsharing.ui.signup.business.viewmodel.GetBusinessProfileViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.CaptainDocsViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.view.MyTimePickerDialog
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDobField
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

 @SuppressLint("UnrememberedMutableState")
@Composable
fun AddGeneralBusinessInfo(navController: NavController,
     viewModel: BusinessInfoViewModel = koinViewModel(),
     viewModelfetch: GetBusinessInfoViewModel = koinViewModel()
 ) {
     val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val businessNameFocusRequester = remember { FocusRequester() }
    val businessTypeFocusRequester = remember { FocusRequester() }
    val businessAddressFocusRequester = remember { FocusRequester() }
    val businessPhoneNoFocusRequester = remember { FocusRequester() }
    val establishmentYearFocusRequester = remember { FocusRequester() }
    val businessTimeFocusRequester = remember { FocusRequester() }
    val showDialog = mutableStateOf(false)
    val showTimeDialog = mutableStateOf(false)
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false)}
    var gettingData by remember { mutableStateOf(true) }
     val fetchState by viewModelfetch.registrationState.collectAsState()

    val isValidate = viewModel.businessName.isNotEmpty()
            && viewModel.businessType.isNotEmpty()
            && viewModel.businessAddress.isNotEmpty()
            && viewModel.businessPhoneNo.isNotEmpty()
            && viewModel.establishmentYear.isNotEmpty()
            && viewModel.businessTime.isNotEmpty()

    val handleError = {
        errorMessage = null
        isError = false
    }

     val registrationState by viewModel.registrationState.collectAsState()

     fun performLogin(){
         navController.navigate(NavigationManager.BUSINESS_DESCRIPTIONS_SCREEN)
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

    LaunchedEffect(gettingData) {
     if (gettingData) viewModelfetch.GetBusinessProfile()
    }

     LaunchedEffect(fetchState) {
         if (fetchState is NetworkResponse.Success && gettingData) {
             viewModel.loadInitialData(fetchState.data)
             gettingData = false
         }
     }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = stringResource(R.string.add_your_business_info)+ " 2/4", onImageClick = {
                
                navController.popBack()
            })
        },
        content = { innerPadding ->
            if (isLoading || gettingData) {
                Dialog(
                    onDismissRequest = {},
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .background(White, shape = RoundedCornerShape(8.dp))
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }else {
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
                    numberOfViews = 4,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 2
                )

                if (showDialog.value) {
                    MyDatePickerDialog(
                        onDateSelected = {
                            viewModel.bookingDate = it
                            viewModel.establishmentYear = viewModel.bookingDate
                        },
                        onDismiss = { showDialog.value = false }
                    )
                }

                if (showTimeDialog.value) {
                    MyTimePickerDialog(
                        onDateSelected = {
                            viewModel.businessTime =  it + ":00"
                            showTimeDialog.value = false
                        },
                        onDismiss = { showTimeDialog.value = false}
                    )
                }

                Spacer(Modifier.height(30.dp))
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.business_name_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = viewModel.businessName,
                    placeholderText = stringResource(R.string.business_name_placeholder),
                    onTextChange = { viewModel.businessName = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (viewModel.businessName.isNotEmpty()&& viewModel.businessName.length <= 3) stringResource(R.string.business_name_validation_text) else null,
                    isError = viewModel.businessName.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { businessTypeFocusRequester.requestFocus() }
                    ),
                    focusRequester = businessNameFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))



                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.business_type_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = viewModel.businessType,
                    placeholderText = stringResource(R.string.business_type_placeholder),
                    onTextChange = { viewModel.businessType = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (viewModel.businessType.isNotEmpty()&& viewModel.businessType.length <= 3) stringResource(R.string.business_type_validation_text) else null,
                    isError = viewModel.businessType.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { businessAddressFocusRequester.requestFocus() }
                    ),
                    focusRequester = businessTypeFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))



//                Text(
//                    text = stringResource(R.string.business_address_label),
//                    style = TextStyle(
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Normal,
//                        color = Color.Black
//                    )
//                )

                // Observe value from map_picker result
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val selectedAddress = navBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("selected_address")

                LaunchedEffect(selectedAddress) {
                    if (!selectedAddress.isNullOrBlank()) {
                        viewModel.businessAddress = selectedAddress
                        navBackStackEntry?.savedStateHandle?.remove<String>("selected_address")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.business_address_label),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            navController.navigate("map_picker")
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.location_icon), // your drawable
                            contentDescription = "Edit",
                            tint = colorResource(R.color.button_normal),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Pick location",
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(R.color.button_normal)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = viewModel.businessAddress,
                    placeholderText = stringResource(R.string.business_address_placeholder),
                    onTextChange = { viewModel.businessAddress = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 500,
                    singleLine = false,
                    maxLines = 3,
                    errorMessage = if (viewModel.businessAddress.isNotEmpty() && viewModel.businessAddress.length <= 3) stringResource(
                        R.string.business_address_validation_text
                    ) else null,
                    isError = viewModel.businessAddress.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { businessPhoneNoFocusRequester.requestFocus() }
                    ),
                    focusRequester = businessAddressFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.business_contact_no_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = viewModel.businessPhoneNo,
                    placeholderText = stringResource(R.string.business_contact_no_placeholder),
                    onTextChange = { viewModel.businessPhoneNo = it },
                    keyboardType = KeyboardType.Number,
                    maxChars = 50,
                    errorMessage = if (viewModel.businessPhoneNo.isNotEmpty() && viewModel.businessPhoneNo.length <= 5) stringResource(R.string.business_contact_no_validation_text) else null,
                    isError = viewModel.businessPhoneNo.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { establishmentYearFocusRequester.requestFocus() }
                    ),
                    focusRequester = businessPhoneNoFocusRequester
                )


                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.business_starting_year_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier.clickable { showDialog.value = true }
                ) {
                    CustomDobField(
                        textValue = viewModel.establishmentYear,
                        placeholderText = stringResource(R.string.business_starting_year_placeholder),
                        onTextChange = { viewModel.establishmentYear = it },
                        keyboardType = KeyboardType.Number,
                        maxChars = 4,
                        errorMessage = null,
                        isError = viewModel.establishmentYear.isNotEmpty(),
                        onClearError = handleError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { businessTimeFocusRequester.requestFocus() }
                        ),
                        focusRequester = establishmentYearFocusRequester
                    )
                }

                Spacer(Modifier.height(20.dp))
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.business_time_label)
                )

                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier.clickable {
                        showTimeDialog.value = true
                    }
                ) {
                    CustomDobField(
                        textValue = viewModel.businessTime,
                        placeholderText = stringResource(R.string.business_time_placeholder),
                        onTextChange = { viewModel.businessTime = it },
                        keyboardType = KeyboardType.Text,
                        maxChars = 40,
                        errorMessage = if  (viewModel.businessTime.isNotEmpty() && viewModel.businessTime.length <= 5)stringResource(R.string.business_time_validation_text) else null,
                        isError =  viewModel.businessTime.isNotEmpty(),
                        onClearError = handleError,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.clearFocus() }
                        ),
                        focusRequester = businessTimeFocusRequester,
                        showBorder = false
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = stringResource(R.string.save_button_label),
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {
                        isLoading = true
                        viewModel.saveBusinessProfile()
                        focusManager.clearFocus()
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

            }}
        },

        )
}

 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun MyDatePickerDialog(onDateSelected: (String) -> Unit, onDismiss: () -> Unit
 ) {
     val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
         override fun isSelectableDate(utcTimeMillis: Long): Boolean {
             return utcTimeMillis <= System.currentTimeMillis()
         }
     })

     val selectedDateMillis = datePickerState.selectedDateMillis
     val selectedDate = selectedDateMillis?.let {
         val calendar = Calendar.getInstance().apply { timeInMillis = it }
         val year = calendar.get(Calendar.YEAR)
         val month = calendar.get(Calendar.MONTH) + 1 // Months are 0-based, so add 1
         val day = calendar.get(Calendar.DAY_OF_MONTH)
         "$year"
     } ?: ""

     DatePickerDialog(
         onDismissRequest = { onDismiss() },
         confirmButton = {
             Button(onClick = {
                 onDateSelected(selectedDate.toString())
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
         }
     ) {
         DatePicker(
             state = datePickerState
         )
     }
 }

@Preview
@Composable
fun PreviewGeneralBusinessInfo() {
    AddGeneralBusinessInfo(navController = rememberNavController())
}