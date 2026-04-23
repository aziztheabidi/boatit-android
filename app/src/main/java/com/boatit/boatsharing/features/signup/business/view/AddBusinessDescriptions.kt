package com.boatit.boatsharing.features.signup.business

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.navigation.popBack
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessAboutRequest
import com.boatit.boatsharing.features.signup.business.viewmodel.BusinessAboutUiEffect
import com.boatit.boatsharing.features.signup.business.viewmodel.BusinessAboutUiEvent
import com.boatit.boatsharing.features.signup.business.viewmodel.BusinessAboutViewModel
import com.boatit.boatsharing.features.signup.business.viewmodel.GetBusinessInfoViewModel
import com.boatit.boatsharing.ui.components.CustomButton
import com.boatit.boatsharing.ui.components.CustomDropDown
import com.boatit.boatsharing.ui.components.CustomTextField
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.FormStepsViews
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@Composable
fun AddBusinessDescriptions(
    navController: NavController,
    viewModelfetch: GetBusinessInfoViewModel = koinViewModel(),
    viewModel: BusinessAboutViewModel = koinViewModel(),
) {
    val focusManager = LocalFocusManager.current

    val businessDescriptionFocusRequester = remember { FocusRequester() }
    val options = listOf("Yes", "No")
    var selectedOptionBolean by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Yes") }
    var businessDescription by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userSessionStore: UserSessionStore = get(UserSessionStore::class.java)
    val currentUserId = userSessionStore.currentUserId()
    var getingData by remember { mutableStateOf(true) }
    val fetchVm by viewModelfetch.uiState.collectAsState()
    val fetchState = fetchVm.registrationState
    val aboutUi by viewModel.uiState.collectAsState()
    val isValidate = businessDescription.isNotEmpty() && selectedOption.isNotEmpty()
    val showSaveProgress =
        aboutUi.isSaving && aboutUi.registrationState is NetworkResponse.Loading

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is BusinessAboutUiEffect.ShowSuccessToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is BusinessAboutUiEffect.ShowErrorToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                BusinessAboutUiEffect.NavigateToBusinessLogo -> {
                    navController.navigate(NavigationManager.BUSINESS_LOGO_SCREEN)
                }
            }
        }
    }

    LaunchedEffect(fetchState) {
        if (fetchState is NetworkResponse.Success && getingData) {
            businessDescription = fetchState.data?.obj?.Description.orEmpty()
            selectedOptionBolean = fetchState.data?.obj?.IsDock == true
            if (selectedOptionBolean) {
                selectedOption = "Yes"
            } else {
                selectedOption = "No"
            }
            getingData = false
        }
    }

    LaunchedEffect(getingData) {
        if (getingData) viewModelfetch.GetBusinessProfile()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(
                text = stringResource(R.string.add_your_business_info) + " 3/4",
                onImageClick = {
                    navController.popBack()
                },
            )
        },
        content = { innerPadding ->
            if (showSaveProgress) {
                Dialog(
                    onDismissRequest = {},
                    properties =
                        DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false,
                        ),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier =
                            Modifier
                                .size(100.dp)
                                .background(White, shape = RoundedCornerShape(8.dp)),
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
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
                        numberOfViews = 4,
                        activeColor = colorResource(id = R.color.button_normal),
                        inactiveColor = Color.Gray,
                        activeViewsCount = 3,
                    )

                    Spacer(Modifier.height(30.dp))
                    Text(
                        style =
                            TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                            ),
                        text = stringResource(R.string.business_description_label),
                    )

                    Spacer(Modifier.height(10.dp))

                    CustomTextField(
                        textValue = businessDescription,
                        placeholderText = stringResource(R.string.business_description_placeholder),
                        onTextChange = { businessDescription = it },
                        keyboardType = KeyboardType.Text,
                        maxChars = 1000,
                        singleLine = false,
                        minLines = 20,
                        errorMessage =
                            if (businessDescription.isNotEmpty() && businessDescription.length <= 3) {
                                stringResource(
                                    R.string.business_description_validation_text,
                                )
                            } else {
                                null
                            },
                        isError = businessDescription.isNotEmpty() && businessDescription.length <= 3,
                        onClearError = {},
                        imeAction = ImeAction.Done,
                        showTrailingIcon = false,
                        keyboardActions =
                            KeyboardActions(
                                // onNext = { businessTypeFocusRequester.requestFocus() }
                            ),
                        focusRequester = businessDescriptionFocusRequester,
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        style =
                            TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                            ),
                        text = stringResource(R.string.business_dock_label),
                    )

                    Spacer(Modifier.height(10.dp))

                    CustomDropDown(
                        options = options,
                        selectedOption = selectedOption,
                        onOptionSelected = {
                            selectedOption = it
                            if (selectedOption.equals("Yes")) {
                                selectedOptionBolean = true
                            } else {
                                selectedOptionBolean = false
                            }
                        },
                        placeholderText = stringResource(R.string.business_dock_placeholder),
                        isError = false,
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    CustomButton(
                        text = stringResource(R.string.save_button_label),
                        isValidate = isValidate,
                        isLoading = showSaveProgress,
                        onButtonClick = {
                            viewModel.onEvent(
                                BusinessAboutUiEvent.Save(
                                    SaveBusinessAboutRequest(
                                        currentUserId,
                                        Description = businessDescription,
                                        IsDock = selectedOptionBolean,
                                    ),
                                ),
                            )
                            focusManager.clearFocus()
                        },
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        },
    )
}

@Preview
@Composable
fun PreviewAddBusinessDescriptions() {
    AddBusinessDescriptions(navController = rememberNavController())
}
