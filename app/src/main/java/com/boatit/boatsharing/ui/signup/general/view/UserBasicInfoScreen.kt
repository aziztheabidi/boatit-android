package com.boatit.boatsharing.ui.signup.general.view

import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.CREATE_ACCOUNT_STEP_TWO_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.USER_ACCOUNT_INFO_SCREEN
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.RegistrationViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.uihelpers.TermsAndPrivacyView
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserBasicInfoScreen(navController: NavController, viewModel: RegistrationViewModel = koinViewModel(), ) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val emailFocusRequester =  remember { FocusRequester() }
    val nameFocusRequester =  remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }

    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val isEmailValid = email.contains("@") && email.contains(".")
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    val registrationState by viewModel.registrationState.collectAsState()

    fun performLogin(){
        navController.navigate(route = "$CREATE_ACCOUNT_STEP_TWO_SCREEN/$email")
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
            isLoading = false
            isNetworkError = true
            errorMessage = "Network error, please try again."
            Toast.makeText(context, (registrationState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    val isValidate = email.isNotEmpty() && name.isNotEmpty()&& phoneNumber.isNotEmpty()
            && isEmailValid

    val handleError = {
        errorMessage = null
        isError = false
    }

    LaunchedEffect(isButtonEnabled) {

    }

    Scaffold(
        topBar = {
            CustomTopBar(text = "${stringResource(R.string.add_your_info)} 1/3", onImageClick = {
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
                        bottom = innerPadding.calculateTopPadding()+25.dp,
                    )
                    .fillMaxSize()
                    // .background(color = Color.Gray)
                    .verticalScroll(rememberScrollState())
            ) {

                FormStepsViews(numberOfViews = 3,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 1)

                Spacer(Modifier.height(30.dp))
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.email)
                )

                Spacer(Modifier.height(10.dp))



                CustomTextField(
                    textValue = email,
                    placeholderText = stringResource(R.string.email_placeholder),
                    onTextChange = { email = it },
                    keyboardType = KeyboardType.Email,
                    maxChars = 100,
                    errorMessage = if (!isEmailValid && email.isNotEmpty()) stringResource(R.string.email_validation_text) else null,
                    isError = !isEmailValid && email.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { nameFocusRequester.requestFocus() }
                    ),
                    focusRequester = emailFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.name_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = name,
                    placeholderText = stringResource(R.string.name_placeholder),
                    onTextChange = { name = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 200,
                    errorMessage = if (name.isNotEmpty() && name.length <= 3) stringResource(R.string.name_validation_text) else null,
                    isError =  name.isNotEmpty() && name.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { phoneNumberFocusRequester.requestFocus() }
                    ),
                    focusRequester = nameFocusRequester
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
                    errorMessage = if (phoneNumber.isNotEmpty() && phoneNumber.length <= 3) stringResource(R.string.phone_validation_text) else null,
                    isError =  phoneNumber.isNotEmpty()  && phoneNumber.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    focusRequester = phoneNumberFocusRequester
                )


                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = "Next",
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {
                        viewModel.registerUser(name, phoneNumber, email)
                        isButtonEnabled = true
                        isLoading = true
                        focusManager.clearFocus()
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))

            }
        },



        bottomBar = {
            TermsAndPrivacyView(
                onClick = {

                }
            )

        }


    )

}

@Preview
@Composable
fun PreviewRegistrationStepOne() {
    UserBasicInfoScreen(navController = rememberNavController())
}