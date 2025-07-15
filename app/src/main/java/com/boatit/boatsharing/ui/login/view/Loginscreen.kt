package com.boatit.boatsharing.ui.login.view

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.viewmodel.LoginViewModel
import com.boatit.boatsharing.ui.userroles.viewmodel.FCMTokenViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.RegistrationViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomClickableTextView
import com.boatit.boatsharing.uihelpers.CustomErrorView
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.PasswordTextField
import com.boatit.boatsharing.uihelpers.TermsAndPrivacyView
import com.boatit.boatsharing.utils.AppConstants
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel(),
    viewModelFcm: FCMTokenViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val email = viewModel.email
    val password = viewModel.password
    val isFormValid = viewModel.isFormValid
    val isLoading = viewModel.isLoading
    val loginState by viewModel.loginState.collectAsState()

    fun performLogin(response: LoginResponse?) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                viewModelFcm.fcm(AppConstants.USER_ID.toString(), token)
            }
        }
        when (response?.obj?.Role) {
            "Voyager" -> navController.navigate("$DASHBOARD_SCREEN/null")
            "Business" -> navController.navigate(NavigationManager.BUSINESS_SCREEN)
            "Captain" -> navController.navigate(NavigationManager.CAPTAIN_OFFLINE_SCREEN)
            else -> navController.navigate(NavigationManager.SELECT_ROLE_SCREEN)
        }
        viewModel.resetNearbyPlaces()
    }

    when (loginState) {
        is NetworkResponse.Success -> {
            Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
            performLogin((loginState as NetworkResponse.Success<LoginResponse>).data)
        }

        is NetworkResponse.Error -> {
            val error = (loginState as NetworkResponse.Error).message
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.resetNearbyPlaces()
        }

        else -> {}
    }

        Scaffold(
            containerColor = Color.White,
            topBar = {
                CustomTopBar(
                    text = stringResource(R.string.login_h1),
                    onImageClick = {
                        navController.popBackStack()

                    }
                )
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
                    Spacer(Modifier.height(30.dp))

                    Text(
                        text = stringResource(R.string.email),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(Modifier.height(10.dp))

                    CustomTextField(
                        textValue = email,
                        placeholderText = stringResource(R.string.email_placeholder),
                        onTextChange = viewModel::onEmailChange,
                        keyboardType = KeyboardType.Email,
                        maxChars = 100,
                        errorMessage = if (!viewModel.isEmailValid && email.isNotEmpty())
                            stringResource(R.string.email_validation_text)
                        else null,
                        isError = !viewModel.isEmailValid && email.isNotEmpty(),
                        onClearError = { viewModel.errorMessage = null },
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { passwordFocusRequester.requestFocus() }
                        ),
                        focusRequester = emailFocusRequester
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = stringResource(R.string.password),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    )

                    Spacer(Modifier.height(10.dp))

                    PasswordTextField(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        errorMessage = if (!viewModel.isPasswordValid && password.isNotEmpty())
                            stringResource(R.string.password_validation_text)
                        else null,
                        isError = !viewModel.isPasswordValid && password.isNotEmpty(),
                        onClearError = { viewModel.errorMessage = null },
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        focusRequester = passwordFocusRequester
                    )

                    Spacer(Modifier.height(40.dp))

                    CustomButton(
                        text = stringResource(R.string.login),
                        isValidate = isFormValid,
                        isLoading = isLoading,
                        onButtonClick = {
                            focusManager.clearFocus()
                            viewModel.login()
                        }
                    )

                    Spacer(Modifier.height(10.dp))

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomClickableTextView(
                            text = stringResource(R.string.forgot_password),
                            onTextClick = {
                                navController.navigate(NavigationManager.FORGOT_PASSWORD_SCREEN)
                            }
                        )
                    }
                }
            },
            bottomBar = {
                TermsAndPrivacyView(
                    onClick = {
                        val url = ApiConstants.PRIVACY_POLICY
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
            }
        )

}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}