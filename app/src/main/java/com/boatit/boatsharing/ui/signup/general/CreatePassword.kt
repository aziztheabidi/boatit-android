package com.boatit.boatsharing.ui.signup.general

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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager.LOGIN_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.SELECT_ROLE_SCREEN
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomResponseView
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.uihelpers.PasswordTextField
import com.boatit.boatsharing.uihelpers.TermsAndPrivacyView
import kotlinx.coroutines.delay

@Composable
fun CreatePassword(navController: NavController) {

    val focusManager = LocalFocusManager.current
    val passwordFocusRequester =  remember { FocusRequester() }
    var password by remember { mutableStateOf("") }


    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }




    val isValidate =
            password.isNotEmpty()

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
                println("info added")

            } else {
                isNetworkError = true
                errorMessage = "Network error, please try again."
            }
        }
    }
    if (!isNetworkError) {
    Scaffold(
        topBar = {
            CustomTopBar(text = "${stringResource(R.string.add_your_info)} 3/3", onImageClick = {
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
                    numberOfViews = 3,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 3
                )

                Spacer(Modifier.height(30.dp))




                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.password)
                )

                Spacer(Modifier.height(10.dp))

                PasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    errorMessage = null,
                    isError = false,
                    onClearError = {},
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    focusRequester = passwordFocusRequester
                )

                PasswordStrengthIndicator(password = password)
                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = stringResource(R.string.continue_button_text),
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
    } else{
        CustomResponseView(
            imageResId = R.drawable.sucess_icon,
            text = "Your Account\nCreated successfully",
            buttonLabel = "Select Role",
            onClick = {

                navController.navigateWithClearStack(route = SELECT_ROLE_SCREEN,clearStack = true)
            }
        )

    }

}

@Preview
@Composable
fun PreviewRegistrationStepThree() {
    CreatePassword(navController = rememberNavController())
}



@Composable
fun PasswordStrengthIndicator(password: String) {

    val hasMinChars = password.length >= 8
    val hasNumber = password.any { it.isDigit() }
    val hasSymbol = password.any { !it.isLetterOrDigit() }

    val progressBarColor = when {
        hasMinChars && hasNumber && hasSymbol -> colorResource(R.color.green)
        hasMinChars && hasNumber -> colorResource(R.color.yellow)
        hasMinChars -> colorResource(R.color.red)
        else -> Color.Gray
    }

    val progressValue = when {
        hasMinChars && hasNumber && hasSymbol -> 1f
        hasMinChars && hasNumber -> 0.66f
        hasMinChars -> 0.33f
        else -> 0f
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {

        LinearProgressIndicator(
            progress = { progressValue },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = progressBarColor,
        )

        Spacer(modifier = Modifier.height(16.dp))


        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            ValidationSteps(isValid = hasMinChars, text = "8 characters minimum")
            Spacer(modifier = Modifier.width(16.dp))


            ValidationSteps(isValid = hasNumber, text = "a number")
            Spacer(modifier = Modifier.width(16.dp))

            ValidationSteps(isValid = hasSymbol, text = "a symbol")
        }
    }
}

@Composable
fun ValidationSteps(isValid: Boolean, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = if (isValid) painterResource(id = R.drawable.filled_circle) else painterResource(id = R.drawable.circle),
            contentDescription = "validation_icon",
            tint = Color.Unspecified,
            modifier = Modifier.size(18.dp),
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            ),
        )
    }
}


