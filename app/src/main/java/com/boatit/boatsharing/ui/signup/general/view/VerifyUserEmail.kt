package com.boatit.boatsharing.ui.signup.general.view

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.CREATE_ACCOUNT_STEP_THREE_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.CREATE_ACCOUNT_STEP_TWO_SCREEN
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.signup.general.repository.VerifyEmailViewModel
import com.boatit.boatsharing.utils.CountDownTimer
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.uihelpers.TermsAndPrivacyView
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun VerifyUserEmail(navController: NavController, value: String, viewModel : VerifyEmailViewModel = koinViewModel()) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val enteredValues = remember { mutableStateListOf("", "", "","","") }
    val focusRequesters = remember { List(5) { FocusRequester() } }
    var isTimerEnabled by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }

    var isValidate = if (enteredValues.all { it.isNotEmpty() }) {
        true
    } else {
          println("Please enter all digits.")
        false
    }

    val registrationState by viewModel.registrationState.collectAsState()

    fun performLogin(token: String?){
        navController.navigate(route = "$CREATE_ACCOUNT_STEP_THREE_SCREEN/$token")
    }

    when (registrationState) {
        is NetworkResponse.Success -> {
            if(isLoading){
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, registrationState.data?.Message , Toast.LENGTH_SHORT).show()
                performLogin(registrationState.data?.obj)
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


    LaunchedEffect(isButtonEnabled) {}

    Scaffold(
        topBar = {
            CustomTopBar(text = "${stringResource(R.string.add_your_info)} 2/3", onImageClick = {
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

                FormStepsViews(numberOfViews = 3,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 2)

                Spacer(Modifier.height(30.dp))



                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    ),

                    text = stringResource(R.string.email_verification_text)
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.code_text)
                )

                Spacer(Modifier.height(10.dp))


                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        enteredValues.forEachIndexed { index, value ->

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 0.dp)
                            ) {
                                CustomTextField(
                                    textValue = value,
                                    placeholderText = "",

                                    onTextChange = { input ->
                                        if (input.length <= 1 && input.all { it.isDigit() }) {
                                            enteredValues[index] = input
                                            if (input.isNotEmpty() && index < enteredValues.lastIndex) {
                                                focusRequesters[index + 1].requestFocus()
                                            }
                                        }
                                    },
                                    keyboardType = KeyboardType.Number,
                                    maxChars = 1,
                                    errorMessage = null,
                                    isError = false,
                                    onClearError = {},
                                    imeAction = if (index == enteredValues.lastIndex) ImeAction.Done else ImeAction.Next,
                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            if (index < enteredValues.lastIndex) {
                                                focusRequesters[index + 1].requestFocus()
                                            }
                                        },
                                        onDone = {
                                            println("value: ${enteredValues.joinToString("")}")
                                            focusManager.clearFocus()
                                        }
                                    ),
                                    showTrailingIcon = false,
                                    focusRequester = focusRequesters[index],
                                    textAlign = TextAlign.Center,
                                )
                            }


                    }
                    }

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = "Verify Email",
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {
                        if (!isValidate) {
                            errorMessage = ""
                        }
                        else {
                            val otp = enteredValues.get(0) + enteredValues.get(1) + enteredValues.get(2) + enteredValues.get(3) + enteredValues.get(4)
                            viewModel.VerifyEmail(value,  otp)
                            isTimerEnabled=true
                            isButtonEnabled = true
                            isLoading = true
                            focusManager.clearFocus()
                            println("perform network call")
                        }
                    }
                )
                 if (isTimerEnabled){
                     CountDownTimer(
                         onResendClick = {  navController.popBack() },
                         text ="Resend Code",
                         onStartTimer = { isValidate = false },
                     )
                 }
                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth().
                        height(40.dp), contentAlignment = Alignment.TopCenter
                ) {


                    val annotatedText = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp)
                        ) {
                            append(stringResource(R.string.wrong_email_text) +" ")
                        }

                        pushStringAnnotation(tag = "differentEmail", annotation = "differentEmail")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,fontSize = 16.sp,
                            color = Color.Black)
                        ) {
                            append(stringResource(R.string.another_email_text))
                        }
                        pop()
                    }

                    Text(
                        text = annotatedText,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                            .clickable
                            {
                                navController.navigate(CREATE_ACCOUNT_STEP_THREE_SCREEN)
                                println("click")
                            },
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        textAlign = TextAlign.Center
                    )


                }

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
fun PreviewRegistrationStepTwo() {
    VerifyUserEmail(
        navController = rememberNavController(),
        value = "",
    )
}