package com.boatit.boatsharing.ui.business.view


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.boatit.boatsharing.ui.business.model.BusinessData
import com.boatit.boatsharing.ui.business.viewmodel.GetBusinessViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ActiveVoyageDetails
import com.boatit.boatsharing.ui.voyager.dashbaord.model.CancelBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ConfirmBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.NearByVoyagesViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDropDown
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun BusinessDashboard(navController: NavController,
                      viewModel: GetBusinessViewModel = koinViewModel(),) {

    val focusManager = LocalFocusManager.current
    val businessDescriptionFocusRequester = remember { FocusRequester() }
    val options = listOf("Yes", "No")
    var BDetail by remember { mutableStateOf<BusinessData?>(null) }
    var selectedOption by remember { mutableStateOf("") }
    var businessDescription by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    val isValidate = businessDescription.isNotEmpty()&&selectedOption.isNotEmpty()
    val fetchState by viewModel.loginState.collectAsState()

    when (fetchState) {
        is NetworkResponse.Success -> {
            BDetail = fetchState.data?.obj
        }
        is NetworkResponse.Error -> {}
        else -> {}
    }

    val handleError = {
        errorMessage = null
        isError = false
    }

    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Scaffold(
        topBar = {

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

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(100.dp)
                        .padding(start = 20.dp, top = 40.dp),
                    contentAlignment = Alignment.TopStart,
                )  {

                    Image(
                        painter = painterResource(id = R.drawable.wheel_icon),
                        contentDescription = "Icon Image",
                        modifier = Modifier
                            .size(width = 80.dp, height = 80.dp)
                            .clickable {
                                navController.navigate(NavigationManager.CAPTAIN_MENU_OPTIONS_SCREEN)
                            }
                    )

                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .width(70.dp)
                            .height(70.dp)
                            .padding(3.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_sponsor),
                                contentDescription = "Icon",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {},
                                tint = colorResource(R.color.button_normal)
                            )

                        }
                    }

                    Text(
                        style = TextStyle(
                            color = Color.Blue,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        text = "Business Name"
                    )

                    Text(
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        text = stringResource(R.string.business_dock_label)
                    )

                    Spacer(Modifier.height(30.dp))

                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp), // Corner radius
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray, // Border color
                                shape = RoundedCornerShape(10.dp) // Apply same corner radius to border
                            ),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "Date of Establishment",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = colorResource(id = R.color.black) // Text color matches border
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        text = stringResource(R.string.business_dock_label)
                    )

                    Spacer(Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(
                                    id = R.color.button_normal
                                )
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.followed),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp), // Corner radius
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.button_normal), // Border color
                                    shape = RoundedCornerShape(10.dp) // Apply same corner radius to border
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text(
                                text = stringResource(R.string.add_to_yoyage),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = colorResource(id = R.color.button_normal) // Text color matches border
                            )
                        }

                    }
                }

                Spacer(Modifier.height(30.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = "Gallery"
                )

                Spacer(Modifier.height(20.dp))

                Column {
                    Row {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .padding(3.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_sponsor),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    tint = colorResource(R.color.button_normal)
                                )

                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .padding(3.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_sponsor),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    tint = colorResource(R.color.button_normal)
                                )

                            }
                        }


                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .padding(3.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_sponsor),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    tint = colorResource(R.color.button_normal)
                                )

                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Row {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .padding(3.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_sponsor),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    tint = colorResource(R.color.button_normal)
                                )

                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .padding(3.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_sponsor),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    tint = colorResource(R.color.button_normal)
                                )

                            }
                        }


                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .padding(3.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_sponsor),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    tint = colorResource(R.color.button_normal)
                                )

                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Row {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .padding(3.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_sponsor),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    tint = colorResource(R.color.button_normal)
                                )

                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .padding(3.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_sponsor),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    tint = colorResource(R.color.button_normal)
                                )

                            }
                        }


                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .padding(3.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_sponsor),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {},
                                    tint = colorResource(R.color.button_normal)
                                )

                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = "Location"
                )

                Spacer(Modifier.height(10.dp))

                CustomDropDown(
                    options = options,
                    selectedOption = selectedOption,
                    onOptionSelected = { selectedOption = it },
                    placeholderText = "Location",
                    isError = false
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = "Business Hours"
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = businessDescription,
                    placeholderText = "Timings",
                    onTextChange = { businessDescription = it },
                    keyboardType = KeyboardType.Text,
                    maxChars = 1000,
                    singleLine = false,
                    minLines = 20,
                    errorMessage = if (businessDescription.isNotEmpty() && businessDescription.length <= 3) stringResource(
                        R.string.business_description_validation_text
                    ) else null,
                    isError = businessDescription.isNotEmpty() && businessDescription.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Done,
                    showTrailingIcon = false,
                    keyboardActions = KeyboardActions(
                        // onNext = { businessTypeFocusRequester.requestFocus() }
                    ),
                    focusRequester = businessDescriptionFocusRequester
                )

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = "Save Changes",
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
fun PreviewBusinessDashboard() {
    BusinessDashboard(navController = rememberNavController())
}