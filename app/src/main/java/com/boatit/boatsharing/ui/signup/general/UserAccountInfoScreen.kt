package com.boatit.boatsharing.ui.signup.general

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
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import kotlinx.coroutines.delay


@Composable
fun UserAccountInfoScreen(navController: NavController,value: String?) {


    println("comingFrom:$value")

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


    val isEmailValid = paypalEmail.contains("@") && paypalEmail.contains(".")
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
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

                 if (value.toString() == "captainRole"){

                     navController.navigate(NavigationManager.CAPTAIN_DOCUMENT_INFO_SCREEN)

                 }
                 else if (value.toString() == "businessRole"){

                     navController.navigate(NavigationManager.BUSINESS_GENERAL_INFO_SCREEN)

                 }
                else{
                    //// navigate to home
                }

            }
        }
    }
    Scaffold(
        topBar = {
            if (value.toString() == "captainRole"){
                CustomTopBar(text = stringResource(R.string.add_your_acc_info)+" 1/3", onImageClick = {
                    println("clicked...")
                })
            }
            else if (value.toString() == "businessRole"){
                CustomTopBar(text = stringResource(R.string.add_your_acc_info)+" 1/4", onImageClick = {
                    println("clicked...")
                })
            }
            else{
                CustomTopBar(text = stringResource(R.string.add_your_acc_info), onImageClick = {
                    println("clicked...")
                })
            }

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
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.firstname_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = firstName,
                    placeholderText = stringResource(R.string.firstname_placeholder),
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
                    focusRequester = firstNameFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))



                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.lastname_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = lastName,
                    placeholderText = stringResource(R.string.lastname_placeholder),
                    onTextChange = { lastName = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (lastName.isNotEmpty()&& lastName.length <= 3) stringResource(R.string.lastname_validation_text) else null,
                    isError = lastName.isNotEmpty()&& lastName.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { phoneNumberFocusRequester.requestFocus() }
                    ),
                    focusRequester = lastNameFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))



                Text(
                    text = stringResource(R.string.phone_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = phoneNumber,
                    placeholderText = stringResource(R.string.phone_placeholder),
                    onTextChange = { phoneNumber = it },
                    keyboardType = KeyboardType.Number,
                    maxChars = 15,
                    errorMessage = if (phoneNumber.isNotEmpty() && phoneNumber.length <= 3) stringResource(
                        R.string.phone_validation_text
                    ) else null,
                    isError = phoneNumber.isNotEmpty()&& phoneNumber.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { addressFocusRequester.requestFocus() }
                    ),
                    focusRequester = phoneNumberFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.address_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = address,
                    placeholderText = stringResource(R.string.address_placeholder),
                    onTextChange = { address = it },
                    keyboardType = KeyboardType.Text,
                    singleLine = false,
                    maxLines = 3,
                    maxChars = 200,
                    errorMessage = if (address.isNotEmpty() && address.length <= 3) stringResource(R.string.address_validation_text) else null,
                    isError = address.isNotEmpty() && address.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { dobFocusRequester.requestFocus() }
                    ),
                    focusRequester = addressFocusRequester
                )


                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.dob_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = dob,
                    placeholderText = stringResource(R.string.dob_placeholder),
                    onTextChange = { dob = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 40,
                    errorMessage = if (dob.isNotEmpty() && dob.length <= 3) stringResource(R.string.dob_validation_text) else null,
                    isError = dob.isNotEmpty()&& dob.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { paypalFocusRequester.requestFocus() }
                    ),
                    focusRequester = dobFocusRequester
                )


                Spacer(Modifier.height(20.dp))
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.paypal_email_label)
                )

                Spacer(Modifier.height(10.dp))


                CustomTextField(
                    textValue = paypalEmail,
                    placeholderText = stringResource(R.string.email_placeholder),
                    onTextChange = { paypalEmail = it },
                    keyboardType = KeyboardType.Email,
                    maxChars = 100,
                    errorMessage = if (!isEmailValid && paypalEmail.isNotEmpty()) stringResource(R.string.email_validation_text) else null,
                    isError = !isEmailValid && paypalEmail.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    ),
                    focusRequester = paypalFocusRequester
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
fun PreviewVoyagerAccountInfo() {
    UserAccountInfoScreen(navController = rememberNavController(),"")
}