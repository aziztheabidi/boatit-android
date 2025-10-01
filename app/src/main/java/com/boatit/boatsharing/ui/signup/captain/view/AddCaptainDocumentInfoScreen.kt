package com.boatit.boatsharing.ui.signup.captain

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
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.ui.signup.captain.view.MyDatePickerDialog
import com.boatit.boatsharing.ui.signup.captain.view.MyFutureDatePickerDialog
import com.boatit.boatsharing.ui.signup.captain.viewmodel.CaptainDocsViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.GetCaptainDocsViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDobField
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun AddCaptainDocumentInfoScreen(
    navController: NavController,
    viewModelfetch: GetCaptainDocsViewModel = koinViewModel(),
    viewModel: CaptainDocsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val showDialog = mutableStateOf(false)
    val showDialogExp = mutableStateOf(false)
    val focusManager = LocalFocusManager.current
    val registrationState by viewModel.registrationState.collectAsState()
    val fetchState by viewModelfetch.registrationState.collectAsState()
    var gettingData by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val handleError = {
        errorMessage = null
        isError = false
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

    LaunchedEffect(gettingData) {
        if (gettingData) viewModelfetch.GetDocs()
    }

    LaunchedEffect(fetchState) {
        if (fetchState is NetworkResponse.Success && gettingData) {
            viewModel.loadInitialData(fetchState.data)
            gettingData = false
        }
    }

    Scaffold(
        containerColor = Color.White,
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
            if (showDialog.value) {
                MyFutureDatePickerDialog(
                    onDateSelected = {
                        viewModel.policyExpirationDate = it

                    },
                    onDismiss = { showDialog.value = false }
                )


            }
            if (showDialogExp.value) {
                MyFutureDatePickerDialog(
                    onDateSelected = {
                        viewModel.licenseNoExpiryDate = it
                    },
                    onDismiss = { showDialogExp.value = false }
                )
            }
            Spacer(Modifier.height(30.dp))
            DocumentField(label = R.string.license_label, value = viewModel.licenseNo, onValueChange = { viewModel.licenseNo = it }, errorCondition = viewModel.licenseNo.length <= 5)
            Text(text = "License Expiration Date", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal, color = Color.Black))
            Spacer(Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.clickable { showDialogExp.value = true }
            ) {
                CustomDobField(
                    textValue = viewModel.licenseNoExpiryDate,
                    placeholderText = stringResource(R.string.dob_placeholder),
                    onTextChange = { viewModel.licenseNoExpiryDate = it },
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

            DocumentField(label = R.string.license_type_label, value = viewModel.licenseType, onValueChange = { viewModel.licenseType = it }, errorCondition = viewModel.licenseType.length <= 3)
            DocumentField(label = R.string.insurance_company_label, value = viewModel.insuranceCompany, onValueChange = { viewModel.insuranceCompany = it }, errorCondition = viewModel.insuranceCompany.length <= 3)
            DocumentField(label = R.string.policy_number_label, value = viewModel.policyNo, onValueChange = { viewModel.policyNo = it }, errorCondition = viewModel.policyNo.length <= 3)
            Text(text = "Policy Expiration Date", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal, color = Color.Black))
            Spacer(Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.clickable { showDialog.value = true }
            ) {
                CustomDobField(
                    textValue = viewModel.policyExpirationDate,
                    placeholderText = stringResource(R.string.dob_placeholder),
                    onTextChange = { viewModel.policyExpirationDate = it },
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