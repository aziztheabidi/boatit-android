package com.boatit.boatsharing.ui.voyager.dashbaord.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
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
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.uihelpers.CustomDobField
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.SessionDialog
import com.boatit.boatsharing.uihelpers.getDate
import com.boatit.boatsharing.utils.AppConstants

@SuppressLint("UnrememberedMutableState")
@Composable
fun FindBoat(navController: NavController,
             modifier: Modifier, pickupLocation:String,
             dropOffLocation:String, totalPassengers:String,
             onCancelClick: () -> Unit,
             onFindBoatClick: () -> Unit) {

    var showDialog by remember { mutableStateOf(false) }

    var pLocation by remember { mutableStateOf(pickupLocation) }
    var dLocation by remember { mutableStateOf(dropOffLocation) }
    var category by remember { mutableStateOf("") }
    var noOffPassengers by remember { mutableStateOf(totalPassengers) }
    var bookingDate by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var expandedp by remember { mutableStateOf(false) }
    var expandedd by remember { mutableStateOf(false) }



    val handleError = { errorMessage = null
        isError = false
    }

    bookingDate = getDate()

    LaunchedEffect(Unit) {
        if(AppConstants.BusinessDock!!){
            AppConstants.Busines_DOCK = false;
            if(AppConstants.BusinessDockTYpe.equals("Pick")){
                pLocation = AppConstants.Pick_Up_Loc?.second!!
            }else{
                dLocation = AppConstants.Drop_Off_Loc?.second!!
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
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
//
//                Card(
//                    modifier = Modifier
//                        .width(90.dp)
//                        .height(60.dp)
//                        .padding(3.dp),
//                    shape = RoundedCornerShape(8.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//                    colors = CardDefaults.cardColors(containerColor = Color.White)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(0.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//
//                        if (showDialog.value) {
//                            MyDatePickerDialog(
//                                onDateSelected = { bookingDate = it },
//                                onDismiss = { showDialog.value = false }
//                            )
//                        }
//                        Icon(
//                            painter = painterResource(id = R.drawable.event_calender),
//                            contentDescription = "Icon",
//                            modifier = Modifier
//                                .size(30.dp)
//                                .clickable {
//                                    showDialog.value = true
//                                },
//                            tint = colorResource(R.color.button_normal)
//                        )
//
//                        Text(
//                            text = "Create Event",
//                            style = TextStyle(
//                                fontSize = 10.sp,
//                                fontWeight = FontWeight.Normal,
//                                color = Color.Black
//                            ),
//                            modifier = Modifier.padding(top = 4.dp)
//                        )
//                    }
//                }
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
            )

            {

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
                    text = stringResource(R.string.categoy)
                )
                Spacer(Modifier.height(10.dp))

                Box( modifier = Modifier.clickable { expanded = true }){
                    CustomDobField(
                        textValue = category,
                        placeholderText = stringResource(R.string.categoy),
                        onTextChange = {category = it},
                        keyboardType = KeyboardType.Text,
                        maxChars = 100,
                        errorMessage = null,
                        isError = false, 
                        onClearError = handleError,
                        imeAction = ImeAction.Next,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.boat_icon),
                                contentDescription = "Icon",
                                modifier = Modifier.size(20.dp),
                                tint = colorResource(R.color.button_normal)
                            )
                        }

                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background( Color.White)
                            .padding(horizontal = 16.dp,vertical = 4.dp)
                    ) {
                        AppConstants.Cates.forEach { categories ->
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    category = categories.Name
                                    AppConstants.Cat_id = categories.Id
                                },

                                text = {
                                    Text(
                                        text = categories.Name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color.White
                                    )
                            )
                        }
                    }
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

                Box( modifier = Modifier.clickable { expandedp = true }){
                    CustomDobField(
                        textValue = pLocation,
                        placeholderText = stringResource(R.string.pickup_location_lbl),
                        onTextChange = { pLocation = it },
                        keyboardType = KeyboardType.Text,
                        maxChars = 100,
                        errorMessage = null,
                        isError = false,
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

                    DropdownMenu(
                        expanded = expandedp,
                        onDismissRequest = { expandedp = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background( Color.White)
                            .padding(horizontal = 16.dp,vertical = 4.dp)
                    ) {
                        AppConstants.PLACES.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    expandedp = false
                                    pLocation = category.Name
                                    AppConstants.Pick_Up_Loc = Pair(category.DockTypeId, category.Name)
                                },
                                text = {
                                    Text(
                                        text = category.Name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color.White
                                    )
                            )
                        }
                    }
                }

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

                Box( modifier = Modifier.clickable { expandedd = true }){
                    CustomDobField(
                        textValue = dLocation,
                        placeholderText = stringResource(R.string.drop_off_location_lbl),
                        onTextChange = { dLocation = it },
                        keyboardType = KeyboardType.Text,
                        maxChars = 100,
                        errorMessage = null,
                        isError = false,
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
                    DropdownMenu(
                        expanded = expandedd,
                        onDismissRequest = { expandedd = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background( Color.White)
                            .padding(horizontal = 26.dp,vertical = 5.dp)
                    ) {
                        AppConstants.PLACES.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    expandedd = false
                                    dLocation = category.Name
                                    AppConstants.Drop_Off_Loc = Pair(category.DockTypeId, category.Name)
                                },
                                text = {
                                    Text(
                                        text = category.Name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color.White
                                    )
                            )
                        }
                    }
                }

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
                    onTextChange = { input ->
                        noOffPassengers = input
                        AppConstants.No_Of_Voyagers = input.toIntOrNull() ?: 0

//                        noOffPassengers = it
//                        AppConstants.No_Of_Voyagers = noOffPassengers.toInt() ?: 0
                                   },
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
                            val categoryStr = category
                            val noOfPassengersStr = noOffPassengers

                            // Extract operator
                            val operator = when {
                                categoryStr.contains("<=") -> "<="
                                categoryStr.contains(">=") -> ">="
                                else -> null
                            }

                            // Extract number from category
                            val numberInCategory = categoryStr.filter { it.isDigit() }
                            val categoryInt = numberInCategory.toIntOrNull()
                            val noOfPassengersInt = noOfPassengersStr.toIntOrNull()

                            // Safely compare based on operator
                            if (operator != null && categoryInt != null && noOfPassengersInt != null) {
                                val isInvalid = when (operator) {
                                    "<=" -> noOfPassengersInt > categoryInt
                                    ">=" -> noOfPassengersInt < categoryInt
                                    else -> false
                                }

                                if (isInvalid) {
                                    showDialog = true
                                    return@Button
                                }
                            }
                            onFindBoatClick()
                        },
                        enabled = category.isNotEmpty() && noOffPassengers.isNotEmpty() && dLocation.isNotEmpty() && pLocation.isNotEmpty(),
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

            if(showDialog){

                SessionDialog(
                    text = "This selected category requires a different number of passengers. Please review the limits.",
                    onCancel = {},
                    onPressOk = {
                        showDialog = false
                    },
                    showCancelButton = false
                )}

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