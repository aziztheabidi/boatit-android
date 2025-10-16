package com.boatit.boatsharing.ui.captain.dashbaord.view

import VoyageData
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.CHAT_SCREEN
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageRequest
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.uihelpers.SessionDialog
import com.boatit.boatsharing.utils.AppConstants

@Composable
fun CaptainVoyageDetails(navController: NavController, notification : VoyageData, VoyageId: String?, CaptainName: String?, onDeclineClick: () -> Unit, onAcceptClick: (String) -> Unit) {

    val context = LocalContext.current
    val enteredValues = remember { mutableStateListOf("", "", "","","") }
    val focusManager = LocalFocusManager.current
    val focusRequesters = remember { List(5) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var pickupNotes by remember { mutableStateOf("") }
    var hasNavigated by remember { mutableStateOf(false) }

    var showDialogForCancel by remember { mutableStateOf(false) }

    var showDialogForOTP by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.height(screenHeight * 0.65f),
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
        ) {
            Spacer(Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(
                    text = """Voyage from ${notification.PickupDock} to ${notification.DropOffDock}""",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )


                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Text(
                    text = stringResource(R.string.enter_pin_text),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    enteredValues.forEachIndexed { index, value ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, colorResource(R.color.button_normal), RoundedCornerShape(8.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomTextField(
                                textValue = value,
                                placeholderText = "",
                                onTextChange = { input ->
                                    if (input.length <= 1 && input.all { it.isDigit() }) {
                                        enteredValues[index] = input
                                        if (input.isNotEmpty() && index < enteredValues.lastIndex) {
                                            focusRequesters[index + 1].requestFocus()
                                        }
                                    }
                                },
                                keyboardType = KeyboardType.Number,
                                maxChars = 1,
                                errorMessage = null,
                                isError = false,
                                onClearError = {},
                                imeAction = if (index == enteredValues.lastIndex) ImeAction.Done else ImeAction.Next,
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        if (index < enteredValues.lastIndex) {
                                            focusRequesters[index + 1].requestFocus()
                                        }
                                    },
                                    onDone = {
                                        focusManager.clearFocus()
                                    }
                                ),
                                showTrailingIcon = false,
                                focusRequester = focusRequesters[index],
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = notification.VoyagerName .firstOrNull()?.uppercase() ?: "",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }


                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = notification.VoyagerName ,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.payment_done), // Verified icon
                                contentDescription = "Verified",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "$" + notification.AmountToPay, color = Color.Gray, fontSize = 12.sp)
                        }


                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = pickupNotes,
                    onValueChange = {
                        pickupNotes = it

                        if (!hasNavigated && it.isNotBlank()) {
                            hasNavigated = true
                            navController.navigate(route = "$CHAT_SCREEN/${VoyageId}/${AppConstants.USER_ID}/${notification.VoyagerName}/${CaptainName}")

                        }
                    },
                    placeholder = {
                        Text(
                            "Connect with Voyager?",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.button_normal),
                        unfocusedBorderColor = colorResource(R.color.button_normal),
                        unfocusedTextColor = Color.Gray,
                        errorLabelColor = Color.Red
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 0.dp) // Padding for the row
                ) {

//                    IconButton(onClick = {
//                            val intent = Intent(Intent.ACTION_DIAL).apply {
//                            data = Uri.parse("tel:1228388383")
//                        }
//                        context.startActivity(intent) }) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.call_icon),
//                            contentDescription = "Call",
//                            tint = Color.Unspecified,
//                            modifier = Modifier.size(50.dp)
//                        )
//                    }

//                    IconButton(onClick = {
//                        navController.navigate(route = "$CHAT_SCREEN/${VoyageId}/${AppConstants.USER_ID}/${VoyagerName}/${CaptainName}")
//                    }) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.message_icon),
//                            contentDescription = "Message",
//                            tint = Color.Unspecified,  modifier = Modifier.size(50.dp)
//                        )
//                    }
//
//                    IconButton(onClick = {
//                        val intent = Intent(Intent.ACTION_SEND).apply {
//                            type = "text/plain"
//                            putExtra(Intent.EXTRA_TEXT, "Boating App")
//                        }
//                        context.startActivity(Intent.createChooser(intent, "Share via"))
//                    }) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.share_icon),
//                            contentDescription = "Share",
//                            tint = Color.Unspecified,  modifier = Modifier.size(50.dp)
//                        )
//                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {

                            showDialogForCancel=true

                        },
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
                            text = stringResource(R.string.decline_text),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = R.color.button_normal) // Text color matches border
                        )
                    }

                    Button(
                        onClick = {
                            val otp = enteredValues[0] + enteredValues[1] + enteredValues[2] + enteredValues[3] + enteredValues[4]
                            if (enteredValues.all { it.isNotEmpty() } && otp.length == 5) {
                                onAcceptClick(otp)
                            }
                            else{
                                showDialogForOTP=true
                            }

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
                            text = stringResource(R.string.start_text),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Terms & Conditions.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                if(showDialogForCancel){

                    SessionDialog(
                        text = "Are you sure, you want to decline voyage",
                        onCancel = {
                            showDialogForCancel = false
                        },
                        onPressOk = {
                            showDialogForCancel = false
                             onDeclineClick()
                        },
                        showCancelButton = true
                    )
                }



                if(showDialogForOTP){

                    SessionDialog(
                        text = "Enter PIN for voyage to start",
                        onCancel = {
                            showDialogForOTP = false
                        },
                        onPressOk = {
                            showDialogForOTP = false
                        },
                        showCancelButton = true
                    )
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.wheel_icon),
            contentDescription = "Floating Icon",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(90.dp)
            .clickable { navController.navigate(NavigationManager.MENU_OPTIONS_SCREEN) }
        )
    }
}
