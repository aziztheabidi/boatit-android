package com.boatit.boatsharing.features.signup.captain

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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
import com.boatit.boatsharing.ui.navigation.navigateWithClearStack
import com.boatit.boatsharing.features.signup.captain.viewmodel.CaptainBoatUiEvent
import com.boatit.boatsharing.features.signup.captain.viewmodel.CaptainBoatViewModel
import com.boatit.boatsharing.features.signup.captain.viewmodel.GetCaptainBoatViewModel
import com.boatit.boatsharing.ui.components.CustomButton
import com.boatit.boatsharing.ui.components.CustomTextField
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.FormStepsViews
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddCaptainBoatInfoScreen(
    navController: NavController,
    viewModel: CaptainBoatViewModel = koinViewModel(),
    viewModelfetch: GetCaptainBoatViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var getingData by remember { mutableStateOf(true) }
    val boatUi by viewModel.uiState.collectAsState()
    val registrationState = boatUi.registrationState
    val fetchVm by viewModelfetch.uiState.collectAsState()
    val fetchState = fetchVm.registrationState

    LaunchedEffect(fetchState) {
        when (fetchState) {
            is NetworkResponse.Success -> {
                if (getingData) {
                    viewModel.onEvent(CaptainBoatUiEvent.LoadInitial(fetchState.data))
                    getingData = false
                }
            }
            is NetworkResponse.Error -> {
                getingData = false
            }
            else -> Unit
        }
    }

    LaunchedEffect(getingData) {
        if (getingData) viewModelfetch.GetCaptainBoat()
    }

    LaunchedEffect(registrationState) {
        if (boatUi.isButtonClicked) {
            when (val state = registrationState) {
                is NetworkResponse.Success -> {
                    Toast.makeText(context, state.data?.Message ?: "Saved", Toast.LENGTH_SHORT).show()
                    viewModel.onEvent(CaptainBoatUiEvent.RegistrationHandled)
                    navController.navigateWithClearStack(NavigationManager.CAPTAIN_OFFLINE_SCREEN, clearStack = true)
                }

                is NetworkResponse.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    viewModel.onEvent(CaptainBoatUiEvent.RegistrationHandled)
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = stringResource(R.string.add_your_boat_info) + " 3/3") {
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
            FormStepsViews(
                numberOfViews = 3,
                activeColor = colorResource(id = R.color.button_normal),
                inactiveColor = Color.Gray,
                activeViewsCount = 3,
            )

            Spacer(Modifier.height(30.dp))

            listOf(
                Triple(R.string.boat_name_label, R.string.boat_name_placeholder, boatUi.boatName),
                Triple(R.string.boat_make_label, R.string.boat_make_placeholder, boatUi.boatMake),
                Triple(R.string.boat_model_label, R.string.boat_model_placeholder, boatUi.boatModel),
                Triple(R.string.boat_year_label, R.string.boat_year_placeholder, boatUi.boatYear),
                Triple(R.string.boat_size_label, R.string.boat_size_placeholder, boatUi.boatSize),
                Triple(R.string.boat_capacity_label, R.string.boat_capacity_placeholder, boatUi.boatCapacity),
            ).forEachIndexed { index, (labelRes, placeholderRes, value) ->
                Text(
                    text = stringResource(id = labelRes),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal, color = Color.Black),
                )
                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = value,
                    placeholderText = stringResource(id = placeholderRes),
                    onTextChange = {
                        when (index) {
                            0 -> viewModel.onEvent(CaptainBoatUiEvent.BoatNameChanged(it))
                            1 -> viewModel.onEvent(CaptainBoatUiEvent.BoatMakeChanged(it))
                            2 -> viewModel.onEvent(CaptainBoatUiEvent.BoatModelChanged(it))
                            3 -> viewModel.onEvent(CaptainBoatUiEvent.BoatYearChanged(it))
                            4 -> viewModel.onEvent(CaptainBoatUiEvent.BoatSizeChanged(it))
                            5 -> viewModel.onEvent(CaptainBoatUiEvent.BoatCapacityChanged(it))
                        }
                    },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = { viewModel.onEvent(CaptainBoatUiEvent.ClearError) },
                    imeAction = if (index == 5) ImeAction.Done else ImeAction.Next,
                    keyboardActions =
                        KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down,
                            )
                        }),
                )
                Spacer(Modifier.height(20.dp))
            }

            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = stringResource(R.string.save_button_label),
                isValidate = boatUi.isValidate,
                isLoading = boatUi.isLoading,
                onButtonClick = {
                    focusManager.clearFocus()
                    viewModel.onEvent(CaptainBoatUiEvent.SaveProfile)
                },
            )
        }
    }
}

@Preview
@Composable
fun PreviewCaptainBoatInfo() {
    AddCaptainBoatInfoScreen(navController = rememberNavController())
}
