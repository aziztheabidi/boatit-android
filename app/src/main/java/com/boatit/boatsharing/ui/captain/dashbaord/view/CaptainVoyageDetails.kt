package com.boatit.boatsharing.ui.captain.dashbaord.view

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
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.utils.AppConstants

@Composable
fun CaptainVoyageDetails(navController: NavController, CaptainName: String?,onDeclineClick: () -> Unit, onAcceptClick: (String) -> Unit) {

    val context = LocalContext.current
    val enteredValues = remember { mutableStateListOf("", "", "","","") }
    val focusManager = LocalFocusManager.current
    val focusRequesters = remember { List(5) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier = Modifier.height(screenHeight * 0.6f),
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
                                .background(colorResource(R.color.button_normal)),
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
                                        println("value: ${enteredValues.joinToString("")}")
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
                    // Captain Image
                    Image(
                        painter = painterResource(id = R.drawable.captain_img), // Replace with your drawable
                        contentDescription = "Captain Image",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = CaptainName!!,
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
                            Text(text = "4.9", fontWeight = FontWeight.Bold)
                            repeat(5) {
                                Icon(
                                    painter = painterResource(id = R.drawable.location_icon_two), // Anchor rating icon
                                    contentDescription = "Rating",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(text = " | Rating", color = Color.Gray, fontSize = 12.sp)
                        }
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 0.dp) // Padding for the row
                ) {

                    IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:1228388383")
                        }
                        context.startActivity(intent) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.call_icon),
                            contentDescription = "Call",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(route = "$CHAT_SCREEN/${AppConstants.Voyage_ID}/${AppConstants.USER_ID}/${CaptainName}/${CaptainName}")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.message_icon),
                            contentDescription = "Message",
                            tint = Color.Unspecified,  modifier = Modifier.size(50.dp)
                        )
                    }

                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Boating App")
                        }
                        context.startActivity(Intent.createChooser(intent, "Share via"))
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.share_icon),
                            contentDescription = "Share",
                            tint = Color.Unspecified,  modifier = Modifier.size(50.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onDeclineClick()
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
                            val otp = enteredValues.get(0) + enteredValues.get(1) + enteredValues.get(2) + enteredValues.get(3) + enteredValues.get(4)
                            onAcceptClick(otp)
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
