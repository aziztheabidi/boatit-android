 package com.boatit.boatsharing.ui.signup.business


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
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
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import kotlinx.coroutines.delay

@Composable
fun AddGeneralBusinessInfo(navController: NavController) {

    val focusManager = LocalFocusManager.current
    val businessNameFocusRequester = remember { FocusRequester() }
    val businessTypeFocusRequester = remember { FocusRequester() }
    val businessAddressFocusRequester = remember { FocusRequester() }
    val businessPhoneNoFocusRequester = remember { FocusRequester() }
    val establishmentYearFocusRequester = remember { FocusRequester() }
    val businessTimeFocusRequester = remember { FocusRequester() }


    var businessName by remember { mutableStateOf("") }
    var businessType by remember { mutableStateOf("") }
    var businessAddress by remember { mutableStateOf("") }
    var businessPhoneNo by remember { mutableStateOf("") }
    var establishmentYear by remember { mutableStateOf("") }
    var businessTime by remember { mutableStateOf("") }



    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }


    val isValidate = businessName.isNotEmpty()
            && businessType.isNotEmpty()
            && businessAddress.isNotEmpty()
            && businessPhoneNo.isNotEmpty()
            && establishmentYear.isNotEmpty()
            && businessTime.isNotEmpty()

    val handleError = {
        errorMessage = null
        isError = false
    }


    suspend fun performLogin(): Boolean {
        delay(2000)
        return false
    }

    LaunchedEffect(isButtonEnabled) {
        if (isLoading) {
            val networkSuccess = performLogin()
            isLoading = false
            if (networkSuccess) {
                isNetworkError = false
                println("info added")


            } else {
                isNetworkError = true
                errorMessage = "Network error, please try again."

                 navController.navigate(NavigationManager.BUSINESS_DESCRIPTIONS_SCREEN)

            }
        }
    }
    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.add_your_business_info)+ " 2/4", onImageClick = {
                println("clicked...")
                navController.popBack()
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
                    numberOfViews = 4,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 2
                )

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
                    textValue = businessName,
                    placeholderText = stringResource(R.string.business_name_placeholder),
                    onTextChange = { businessName = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (businessName.isNotEmpty()&& businessName.length <= 3) stringResource(R.string.business_name_validation_text) else null,
                    isError = businessName.isNotEmpty(),
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
                    textValue = businessType,
                    placeholderText = stringResource(R.string.business_type_placeholder),
                    onTextChange = { businessType = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (businessType.isNotEmpty()&& businessType.length <= 3) stringResource(R.string.business_type_validation_text) else null,
                    isError = businessType.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { businessAddressFocusRequester.requestFocus() }
                    ),
                    focusRequester = businessTypeFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))



                Text(
                    text = stringResource(R.string.business_address_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = businessAddress,
                    placeholderText = stringResource(R.string.business_address_placeholder),
                    onTextChange = { businessAddress = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 500,
                    singleLine = false,
                    maxLines = 3,
                    errorMessage = if (businessAddress.isNotEmpty() && businessAddress.length <= 3) stringResource(
                        R.string.business_address_validation_text
                    ) else null,
                    isError = businessAddress.isNotEmpty(),
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
                    textValue = businessPhoneNo,
                    placeholderText = stringResource(R.string.business_contact_no_placeholder),
                    onTextChange = { businessPhoneNo = it },
                    keyboardType = KeyboardType.Number,
                    maxChars = 50,
                    errorMessage = if (businessPhoneNo.isNotEmpty() && businessPhoneNo.length <= 5) stringResource(R.string.business_contact_no_validation_text) else null,
                    isError = businessPhoneNo.isNotEmpty(),
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

                CustomTextField(
                    textValue = establishmentYear,
                    placeholderText = stringResource(R.string.business_starting_year_placeholder),
                    onTextChange = { establishmentYear = it },
                    keyboardType = KeyboardType.Number,
                    maxChars = 4,
                    errorMessage = if (establishmentYear.isNotEmpty() && establishmentYear.length <= 4) stringResource(R.string.business_starting_year_validation_text) else null,
                    isError = establishmentYear.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { businessTimeFocusRequester.requestFocus() }
                    ),
                    focusRequester = establishmentYearFocusRequester
                )


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


                CustomTextField(
                    textValue = businessTime,
                    placeholderText = stringResource(R.string.business_time_placeholder),
                    onTextChange = { businessTime = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 50,
                    errorMessage = if  (businessTime.isNotEmpty() && businessTime.length <= 5)stringResource(R.string.business_time_validation_text) else null,
                    isError =  businessTime.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    ),
                    focusRequester = businessTimeFocusRequester
                )



                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = stringResource(R.string.save_button_label),
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {

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
fun PreviewGeneralBusinessInfo() {
    AddGeneralBusinessInfo(navController = rememberNavController())
}