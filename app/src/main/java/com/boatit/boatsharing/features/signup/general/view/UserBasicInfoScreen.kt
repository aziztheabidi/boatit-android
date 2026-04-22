package com.boatit.boatsharing.features.signup.general.view

import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import com.boatit.boatsharing.features.signup.general.viewmodel.RegistrationUiEffect
import com.boatit.boatsharing.features.signup.general.viewmodel.RegistrationUiEvent
import com.boatit.boatsharing.features.signup.general.viewmodel.RegistrationViewModel
import com.boatit.boatsharing.ui.components.CustomButton
import com.boatit.boatsharing.ui.components.CustomTextField
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.FormStepsViews
import com.boatit.boatsharing.ui.components.TermsAndPrivacyView
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserBasicInfoScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val nameFocusRequester = remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is RegistrationUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is RegistrationUiEffect.NavigateToNext -> {
                    navController.navigate(AccountRoutes.createAccountStepTwo(effect.email))
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = "${stringResource(R.string.add_your_info)} 1/3", onImageClick = { navController.popBackStack() })
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
                FormStepsViews(
                    numberOfViews = 3,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 1,
                )

                Spacer(Modifier.height(30.dp))

                Text("Email", style = TextStyle(fontSize = 18.sp, color = Color.Black))
                Spacer(Modifier.height(10.dp))
                CustomTextField(
                    textValue = uiState.email,
                    placeholderText = stringResource(R.string.email_placeholder),
                    onTextChange = { viewModel.onEvent(RegistrationUiEvent.EmailChanged(it)) },
                    keyboardType = KeyboardType.Email,
                    maxChars = 100,
                    errorMessage =
                        if (!uiState.isEmailValid && uiState.email.isNotEmpty()) {
                            stringResource(R.string.email_validation_text)
                        } else {
                            null
                        },
                    isError = !uiState.isEmailValid && uiState.email.isNotEmpty(),
                    onClearError = { viewModel.onEvent(RegistrationUiEvent.ClearError) },
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { nameFocusRequester.requestFocus() }),
                    focusRequester = emailFocusRequester,
                )

                Spacer(Modifier.height(20.dp))
                Text("Name", style = TextStyle(fontSize = 18.sp, color = Color.Black))
                Spacer(Modifier.height(10.dp))
                CustomTextField(
                    textValue = uiState.name,
                    placeholderText = stringResource(R.string.name_placeholder),
                    onTextChange = { viewModel.onEvent(RegistrationUiEvent.NameChanged(it)) },
                    keyboardType = KeyboardType.Text,
                    maxChars = 200,
                    errorMessage =
                        if (!uiState.isNameValid && uiState.name.isNotEmpty()) {
                            stringResource(R.string.name_validation_text)
                        } else {
                            null
                        },
                    isError = !uiState.isNameValid && uiState.name.isNotEmpty(),
                    onClearError = { viewModel.onEvent(RegistrationUiEvent.ClearError) },
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { phoneNumberFocusRequester.requestFocus() }),
                    focusRequester = nameFocusRequester,
                )

                Spacer(Modifier.height(20.dp))
                Text("Phone Number", style = TextStyle(fontSize = 18.sp, color = Color.Black))
                Spacer(Modifier.height(10.dp))
                CustomTextField(
                    textValue = uiState.phoneNumber,
                    placeholderText = stringResource(R.string.phone_placeholder),
                    onTextChange = { viewModel.onEvent(RegistrationUiEvent.PhoneChanged(it)) },
                    keyboardType = KeyboardType.Number,
                    maxChars = 15,
                    errorMessage =
                        if (!uiState.isPhoneValid && uiState.phoneNumber.isNotEmpty()) {
                            stringResource(R.string.phone_validation_text)
                        } else {
                            null
                        },
                    isError = !uiState.isPhoneValid && uiState.phoneNumber.isNotEmpty(),
                    onClearError = { viewModel.onEvent(RegistrationUiEvent.ClearError) },
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    focusRequester = phoneNumberFocusRequester,
                )

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = "Next",
                    isValidate = uiState.isFormValid,
                    isLoading = uiState.isLoading,
                    onButtonClick = {
                        focusManager.clearFocus()
                        viewModel.onEvent(RegistrationUiEvent.Submit)
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        },
        bottomBar = {
            TermsAndPrivacyView(onClick = {
                val url = ApiConstants.PRIVACY_POLICY
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            })
        },
    )
}

@Preview
@Composable
fun PreviewRegistrationStepOne() {
    UserBasicInfoScreen(navController = rememberNavController())
}
