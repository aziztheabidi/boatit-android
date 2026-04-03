package com.boatit.boatsharing.ui.voyager.dashboard.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.USER_ACCOUNT_INFO_SCREEN
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.voyager.dashboard.model.Place
import com.boatit.boatsharing.utils.AppConstants
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction

@Composable
fun FindDestinationLocationScreen(navController: NavController, onLocationSelected: (String) -> Unit) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val placesClient = remember { Places.createClient(context) }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    var destinationLocation by remember { mutableStateOf("") }
    var pickUpLocation by remember { mutableStateOf("") }

    var isPickUpLocationFocused by remember { mutableStateOf(false) }
    var isDestinationLocationFocused by remember { mutableStateOf(false) }
    var activeTextField by remember { mutableStateOf("pickupLocation") }

    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableStateOf(0)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {

        Card(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(Color.White),
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box( modifier = Modifier.clickable {
                        activeTextField = "pickupLocation"
                        isDropDownExpanded.value = true }){
                        OutlinedTextField(
                            value = pickUpLocation,
                            enabled = false,
                            label = if (!isPickUpLocationFocused && pickUpLocation.isEmpty()) {
                                {
                                    Text(
                                        text = stringResource(R.string.pickup_location_h1),
                                        fontSize = 12.sp,
                                        color = Color.Black,
                                        modifier = Modifier.padding(0.dp)
                                    )
                                }
                            } else null,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(end = 30.dp)
                                .onFocusChanged { focusState ->
                                    isPickUpLocationFocused = focusState.isFocused
                                    activeTextField = "pickupLocation"
                                },
                            textStyle = TextStyle(fontSize = 14.sp),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.current_marker),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            trailingIcon = {
                                if (pickUpLocation.isNotBlank()) {
                                    IconButton(onClick = {
                                        pickUpLocation = ""; predictions = emptyList()
                                        isDropDownExpanded.value = false
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear")
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                unfocusedTextColor = Color.DarkGray
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {


                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp),

                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        )
                    }


                    Box ( modifier = Modifier.clickable {
                            activeTextField = "destinationLocation"
                            isDropDownExpanded.value = true }) {
                        OutlinedTextField(
                            value = destinationLocation,
                            enabled = false,
                            onValueChange = {},
                            label = if (!isDestinationLocationFocused && destinationLocation.isEmpty()) {
                                {
                                    Text(
                                        text = stringResource(R.string.destination_location_h1),
                                        fontSize = 12.sp,
                                        color = Color.Black,
                                        modifier = Modifier.padding(0.dp)
                                    )
                                }
                            } else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(end = 30.dp)
                                .onFocusChanged { focusState ->
                                    isDestinationLocationFocused = focusState.isFocused
                                    activeTextField = "destinationLocation"
                                },
                            textStyle = TextStyle(fontSize = 14.sp),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.current_marker),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            trailingIcon = {
                                if (destinationLocation.isNotBlank()) {
                                    IconButton(onClick = {
                                        destinationLocation = ""; predictions = emptyList()
                                        isDropDownExpanded.value = false
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear")
                                    }


                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                unfocusedTextColor = Color.DarkGray
                            ),
                        )
                    }
                }

                Icon(
                    painter = painterResource(id = R.drawable.cicular_close_icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(65.dp)
                        .align(Alignment.CenterEnd)
                        .padding(0.dp)
                        .clickable() {
                            navController.popBack()
                        }
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        if(isDropDownExpanded.value){
            LazyColumn {
                items(AppConstants.PLACES) { prediction ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (activeTextField == "pickupLocation") {
                                    pickUpLocation = prediction.Name
                                } else {
                                    destinationLocation = prediction.Name
                                }
                                predictions = emptyList()
                                focusManager.clearFocus()
                                isDropDownExpanded.value = false
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically){
                                Icon(
                                    painter = painterResource(id = R.drawable.current_marker),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .padding(end = 5.dp)
                                )
                                Text(
                                    text = prediction.Name,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(0.dp)
                                )
                            }
                            Text(
                                text = prediction.Address,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(0.dp),
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }


AnimatedVisibility(modifier = Modifier.align(Alignment.BottomCenter),
    visible = pickUpLocation.isNotBlank() && destinationLocation.isNotBlank(),
    enter = fadeIn(),
    exit = fadeOut()
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(100.dp)
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter,

        ) {

        Column(

            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,

            ) {
            Button(
                onClick = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("result_key", "$pickUpLocation:$destinationLocation")
                    navController.popBackStack()
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal))
            ) {
                Text(
                    text = stringResource(R.string.next),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(30.dp))


        }
    }
}

        }
}


