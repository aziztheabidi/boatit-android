package com.boatit.boatsharing.ui.signup.captain

import android.widget.Toast
import androidx.annotation.StringRes
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
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainDocumentRequest
import com.boatit.boatsharing.ui.signup.captain.viewmodel.CaptainDocsViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.CaptainProfileViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.GetCaptainDocsViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddCaptainDocumentInfoScreen(
    navController: NavController,
    viewModelfetch: GetCaptainDocsViewModel = koinViewModel(),
    viewModel: CaptainDocsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val registrationState by viewModel.registrationState.collectAsState()
    val fetchState by viewModelfetch.registrationState.collectAsState()
    var getingData by remember { mutableStateOf(true) }

    // Handle success/failure of save
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

    // Fetch and prefill data
    LaunchedEffect(getingData) {
        if (getingData) viewModelfetch.GetDocs()
    }

    LaunchedEffect(fetchState) {
        if (fetchState is NetworkResponse.Success && getingData) {
            viewModel.loadInitialData(fetchState.data)
            getingData = false
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.add_your_document_info) + " 2/3") {
                navController.popBackStack()
            }
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
            FormStepsViews(3, activeColor = colorResource(id = R.color.button_normal), inactiveColor = Color.Gray, activeViewsCount = 2)

            Spacer(Modifier.height(30.dp))

            // Input Fields
            DocumentField(label = R.string.license_label, value = viewModel.licenseNo, onValueChange = { viewModel.licenseNo = it }, errorCondition = viewModel.licenseNo.length <= 5)
            DocumentField(label = R.string.license_exp_label, value = viewModel.licenseNoExpiryDate, onValueChange = { viewModel.licenseNoExpiryDate = it }, errorCondition = viewModel.licenseNoExpiryDate.length <= 3)
            DocumentField(label = R.string.license_type_label, value = viewModel.licenseType, onValueChange = { viewModel.licenseType = it }, errorCondition = viewModel.licenseType.length <= 3)
            DocumentField(label = R.string.insurance_company_label, value = viewModel.insuranceCompany, onValueChange = { viewModel.insuranceCompany = it }, errorCondition = viewModel.insuranceCompany.length <= 3)
            DocumentField(label = R.string.policy_number_label, value = viewModel.policyNo, onValueChange = { viewModel.policyNo = it }, errorCondition = viewModel.policyNo.length <= 3)
            DocumentField(label = R.string.policy_exp_label, value = viewModel.policyExpirationDate, onValueChange = { viewModel.policyExpirationDate = it }, errorCondition = viewModel.policyExpirationDate.length <= 3)
            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = stringResource(R.string.save_button_label),
                isValidate = viewModel.isFormValid,
                isLoading = viewModel.isLoading,
                onButtonClick = {
                    viewModel.saveDocs()
                    focusManager.clearFocus()
                }
            )
        }
    }
}

@Composable
fun DocumentField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    errorCondition: Boolean
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
        keyboardActions = KeyboardActions(onNext = { /* handled in screen */ })
    )
    Spacer(Modifier.height(20.dp))
}

@Preview
@Composable
fun PreviewCaptainDocumentInfo() {
    AddCaptainDocumentInfoScreen(navController = rememberNavController())
}