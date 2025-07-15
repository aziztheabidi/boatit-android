package com.boatit.boatsharing.ui.signup.general.view

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.boatit.boatsharing.network.di.ApiConstants
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
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserBasicInfoScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val nameFocusRequester = remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.navigateToNext.collectLatest { email ->
            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
            navController.navigate("$CREATE_ACCOUNT_STEP_TWO_SCREEN/$email")
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = "${stringResource(R.string.add_your_info)} 1/3", onImageClick = {  navController.popBackStack()})
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
                    activeViewsCount = 1
                )

                Spacer(Modifier.height(30.dp))

                Text("Email", style = TextStyle(fontSize = 18.sp, color = Color.Black))
                Spacer(Modifier.height(10.dp))
                CustomTextField(
                    textValue = viewModel.email,
                    placeholderText = stringResource(R.string.email_placeholder),
                    onTextChange = { viewModel.onEmailChange(it) },
                    keyboardType = KeyboardType.Email,
                    maxChars = 100,
                    errorMessage = if (!viewModel.isEmailValid && viewModel.email.isNotEmpty())
                        stringResource(R.string.email_validation_text)
                    else null,
                    isError = !viewModel.isEmailValid && viewModel.email.isNotEmpty(),
                    onClearError = { viewModel.errorMessage = null },
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { nameFocusRequester.requestFocus() }),
                    focusRequester = emailFocusRequester
                )

                Spacer(Modifier.height(20.dp))
                Text("Name", style = TextStyle(fontSize = 18.sp, color = Color.Black))
                Spacer(Modifier.height(10.dp))
                CustomTextField(
                    textValue = viewModel.name,
                    placeholderText = stringResource(R.string.name_placeholder),
                    onTextChange = { viewModel.onNameChange(it) },
                    keyboardType = KeyboardType.Text,
                    maxChars = 200,
                    errorMessage = if (!viewModel.isNameValid && viewModel.name.isNotEmpty())
                        stringResource(R.string.name_validation_text)
                    else null,
                    isError = !viewModel.isNameValid && viewModel.name.isNotEmpty(),
                    onClearError = { viewModel.errorMessage = null },
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { phoneNumberFocusRequester.requestFocus() }),
                    focusRequester = nameFocusRequester
                )

                Spacer(Modifier.height(20.dp))
                Text("Phone Number", style = TextStyle(fontSize = 18.sp, color = Color.Black))
                Spacer(Modifier.height(10.dp))
                CustomTextField(
                    textValue = viewModel.phoneNumber,
                    placeholderText = stringResource(R.string.phone_placeholder),
                    onTextChange = { viewModel.onPhoneChange(it) },
                    keyboardType = KeyboardType.Number,
                    maxChars = 15,
                    errorMessage = if (!viewModel.isPhoneValid && viewModel.phoneNumber.isNotEmpty())
                        stringResource(R.string.phone_validation_text)
                    else null,
                    isError = !viewModel.isPhoneValid && viewModel.phoneNumber.isNotEmpty(),
                    onClearError = { viewModel.errorMessage = null },
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    focusRequester = phoneNumberFocusRequester
                )

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = "Next",
                    isValidate = viewModel.isFormValid,
                    isLoading = viewModel.isLoading,
                    onButtonClick = {
                        focusManager.clearFocus()
                        viewModel.register()
                    }
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
        }
    )
}


@Preview
@Composable
fun PreviewRegistrationStepOne() {
    UserBasicInfoScreen(navController = rememberNavController())
}