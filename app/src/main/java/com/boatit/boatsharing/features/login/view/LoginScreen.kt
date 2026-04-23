package com.boatit.boatsharing.features.login.view

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.ui.navigation.AccountRoutes
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.navigation.navigateToVoyagerDashboard
import com.boatit.boatsharing.features.login.viewmodel.LoginUiEffect
import com.boatit.boatsharing.features.login.viewmodel.LoginUiEvent
import com.boatit.boatsharing.features.login.viewmodel.LoginViewModel
import com.boatit.boatsharing.features.login.viewmodel.PostLoginDestination
import com.boatit.boatsharing.features.userroles.viewmodel.FCMTokenUiEvent
import com.boatit.boatsharing.features.userroles.viewmodel.FCMTokenViewModel
import com.boatit.boatsharing.ui.components.CustomButton
import com.boatit.boatsharing.ui.components.CustomClickableSmallTextview
import com.boatit.boatsharing.ui.components.CustomClickableTextView
import com.boatit.boatsharing.ui.components.CustomTextField
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.PasswordTextField
import com.boatit.boatsharing.ui.components.TermsAndPrivacyView
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel(),
    viewModelFcm: FCMTokenViewModel = koinViewModel(),
    userSessionStore: UserSessionStore = get(UserSessionStore::class.java),
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is LoginUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is LoginUiEffect.PostLogin -> {
                    val uid = effect.userIdForFcm
                    if (!uid.isNullOrBlank()) {
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val token = task.result
                                val userId = userSessionStore.currentUserId()
                                if (userId.isNotBlank()) {
                                    viewModelFcm.onEvent(FCMTokenUiEvent.UpdateFcmToken(userId, token))
                                }
                            }
                        }
                    }
                    when (effect.destination) {
                        PostLoginDestination.SelectRole ->
                            navController.navigate(NavigationManager.SELECT_ROLE_SCREEN)
                        PostLoginDestination.VoyagerDashboard ->
                            navController.navigateToVoyagerDashboard()
                        PostLoginDestination.VoyagerAccountInfo ->
                            navController.navigate(route = AccountRoutes.userAccountInfo("voyagerRole"))
                        PostLoginDestination.BusinessDashboard ->
                            navController.navigate(NavigationManager.BUSINESS_SCREEN)
                        PostLoginDestination.BusinessAccountInfo ->
                            navController.navigate(NavigationManager.BUSINESS_ACCT_INFO_SCREEN)
                        PostLoginDestination.CaptainOffline ->
                            navController.navigate(NavigationManager.CAPTAIN_OFFLINE_SCREEN)
                        PostLoginDestination.CaptainAccountInfo ->
                            navController.navigate(NavigationManager.CAPTAIN_INFO_SCREEN)
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(
                text = stringResource(R.string.login_h1),
                onImageClick = {
                    navController.popBackStack()
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(
                            top = innerPadding.calculateTopPadding() + 15.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = innerPadding.calculateTopPadding() + 25.dp,
                        )
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
            ) {
                Spacer(Modifier.height(30.dp))

                Text(
                    text = stringResource(R.string.email),
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = uiState.email,
                    placeholderText = stringResource(R.string.email_placeholder),
                    onTextChange = { viewModel.onEvent(LoginUiEvent.EmailChanged(it)) },
                    keyboardType = KeyboardType.Email,
                    maxChars = 100,
                    errorMessage =
                        if (!uiState.isEmailValid && uiState.email.isNotEmpty()) {
                            stringResource(R.string.email_validation_text)
                        } else {
                            null
                        },
                    isError = !uiState.isEmailValid && uiState.email.isNotEmpty(),
                    onClearError = { viewModel.onEvent(LoginUiEvent.ClearError) },
                    imeAction = ImeAction.Next,
                    keyboardActions =
                        KeyboardActions(
                            onNext = { passwordFocusRequester.requestFocus() },
                        ),
                    focusRequester = emailFocusRequester,
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.password),
                    style =
                        TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                        ),
                )

                Spacer(Modifier.height(10.dp))

                PasswordTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.PasswordChanged(it)) },
                    errorMessage =
                        if (!uiState.isPasswordValid && uiState.password.isNotEmpty()) {
                            stringResource(R.string.password_validation_text)
                        } else {
                            null
                        },
                    isError = !uiState.isPasswordValid && uiState.password.isNotEmpty(),
                    onClearError = { viewModel.onEvent(LoginUiEvent.ClearError) },
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    keyboardActions =
                        KeyboardActions(
                            onDone = { focusManager.clearFocus() },
                        ),
                    focusRequester = passwordFocusRequester,
                )

                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterEnd, // Align to the right center
                ) {
                    CustomClickableSmallTextview(
                        text = stringResource(R.string.forgot_password),
                        onTextClick = {
                            navController.navigate(NavigationManager.FORGOT_PASSWORD_SCREEN)
                        },
                    )
                }
                Spacer(Modifier.height(30.dp))

                CustomButton(
                    text = stringResource(R.string.login),
                    isValidate = uiState.isFormValid,
                    isLoading = uiState.isLoading,
                    onButtonClick = {
                        focusManager.clearFocus()
                        viewModel.onEvent(LoginUiEvent.LoginClicked)
                    },
                )

                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CustomClickableTextView(
                        text = stringResource(R.string.create_account_),
                        onTextClick = {
                            navController.navigate(NavigationManager.CREATE_ACCOUNT_STEP_ONE_SCREEN)
                        },
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
                },
            )
        },
    )
}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}
