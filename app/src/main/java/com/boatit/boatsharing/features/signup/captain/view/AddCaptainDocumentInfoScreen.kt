package com.boatit.boatsharing.features.signup.captain

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.features.signup.captain.view.MyFutureDatePickerDialog
import com.boatit.boatsharing.features.signup.captain.viewmodel.CaptainDocsUiEvent
import com.boatit.boatsharing.features.signup.captain.viewmodel.CaptainDocsViewModel
import com.boatit.boatsharing.features.signup.captain.viewmodel.GetCaptainDocsViewModel
import com.boatit.boatsharing.ui.components.CustomButton
import com.boatit.boatsharing.ui.components.CustomDobField
import com.boatit.boatsharing.ui.components.CustomTextField
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.FormStepsViews
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun AddCaptainDocumentInfoScreen(
    navController: NavController,
    viewModelfetch: GetCaptainDocsViewModel = koinViewModel(),
    viewModel: CaptainDocsViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val showDialog = mutableStateOf(false)
    val showDialogExp = mutableStateOf(false)
    val focusManager = LocalFocusManager.current
    val docsUi by viewModel.uiState.collectAsState()
    val registrationState = docsUi.registrationState
    val fetchVm by viewModelfetch.uiState.collectAsState()
    val fetchState = fetchVm.registrationState
    var getingData by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val handleError = {
        errorMessage = null
        isError = false
        viewModel.onEvent(CaptainDocsUiEvent.ClearError)
    }

    LaunchedEffect(registrationState) {
        when (registrationState) {
            is NetworkResponse.Success -> {
                Toast.makeText(context, registrationState.data?.Message, Toast.LENGTH_SHORT).show()
                navController.navigate(NavigationManager.CAPTAIN_BOAT_INFO_SCREEN)
            }

            is NetworkResponse.Error -> {
                Toast.makeText(context, (registrationState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    LaunchedEffect(getingData) {
        if (getingData) viewModelfetch.GetDocs()
    }

    LaunchedEffect(fetchState) {
        if (fetchState is NetworkResponse.Success && getingData) {
            viewModel.onEvent(CaptainDocsUiEvent.LoadInitial(fetchState.data))
            getingData = false
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = stringResource(R.string.add_your_document_info) + " 2/3") {
                navController.popBackStack()
            }
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
            FormStepsViews(3, activeColor = colorResource(id = R.color.button_normal), inactiveColor = Color.Gray, activeViewsCount = 2)
            if (showDialog.value) {
                MyFutureDatePickerDialog(
                    onDateSelected = {
                        viewModel.onEvent(CaptainDocsUiEvent.PolicyExpirationChanged(it))
                    },
                    onDismiss = { showDialog.value = false },
                )
            }
            if (showDialogExp.value) {
                MyFutureDatePickerDialog(
                    onDateSelected = {
                        viewModel.onEvent(CaptainDocsUiEvent.LicenseExpiryChanged(it))
                    },
                    onDismiss = { showDialogExp.value = false },
                )
            }
            Spacer(Modifier.height(30.dp))
            DocumentField(label = R.string.license_label, value = docsUi.licenseNo, onValueChange = {
                viewModel.onEvent(CaptainDocsUiEvent.LicenseNoChanged(it))
            }, errorCondition = docsUi.licenseNo.length <= 5)
            Text(text = "License Expiration Date", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal, color = Color.Black))
            Spacer(Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.clickable { showDialogExp.value = true },
            ) {
                CustomDobField(
                    textValue = docsUi.licenseNoExpiryDate,
                    placeholderText = stringResource(R.string.dob_placeholder),
                    onTextChange = { viewModel.onEvent(CaptainDocsUiEvent.LicenseExpiryChanged(it)) },
                    keyboardType = KeyboardType.Text,
                    maxChars = 40,
                    errorMessage = null,
                    isError = false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { /* handled in screen */ }),
                )
            }
            Spacer(Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(10.dp))

            DocumentField(label = R.string.license_type_label, value = docsUi.licenseType, onValueChange = {
                viewModel.onEvent(CaptainDocsUiEvent.LicenseTypeChanged(it))
            }, errorCondition = docsUi.licenseType.length <= 3)
            DocumentField(label = R.string.insurance_company_label, value = docsUi.insuranceCompany, onValueChange = {
                viewModel.onEvent(CaptainDocsUiEvent.InsuranceCompanyChanged(it))
            }, errorCondition = docsUi.insuranceCompany.length <= 3)
            DocumentField(label = R.string.policy_number_label, value = docsUi.policyNo, onValueChange = {
                viewModel.onEvent(CaptainDocsUiEvent.PolicyNoChanged(it))
            }, errorCondition = docsUi.policyNo.length <= 3)
            Text(text = "Policy Expiration Date", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal, color = Color.Black))
            Spacer(Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.clickable { showDialog.value = true },
            ) {
                CustomDobField(
                    textValue = docsUi.policyExpirationDate,
                    placeholderText = stringResource(R.string.dob_placeholder),
                    onTextChange = { viewModel.onEvent(CaptainDocsUiEvent.PolicyExpirationChanged(it)) },
                    keyboardType = KeyboardType.Text,
                    maxChars = 40,
                    errorMessage = null,
                    isError = false,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { /* handled in screen */ }),
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            CustomButton(
                text = stringResource(R.string.save_button_label),
                isValidate = docsUi.isFormValid,
                isLoading = docsUi.isLoading,
                onButtonClick = {
                    viewModel.onEvent(CaptainDocsUiEvent.SaveDocs)
                    focusManager.clearFocus()
                },
            )
        }
    }
}

@Composable
fun DocumentField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    errorCondition: Boolean,
) {
    val labelText = stringResource(id = label)
    Text(text = labelText, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal, color = Color.Black))
    Spacer(Modifier.height(10.dp))
    CustomTextField(
        textValue = value,
        placeholderText = labelText,
        onTextChange = onValueChange,
        keyboardType = KeyboardType.Text,
        maxChars = 100,
        errorMessage = if (value.isNotEmpty() && errorCondition) "$labelText is too short" else null,
        isError = value.isNotEmpty() && errorCondition,
        onClearError = { /* Do nothing */ },
        imeAction = ImeAction.Next,
        keyboardActions = KeyboardActions(onNext = { /* handled in screen */ }),
    )
    Spacer(Modifier.height(20.dp))
}

@Preview
@Composable
fun PreviewCaptainDocumentInfo() {
    AddCaptainDocumentInfoScreen(navController = rememberNavController())
}
