package com.boatit.boatsharing.ui.signup.business


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
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDropDown
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import kotlinx.coroutines.delay

@Composable
fun AddBusinessDescriptions(navController: NavController) {

    val focusManager = LocalFocusManager.current

    val businessDescriptionFocusRequester = remember { FocusRequester() }
    val options = listOf("Yes", "No")

    var selectedOption by remember { mutableStateOf("") }
    var businessDescription by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }


    val isValidate = businessDescription.isNotEmpty()&&selectedOption.isNotEmpty()

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
                navController.navigate(NavigationManager.BUSINESS_LOGO_SCREEN)

            }
        }
    }
    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.add_your_business_info)+ " 3/4", onImageClick = {
                println("clicked...")
                navController.popBack()
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
                    numberOfViews = 4,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 3
                )

                Spacer(Modifier.height(30.dp))
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.business_description_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = businessDescription,
                    placeholderText = stringResource(R.string.business_description_placeholder),
                    onTextChange = { businessDescription = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 1000,
                    singleLine = false,
                    minLines= 20,
                    errorMessage = if (businessDescription.isNotEmpty()&& businessDescription.length <= 3) stringResource(R.string.business_description_validation_text) else null,
                    isError = businessDescription.isNotEmpty() && businessDescription.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Done,
                    showTrailingIcon = false,
                    keyboardActions = KeyboardActions(
                       // onNext = { businessTypeFocusRequester.requestFocus() }
                    ),
                    focusRequester = businessDescriptionFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))


                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.business_dock_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomDropDown(
                    options = options,
                    selectedOption = selectedOption,
                    onOptionSelected = { selectedOption = it },
                    placeholderText = stringResource(R.string.business_dock_placeholder),
                    isError = false
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
fun PreviewAddBusinessDescriptions() {
    AddBusinessDescriptions(navController = rememberNavController())
}