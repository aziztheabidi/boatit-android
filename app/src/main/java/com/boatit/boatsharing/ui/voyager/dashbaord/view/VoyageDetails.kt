package com.boatit.boatsharing.ui.voyager.dashbaord.view

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.CHAT_SCREEN
import com.boatit.boatsharing.utils.AppConstants

@Composable
fun VoyageDetails(navController: NavController,
      OTP: Int?,
      CaptainName: String?,
      BoatName: String?,
      BoatModel: String?) {


    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier = Modifier.height(screenHeight * 0.6f),
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
        ) {
            Spacer(Modifier.height(30.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.voyage_start_text),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(
                        modifier = Modifier
                            .border(1.dp, Color(0xFF3366FF), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "12\nMinutes",
                            color = Color(0xFF3366FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

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
                    (OTP.toString()).forEach { pin ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(colorResource(R.color.button_normal)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pin.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
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
                        Text(
                            text = "Top Rated Captain",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
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

                    Spacer(modifier = Modifier.weight(1f))

                    // Boat Info
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.boat_icon), // Boat icon
                            contentDescription = "Boat Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = BoatModel!!,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3366FF)
                        )
                        Text(text = BoatName!!, fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(route = "$CHAT_SCREEN/${AppConstants.Voyage_ID}/${AppConstants.USER_ID}/${CaptainName}/${CaptainName}")
                        }
                        .padding(horizontal = 0.dp, vertical = 0.dp)

                ) {
                    var pickupNotes by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = pickupNotes,
                        onValueChange = { pickupNotes = it },
                        placeholder = {
                            Text(
                                "Any pickup notes?",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor =  colorResource(R.color.button_normal),
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
                            onDone = { keyboardController?.hide() }     // Hide keyboard on "Done"
                        ),// Rounded corners
                        modifier = Modifier
                            .widthIn(min = 200.dp, max = 200.dp) // Set the width
                            .padding(end = 8.dp)
                            .background(Color.White)
                    )
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

            modifier = Modifier
                .size(90.dp)
                .clickable { navController.navigate(NavigationManager.MENU_OPTIONS_SCREEN) }
        )
    }
}
@Preview
@Composable
fun PreviewVoyageDetails() {
    VoyageDetails(
        navController = rememberNavController(),
        OTP = 34455,
        CaptainName = "Johnvsbsb Doebbh",
        BoatName = "Sea Explorer",
        BoatModel = "WaveRunner FX"
    )
}