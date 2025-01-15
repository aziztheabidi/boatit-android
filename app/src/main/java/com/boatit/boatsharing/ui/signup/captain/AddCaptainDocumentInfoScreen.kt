package com.boatit.boatsharing.ui.signup.captain

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
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
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import kotlinx.coroutines.delay

@Composable
fun AddCaptainDocumentInfoScreen(navController: NavController) {

    val focusManager = LocalFocusManager.current
    val licenseNoFocusRequester = remember { FocusRequester() }
    val licenseNoExpiryDateFocusRequester = remember { FocusRequester() }
    val licenseTypeFocusRequester = remember { FocusRequester() }
    val insuranceCompanyFocusRequester = remember { FocusRequester() }
    val policyNoFocusRequester = remember { FocusRequester() }
    val policyExpiryDateFocusRequester = remember { FocusRequester() }


    var licenseNo by remember { mutableStateOf("") }
    var licenseNoExpiryDate by remember { mutableStateOf("") }
    var licenseType by remember { mutableStateOf("") }
    var insuranceCompany by remember { mutableStateOf("") }
    var policyNo by remember { mutableStateOf("") }
    var policyExpirationDate by remember { mutableStateOf("") }




    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }


    val isValidate = licenseNo.isNotEmpty()
            && licenseNoExpiryDate.isNotEmpty()
            && licenseType.isNotEmpty()
            && insuranceCompany.isNotEmpty()
            && policyNo.isNotEmpty()
            && policyExpirationDate.isNotEmpty()

    val handleError = {
        errorMessage = null
        isError = false
    }


    suspend fun performLogin(): Boolean {
        delay(2000)
        return false
    }

    LaunchedEffect(isButtonEnabled) {
        if (isLoading) {
            val networkSuccess = performLogin()
            isLoading = false
            if (networkSuccess) {
                isNetworkError = false
                println("info added")


            } else {
                isNetworkError = true
                errorMessage = "Network error, please try again."


                navController.navigate(NavigationManager.CAPTAIN_BOAT_INFO_SCREEN)

            }
        }
    }
    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.add_your_document_info)+ " 2/3", onImageClick = {
                println("clicked...")
            })
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

                FormStepsViews(
                    numberOfViews = 3,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 2
                )

                Spacer(Modifier.height(30.dp))
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.license_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = licenseNo,
                    placeholderText = stringResource(R.string.license_placeholder),
                    onTextChange = { licenseNo = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (licenseNo.isNotEmpty()&& licenseNo.length <= 5) stringResource(R.string.license_validation_text) else null,
                    isError = licenseNo.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { licenseNoExpiryDateFocusRequester.requestFocus() }
                    ),
                    focusRequester = licenseNoFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))



                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.license_exp_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = licenseNoExpiryDate,
                    placeholderText = stringResource(R.string.license_exp_placeholder),
                    onTextChange = { licenseNoExpiryDate = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (licenseNoExpiryDate.isNotEmpty()&& licenseNoExpiryDate.length <= 3) stringResource(R.string.license_exp_validation_text) else null,
                    isError = licenseNoExpiryDate.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { licenseTypeFocusRequester.requestFocus() }
                    ),
                    focusRequester = licenseNoExpiryDateFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))



                Text(
                    text = stringResource(R.string.license_type_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = licenseType,
                    placeholderText = stringResource(R.string.license_type_placeholder),
                    onTextChange = { licenseType = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 15,
                    errorMessage = if (licenseType.isNotEmpty() && licenseType.length <= 3) stringResource(
                        R.string.license_type_validation_text
                    ) else null,
                    isError = licenseType.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { insuranceCompanyFocusRequester.requestFocus() }
                    ),
                    focusRequester = licenseTypeFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.insurance_company_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = insuranceCompany,
                    placeholderText = stringResource(R.string.insurance_company_placeholder),
                    onTextChange = { insuranceCompany = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    singleLine = false,
                    maxLines = 2,
                    errorMessage = if (insuranceCompany.isNotEmpty() && insuranceCompany.length <= 3) stringResource(R.string.insurance_company_validation_text) else null,
                    isError = insuranceCompany.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { policyNoFocusRequester.requestFocus() }
                    ),
                    focusRequester = insuranceCompanyFocusRequester
                )


                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.policy_number_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = policyNo,
                    placeholderText = stringResource(R.string.policy_number_placeholder),
                    onTextChange = { policyNo = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 40,
                    errorMessage = if (policyNo.isNotEmpty() && policyNo.length <= 3) stringResource(R.string.policy_number_validation_text) else null,
                    isError = policyNo.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { policyExpiryDateFocusRequester.requestFocus() }
                    ),
                    focusRequester = policyNoFocusRequester
                )


                Spacer(Modifier.height(20.dp))
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.policy_exp_label)
                )

                Spacer(Modifier.height(10.dp))


                CustomTextField(
                    textValue = policyExpirationDate,
                    placeholderText = stringResource(R.string.policy_exp_placeholder),
                    onTextChange = { policyExpirationDate = it },
                    keyboardType = KeyboardType.Email,
                    maxChars = 100,
                    errorMessage = if  (policyExpirationDate.isNotEmpty() && policyExpirationDate.length <= 3)stringResource(R.string.policy_exp_validation_text) else null,
                    isError =  policyExpirationDate.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    ),
                    focusRequester = policyExpiryDateFocusRequester
                )



                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = stringResource(R.string.save_button_label),
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {

                        isButtonEnabled = true
                        isLoading = true
                        focusManager.clearFocus()
                        println("perform network call")

                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

            }
        },

        )
}

@Preview
@Composable
fun PreviewCaptainDocumentInfo() {
    AddCaptainDocumentInfoScreen(navController = rememberNavController())
}