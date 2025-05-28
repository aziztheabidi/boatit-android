package com.boatit.boatsharing.ui.signup.captain.view

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.ui.signup.captain.viewmodel.CaptainProfileViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.GetCaptainProfileViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDobField
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar


@SuppressLint("UnrememberedMutableState")
@Composable
fun CaptainAccountInfoScreen(
    navController: NavController,
    viewModel: CaptainProfileViewModel = koinViewModel(),
    viewModelfeth: GetCaptainProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val registrationState by viewModel.registrationState.collectAsState()
    val fetchState by viewModelfeth.registrationState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val isEmailValid = uiState.stripeEmail.contains("@") && uiState.stripeEmail.contains(".")
    val isValid = uiState.firstName.isNotBlank() &&
            uiState.lastName.isNotBlank() &&
            uiState.phoneNumber.isNotBlank() &&
            uiState.address.isNotBlank() &&
            uiState.dateOfBirth.isNotBlank() &&
            isEmailValid

    if (uiState.isSuccess) {
        Toast.makeText(context, "Profile saved", Toast.LENGTH_SHORT).show()
        viewModel.resetSuccessState()
        navController.navigate(NavigationManager.CAPTAIN_DOCUMENT_INFO_SCREEN)
    }

    if (uiState.errorMessage != null) {
        Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
    }

    if (uiState.showDateDialog) {
        MyDatePickerDialog(
            onDateSelected = {
                viewModel.updateDateOfBirth(it)
            },
            onDismiss = { viewModel.toggleDatePicker(false) }
        )
    }

    LaunchedEffect(Unit) {
        viewModelfeth.GetCaptainProfile()
    }

    when (fetchState) {
        is NetworkResponse.Success -> {
            val data = fetchState.data?.obj
            LaunchedEffect(Unit) {
                viewModel.onFieldChange {
                    it.copy(
                        phoneNumber = data?.PhoneNumber.orEmpty(),
                        firstName = data?.FirstName.orEmpty(),
                        lastName = data?.LastName.orEmpty(),
                        address = data?.Address.orEmpty(),
                        dateOfBirth = data?.DateOfBirth.orEmpty(),
                        stripeEmail = data?.StripeEmail.orEmpty()
                    )
                }
            }
        }

        is NetworkResponse.Error -> Unit
        else -> Unit
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                text = "${stringResource(R.string.add_your_info)} 1/3",
                onImageClick = {}
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding() + 15.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = innerPadding.calculateTopPadding() + 25.dp
                    )
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                FormStepsViews(
                    numberOfViews = 1,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 1
                )

                // -- Example for first field only, you can replicate similarly for others --

                Text(
                    text = stringResource(R.string.firstname_label),
                    style = TextStyle(color = Color.Black, fontSize = 18.sp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = uiState.firstName,
                    placeholderText = stringResource(R.string.firstname_placeholder),
                    onTextChange = { viewModel.onFieldChange { it.copy(firstName = it.toString()) } },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (uiState.firstName.isNotEmpty() && uiState.firstName.length <= 3) stringResource(R.string.firstname_validation_text) else null,
                    isError = uiState.firstName.isNotEmpty() && uiState.firstName.length <= 3,
                    onClearError = { viewModel.onFieldChange { it.copy(errorMessage = null) } },
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { lastNameFocusRequester.requestFocus() }
                    ),
                    focusRequester = firstNameFocusRequester
                )

                // 👇 replicate the same pattern for lastName, phoneNumber, address, dob, stripeEmail...

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = stringResource(R.string.save_button_label),
                    isValidate = isValid,
                    isLoading = uiState.isLoading,
                    onButtonClick = {
                        viewModel.saveProfile(AppConstants.USER_ID.toString())
                        focusManager.clearFocus()
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(onDateSelected: (String) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDateMillis = datePickerState.selectedDateMillis
    val selectedDate = selectedDateMillis?.let {
        val calendar = Calendar.getInstance().apply { timeInMillis = it }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Months are 0-based, so add 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        String.format("%04d-%02d-%02d", year, day, month)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate.toString())
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Preview
@Composable
fun PreviewVoyagerAccountInfo() {
    CaptainAccountInfoScreen(navController = rememberNavController())
}