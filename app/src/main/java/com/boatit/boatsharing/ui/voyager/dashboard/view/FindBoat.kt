package com.boatit.boatsharing.ui.voyager.dashboard.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.getDate

@SuppressLint("UnrememberedMutableState")
@Composable
fun FindBoat(navController: NavController,
             modifier: Modifier, pickupLocation:String,
             dropOffLocation:String, totalPassengers:String,
             onCancelClick: () -> Unit,
             onFindBoatClick: () -> Unit) {

    val showDialog = mutableStateOf(false)
    var pLocation by remember { mutableStateOf(pickupLocation) }
    var dLocation by remember { mutableStateOf(dropOffLocation) }
    var noOffPassengers by remember { mutableStateOf(totalPassengers) }

    var bookingDate by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val handleError = { errorMessage = null
        isError = false
    }

    bookingDate = getDate()


    Box(

        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        // Main Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 40.dp),
            shape = RoundedCornerShape(
                topStart = 45.dp,
                topEnd = 45.dp
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        )  {
            Spacer(Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Text label on the left
                Text(
                    text = "Please confirm your details before booking",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                )

                Card(
                    modifier = Modifier
                        .width(90.dp)
                        .height(60.dp)
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

                        if (showDialog.value) {
                            MyDatePickerDialog(
                                onDateSelected = { bookingDate = it },
                                onDismiss = { showDialog.value = false }
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.event_calender),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    showDialog.value = true
                                },
                            tint = colorResource(R.color.button_normal)
                        )

                        Text(
                            text = "Create Event",
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(

                        start = 20.dp,
                        end = 20.dp,
                        bottom = 25.dp,
                    )
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        text = stringResource(R.string.booking_date)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        text = bookingDate
                    )
                }

                Spacer(Modifier.height(10.dp))
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.pickup_location_lbl)
                )

                Spacer(Modifier.height(10.dp))


                CustomTextField(
                    textValue = pickupLocation,
                    placeholderText = stringResource(R.string.pickup_location_lbl),
                    onTextChange = { pLocation = it },
                    keyboardType = KeyboardType.Email,
                    maxChars = 100,
                    errorMessage = if (pickupLocation.isNotEmpty()&& pickupLocation.length <= 3) stringResource(
                        R.string.pickup_location_text) else null,
                    isError = pickupLocation.isNotEmpty()&& pickupLocation.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.location_icon),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = colorResource(R.color.button_normal)
                        )
                    }

                )

                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.drop_off_location_lbl)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = dropOffLocation,
                    placeholderText = stringResource(R.string.drop_off_location_lbl),
                    onTextChange = { dLocation = it },
                    keyboardType = KeyboardType.Email,
                    maxChars = 100,
                    errorMessage = if (dropOffLocation.isNotEmpty()&&dropOffLocation.length <= 3) stringResource(R.string.drop_off_location_text) else null,
                    isError = dropOffLocation.isNotEmpty()&&dropOffLocation.length <= 3,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.drop_off_loc_icon),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }

                )


                Spacer(Modifier.height(15.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.num_off_voyagers_lbl)
                )

                Spacer(Modifier.height(10.dp))

                CustomTextField(
                    textValue = noOffPassengers,
                    placeholderText = stringResource(R.string.num_off_voyagers_lbl),
                    onTextChange = { noOffPassengers = it },
                    keyboardType = KeyboardType.Number,
                    errorMessage = if (totalPassengers.isNotEmpty()&&totalPassengers.length <= 1) stringResource(
                        R.string.num_off_voyagers_text) else null,
                    isError = totalPassengers.isNotEmpty()&&totalPassengers.length <= 1,
                    onClearError = handleError,
                    imeAction = ImeAction.Next,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.passengers),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = colorResource(R.color.button_normal)
                        )
                    }

                )


                Spacer(modifier = Modifier.height(16.dp))

                Spacer(Modifier.height(15.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds spacing between buttons
                ) {
                    Button(
                        onClick = {
                            onFindBoatClick()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 1.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal))
                    ) {
                        Text(
                            text = stringResource(R.string.find_boat_button_text),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = { onCancelClick() },
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
                            text = "Cancel",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = R.color.button_normal) // Text color matches border
                        )
                    }

                }
            }

        }

        Image(
            painter = painterResource(id = R.drawable.wheel_icon),
            contentDescription = "Floating Icon",
            contentScale = ContentScale.FillBounds,

            modifier = Modifier
                .size(90.dp)
                .clickable { navController.navigate(NavigationManager.MENU_OPTIONS_SCREEN) }
        )
    }



}


@Preview
@Composable
fun PreviewFindBoat() {
    FindBoat(
        navController = rememberNavController(),
        modifier = Modifier, pickupLocation = "", dropOffLocation = "",
        totalPassengers = "",
        onCancelClick = {},
        onFindBoatClick = {}
    )
}