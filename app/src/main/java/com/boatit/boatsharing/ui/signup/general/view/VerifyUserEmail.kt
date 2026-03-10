package com.boatit.boatsharing.ui.signup.general.view

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.graphics.Color.Companion.White
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
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager.CREATE_ACCOUNT_STEP_THREE_SCREEN
import com.boatit.boatsharing.ui.signup.general.repository.VerifyEmailViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.uihelpers.TermsAndPrivacyView
import com.boatit.boatsharing.utils.CountDownTimer
import org.koin.androidx.compose.koinViewModel

@Composable
fun VerifyUserEmail(
    navController: NavController,
    userEmail: String,
    viewModel: VerifyEmailViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val registrationState by viewModel.registrationState.collectAsState()
    val enteredValues = remember { mutableStateListOf("", "", "", "", "") }
    val focusRequesters = remember { List(5) { FocusRequester() } }
    var isTimerEnabled by remember { mutableStateOf(false) }
    val isValidate = enteredValues.all { it.isNotEmpty() }
    fun navigateToNextStep(token: String?) {
        Log.e("tokenValue",token.toString())
        navController.navigate("$CREATE_ACCOUNT_STEP_THREE_SCREEN/$token")
    }

    when (registrationState) {
        is NetworkResponse.Success -> {
            Toast.makeText(context, registrationState.data?.Message, Toast.LENGTH_SHORT).show()
                val token = registrationState.data?.obj
            if (!token.isNullOrBlank()) {
                navigateToNextStep(registrationState.data?.obj)
                viewModel.resetNearbyPlaces()
            }
        }

        is NetworkResponse.Error -> {
            Toast.makeText(context, (registrationState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.resetNearbyPlaces()
        }

        else -> Unit
    }

    Scaffold(
        containerColor = White,
        topBar = {
            CustomTopBar(text = "${stringResource(R.string.add_your_info)} 2/3") {
               navController.popBackStack()
            }
        },
        bottomBar = {
            TermsAndPrivacyView(onClick = {
                val url = ApiConstants.PRIVACY_POLICY
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            })
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding() + 15.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 25.dp
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            FormStepsViews(
                numberOfViews = 3,
                activeColor = colorResource(id = R.color.button_normal),
                inactiveColor = Color.Gray,
                activeViewsCount = 2
            )

            Spacer(Modifier.height(30.dp))

            val message = stringResource(R.string.email_verification_text, userEmail)

            Text(
                text = message,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.code_text),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                enteredValues.forEachIndexed { index, text ->
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            textValue = text,
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
                            imeAction = if (index == 4) ImeAction.Done else ImeAction.Next,
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    if (index < 4) focusRequesters[index + 1].requestFocus()
                                },
                                onDone = { focusManager.clearFocus() }
                            ),
                            showTrailingIcon = false,
                            focusRequester = focusRequesters[index],
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            CustomButton(
                text = stringResource(R.string.verify_email),
                isValidate = isValidate,
                isLoading = isLoading,
                onButtonClick = {
                    if (isValidate) {
                        focusManager.clearFocus()
                        val otp = enteredValues.joinToString("")
                        viewModel.verifyEmail(userEmail, otp)
                        isTimerEnabled = true
                    }
                }
            )

            if (isTimerEnabled) {
                CountDownTimer(
                    onResendClick = { navController.popBackStack() },
                    text = "Resend Code",
                    onStartTimer = { isTimerEnabled = false }
                )
            }

            Spacer(Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                val annotatedText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = Color.Black, fontSize = 14.sp)
                    ) {
                        append(stringResource(R.string.wrong_email_text) + " ")
                    }

                    pushStringAnnotation(tag = "differentEmail", annotation = "differentEmail")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    ) {
                        append(stringResource(R.string.another_email_text))
                    }
                    pop()
                }

                Text(
                    text = annotatedText,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            navController.popBackStack()
                            //  navController.navigate(CREATE_ACCOUNT_STEP_THREE_SCREEN)
                        },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



@Preview
@Composable
fun PreviewRegistrationStepTwo() {
    VerifyUserEmail(
        navController = rememberNavController(),
        userEmail = "",
    )
}