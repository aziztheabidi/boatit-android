package com.boatit.boatsharing.features.forgotpassword.view

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.features.forgotpassword.viewmodel.ForgotPassUiEffect
import com.boatit.boatsharing.features.forgotpassword.viewmodel.ForgotPassUiEvent
import com.boatit.boatsharing.features.forgotpassword.viewmodel.ForgotPassViewModel
import com.boatit.boatsharing.ui.components.CustomButton
import com.boatit.boatsharing.ui.components.CustomTextField
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.TermsAndPrivacyView
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPassViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is ForgotPassUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                ForgotPassUiEffect.NavigateToLogin -> {
                    navController.navigate(NavigationManager.LOGIN_SCREEN)
                }
            }
        }
    }

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
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding() + 15.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 25.dp,
                    )
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.reset_password_h2),
                style =
                    TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.email),
                style =
                    TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                    ),
            )

            Spacer(Modifier.height(10.dp))

            CustomTextField(
                textValue = uiState.email,
                placeholderText = stringResource(R.string.email_placeholder),
                onTextChange = { viewModel.onEvent(ForgotPassUiEvent.EmailChanged(it)) },
                keyboardType = KeyboardType.Email,
                maxChars = 100,
                errorMessage =
                    if (!uiState.isEmailValid && uiState.email.isNotEmpty()) {
                        stringResource(R.string.email_validation_text)
                    } else {
                        null
                    },
                isError = !uiState.isEmailValid && uiState.email.isNotEmpty(),
                onClearError = { viewModel.onEvent(ForgotPassUiEvent.ClearError) },
                imeAction = ImeAction.Done,
                keyboardActions =
                    KeyboardActions(
                        onDone = { focusManager.clearFocus() },
                    ),
            )

            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = stringResource(R.string.send_button_text),
                isValidate = uiState.isFormValid,
                isLoading = uiState.isLoading,
                onButtonClick = {
                    viewModel.onEvent(ForgotPassUiEvent.Submit)
                    focusManager.clearFocus()
                },
            )
        }
    }
}

@Preview
@Composable
fun PreviewForgotPasswordScreen() {
    ForgotPasswordScreen(navController = rememberNavController())
}
