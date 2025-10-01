package com.boatit.boatsharing.ui.signup.captain


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusDirection
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
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.ui.signup.captain.viewmodel.CaptainBoatViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.CaptainProfileViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.GetCaptainBoatViewModel
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddCaptainBoatInfoScreen(
    navController: NavController,
    viewModel: CaptainBoatViewModel = koinViewModel(),
    viewModelfetch: GetCaptainBoatViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var gettingData by remember { mutableStateOf(true) }
    val fetchState by viewModelfetch.registrationState.collectAsState()
    val registrationState by viewModel.registrationState.collectAsState()

    when (fetchState) {
        is NetworkResponse.Success -> {
            if(gettingData) {
                viewModel.loadInitialData(fetchState.data)
                gettingData = false
            }
        }
        is NetworkResponse.Error -> {
            gettingData = false
        }
        else -> {}
    }

    LaunchedEffect(gettingData) {
        viewModelfetch.GetCaptainBoat()
    }

    LaunchedEffect(registrationState) {
        if (viewModel.isButtonClicked) {
            when (val state = registrationState) {
                is NetworkResponse.Success -> {
                    Toast.makeText(context, state.data?.Message ?: "Saved", Toast.LENGTH_SHORT).show()
                    viewModel.onRegistrationHandled()
                    navController.navigateWithClearStack(NavigationManager.CAPTAIN_OFFLINE_SCREEN, clearStack = true)

                }

                is NetworkResponse.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    viewModel.onRegistrationHandled()
                }

                else -> {}
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = stringResource(R.string.add_your_boat_info) + " 3/3") {
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
            FormStepsViews(
                numberOfViews = 3,
                activeColor = colorResource(id = R.color.button_normal),
                inactiveColor = Color.Gray,
                activeViewsCount = 3
            )

            Spacer(Modifier.height(30.dp))

            listOf(
                Triple(R.string.boat_name_label, R.string.boat_name_placeholder, viewModel.boatName),
                Triple(R.string.boat_make_label, R.string.boat_make_placeholder, viewModel.boatMake),
                Triple(R.string.boat_model_label, R.string.boat_model_placeholder, viewModel.boatModel),
                Triple(R.string.boat_year_label, R.string.boat_year_placeholder, viewModel.boatYear),
                Triple(R.string.boat_size_label, R.string.boat_size_placeholder, viewModel.boatSize),
                Triple(R.string.boat_capacity_label, R.string.boat_capacity_placeholder, viewModel.boatCapacity)
            ).forEachIndexed { index, (labelRes, placeholderRes, value) ->
                Text(
                    text = stringResource(id = labelRes),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal, color = Color.Black)
                )
                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = value,
                    placeholderText = stringResource(id = placeholderRes),
                    onTextChange = {
                        when (index) {
                            0 -> viewModel.boatName = it
                            1 -> viewModel.boatMake = it
                            2 -> viewModel.boatModel = it
                            3 -> viewModel.boatYear = it
                            4 -> viewModel.boatSize = it
                            5 -> viewModel.boatCapacity = it
                        }
                    },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = viewModel::onClearError,
                    imeAction = if (index == 5) ImeAction.Done else ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(
                        FocusDirection.Down) })
                )
                Spacer(Modifier.height(20.dp))
            }

            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = stringResource(R.string.save_button_label),
                isValidate = viewModel.isValidate,
                isLoading = viewModel.isLoading,
                onButtonClick = {
                    focusManager.clearFocus()
                    viewModel.isButtonClicked = true
                    viewModel.saveProfile()
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewCaptainBoatInfo() {
    AddCaptainBoatInfoScreen(navController = rememberNavController())
}