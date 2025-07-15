package com.boatit.boatsharing.ui.forgotpassword.view

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.LOGIN_SCREEN
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.ui.forgotpassword.viewmodel.ForgotPassViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomResponseView
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.TermsAndPrivacyView
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPassViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val registrationState by viewModel.loginState.collectAsState()

    // Observe registration state and respond
    LaunchedEffect(registrationState) {
        when (registrationState) {
            is NetworkResponse.Success -> {
                Toast.makeText(context, registrationState.data?.Message ?: "Success", Toast.LENGTH_SHORT).show()
                navController.navigate(NavigationManager.LOGIN_SCREEN)
            }
            is NetworkResponse.Error -> {
                Toast.makeText(context, (registrationState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    if (!viewModel.isNetworkError) {

            Scaffold(
                containerColor = Color.White,
                topBar = {
                    CustomTopBar(text = stringResource(R.string.reset_password_h1), onImageClick = {
                        navController.popBackStack()
                    })
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
                        text = stringResource(R.string.email),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    )

                    Spacer(Modifier.height(10.dp))

                    CustomTextField(
                        textValue = viewModel.email,
                        placeholderText = stringResource(R.string.email_placeholder),
                        onTextChange = { viewModel.email = it },
                        keyboardType = KeyboardType.Email,
                        maxChars = 100,
                        errorMessage = if (!viewModel.isEmailValid && viewModel.email.isNotEmpty()) {
                            stringResource(R.string.email_validation_text)
                        } else null,
                        isError = !viewModel.isEmailValid && viewModel.email.isNotEmpty(),
                        onClearError = { viewModel.clearError() },
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    CustomButton(
                        text = stringResource(R.string.send_button_text),
                        isValidate = viewModel.isFormValid,
                        isLoading = viewModel.isLoading,
                        onButtonClick = {
                            viewModel.forgotPass()
                            focusManager.clearFocus()
                        }
                    )
                }
            }

    }
}


@Preview
@Composable
fun PreviewForgotPasswordScreen() {
    ForgotPasswordScreen(navController = rememberNavController())
}