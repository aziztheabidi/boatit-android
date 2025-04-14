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
fun AddCaptainBoatInfoScreen(navController: NavController, viewModel: CaptainBoatViewModel = koinViewModel(), viewModelfetch: GetCaptainBoatViewModel = koinViewModel()) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val boatNameFocusRequester = remember { FocusRequester() }
    val boatMakeExpiryDateFocusRequester = remember { FocusRequester() }
    val boatModelFocusRequester = remember { FocusRequester() }
    val boatYearFocusRequester = remember { FocusRequester() }
    val boatSizeFocusRequester = remember { FocusRequester() }
    val boatCapacityFocusRequester = remember { FocusRequester() }
    var boatName by remember { mutableStateOf("") }
    var boatMake by remember { mutableStateOf("") }
    var boatModel by remember { mutableStateOf("") }
    var boatYear by remember { mutableStateOf("") }
    var boatSize by remember { mutableStateOf("") }
    var boatCapacity by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var getingData by remember { mutableStateOf(true) }
    var isNetworkError by remember { mutableStateOf(false) }


    val isValidate = boatName.isNotEmpty()
            && boatMake.isNotEmpty()
            && boatModel.isNotEmpty()
            && boatYear.isNotEmpty()
            && boatSize.isNotEmpty()
            && boatCapacity.isNotEmpty()

    val handleError = {
        errorMessage = null
        isError = false
    }

    val registrationState by viewModel.registrationState.collectAsState()
    val fetchState by viewModelfetch.registrationState.collectAsState()

    fun performLogin(){
       navController.navigate(NavigationManager.CAPTAIN_OFFLINE_SCREEN)
    }

    when (registrationState) {
        is NetworkResponse.Success -> {
            if(isLoading){
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, registrationState.data?.Message , Toast.LENGTH_SHORT).show()
                performLogin()
            }

        }
        is NetworkResponse.Error -> {
            isLoading = false
            isNetworkError = true
            errorMessage = "Network error, please try again."
            Toast.makeText(context, (registrationState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    when (fetchState) {
        is NetworkResponse.Success -> {
            if(getingData) {
                boatName = fetchState.data?.obj?.Name.toString()
                boatMake = fetchState.data?.obj?.Make.toString()
                boatModel = fetchState.data?.obj?.Model.toString()
                boatYear = fetchState.data?.obj?.Year.toString()
                boatSize = fetchState.data?.obj?.Size.toString()
                getingData = false
            }
        }
        is NetworkResponse.Error -> {
            getingData = false
        }
        else -> {}
    }


    LaunchedEffect(getingData) {
        viewModelfetch.GetCaptainBoat()
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.add_your_boat_info)+ " 3/3", onImageClick = {
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
                    numberOfViews = 3,
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
                    text = stringResource(R.string.boat_name_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = boatName,
                    placeholderText = stringResource(R.string.boat_name_placeholder),
                    onTextChange = { boatName = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (boatName.isNotEmpty()&& boatName.length <= 5) stringResource(R.string.boat_name_validation_text) else null,
                    isError = boatName.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { boatMakeExpiryDateFocusRequester.requestFocus() }
                    ),
                    focusRequester = boatNameFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))



                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.boat_make_label)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = boatMake,
                    placeholderText = stringResource(R.string.boat_make_placeholder),
                    onTextChange = { boatMake = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = if (boatMake.isNotEmpty()&& boatMake.length <= 3) stringResource(R.string.boat_make_validation_text) else null,
                    isError = boatMake.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { boatModelFocusRequester.requestFocus() }
                    ),
                    focusRequester = boatMakeExpiryDateFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))



                Text(
                    text = stringResource(R.string.boat_model_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = boatModel,
                    placeholderText = stringResource(R.string.boat_model_placeholder),
                    onTextChange = { boatModel = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 15,
                    errorMessage = if (boatModel.isNotEmpty() && boatModel.length <= 3) stringResource(
                        R.string.boat_model_validation_text
                    ) else null,
                    isError = boatModel.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { boatYearFocusRequester.requestFocus() }
                    ),
                    focusRequester = boatModelFocusRequester
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.boat_year_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = boatYear,
                    placeholderText = stringResource(R.string.boat_year_placeholder),
                    onTextChange = { boatYear = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 50,
                    errorMessage = if (boatYear.isNotEmpty() && boatYear.length <= 1) stringResource(R.string.boat_year_validation_text) else null,
                    isError = boatYear.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { boatSizeFocusRequester.requestFocus() }
                    ),
                    focusRequester = boatYearFocusRequester
                )


                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.boat_size_label),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    textValue = boatSize,
                    placeholderText = stringResource(R.string.boat_size_placeholder),
                    onTextChange = { boatSize = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 40,
                    errorMessage = if (boatSize.isNotEmpty() && boatSize.length <= 3) stringResource(R.string.boat_size_validation_text) else null,
                    isError = boatSize.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = { boatCapacityFocusRequester.requestFocus() }
                    ),
                    focusRequester = boatSizeFocusRequester
                )


                Spacer(Modifier.height(30.dp))
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.boat_capacity_label)
                )

                Spacer(Modifier.height(10.dp))


                CustomTextField(
                    textValue = boatCapacity,
                    placeholderText = stringResource(R.string.boat_capacity_placeholder),
                    onTextChange = { boatCapacity = it },
                    keyboardType = KeyboardType.Email,
                    maxChars = 100,
                    errorMessage = if  (boatCapacity.isNotEmpty() && boatCapacity.length <= 3)stringResource(R.string.boat_capacity_validation_text) else null,
                    isError =  boatCapacity.isNotEmpty(),
                    onClearError = handleError,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    ),
                    focusRequester = boatCapacityFocusRequester
                )



                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = stringResource(R.string.save_button_label),
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {
                        viewModel.saveProfile(SaveCaptainBoatRequest(
                            UserId =  AppConstants.USER_ID.toString(),
                            Name = boatName,
                            Make = boatMake,
                            Model = boatModel,
                            Year = boatYear.toInt(),
                            Size = boatSize.toInt(),
                            Capacity = boatCapacity.toInt()
                         )
                        )
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
fun PreviewCaptainBoatInfo() {
    AddCaptainBoatInfoScreen(navController = rememberNavController())
}