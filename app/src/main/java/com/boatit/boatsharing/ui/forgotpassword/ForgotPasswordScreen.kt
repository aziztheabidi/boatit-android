package com.boatit.boatsharing.ui.forgotpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager.LOGIN_SCREEN
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomResponseView
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.TermsAndPrivacyView
import kotlinx.coroutines.delay


@Composable
fun ForgotPasswordScreen(navController: NavController) {

    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isEmailValid = email.contains("@") && email.contains(".")

    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }


    val isValidate = email.isNotEmpty() && isEmailValid

    val handleError = {
        errorMessage = null
        isError = false
    }

    suspend fun performLogin():Boolean{
        delay(2000)
        return false
    }

    LaunchedEffect(isButtonEnabled) {
        if (isLoading) {
            val networkSuccess = performLogin()
            isLoading = false
            if (networkSuccess) {
                isNetworkError = false
                println("link send successful")
            } else {
                isNetworkError = true
                errorMessage = "Network error, please try again."
            }
        }
    }
    if (!isNetworkError) {
    Scaffold(
        topBar = {

            CustomTopBar(text = stringResource(R.string.reset_password_h1), onImageClick = {
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
            )
            {

                Spacer(Modifier.height(30.dp))
                Text(
                    text = stringResource(R.string.reset_password_h2),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

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
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),

                )
                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = stringResource(R.string.send_button_text),
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {


                            isButtonEnabled = true
                            isLoading = true
                            focusManager.clearFocus()
                            println("perform network call")


                    }
                )

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
        else{
        CustomResponseView(
            imageResId = R.drawable.response_email,
            text = stringResource(R.string.reset_password_success_message),
            buttonLabel = stringResource(R.string.back_to_login),
            onClick = { navController.navigateWithClearStack(route = LOGIN_SCREEN,clearStack = true) }
        )

    }

}



@Preview
@Composable
fun PreviewForgotPasswordScreen() {
    ForgotPasswordScreen(navController = rememberNavController())
}