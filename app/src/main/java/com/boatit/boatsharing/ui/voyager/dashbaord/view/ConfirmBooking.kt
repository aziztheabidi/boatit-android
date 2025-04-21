package com.boatit.boatsharing.ui.voyager.dashbaord.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.utils.AppConstants

@Composable

fun ConfirmBooking(
                   navController: NavController,
                   onCancelClick: () -> Unit,
                   onPayNowClick: () -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(

        modifier = Modifier.height(screenHeight * 0.65f),
        contentAlignment = Alignment.TopCenter
    ) {
        // Main Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 20.dp),
            shape = RoundedCornerShape(
                topStart = 45.dp,
                topEnd = 45.dp
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState()) // Add this
                    .padding(
                        top = 50.dp,
                        start = 16.dp, end = 16.dp, bottom = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Boat Info Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column() {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                text = "Sunday, 12 April | 10:00 am"
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.pending),
                                    contentDescription = "Status Icon",
                                    modifier = Modifier
                                        .size(25.dp) // Adjust size as needed
                                        .padding(end = 5.dp), // Add some space between text and icon
                                    tint = Color.Blue // Change color of the icon
                                )
                                Text(
                                    style = TextStyle(
                                        color = Color(0xFF797979),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500
                                    ),
                                    text = "Pending"
                                )

                            }
                        }

                        Spacer(Modifier.height(7.dp))

                        Text(
                            style = TextStyle(
                                color = Color(0xFF6A6969),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W500
                            ),
                            text = "Event Conference"
                        )
                        Spacer(Modifier.height(7.dp))
                        Text(
                            style = TextStyle(
                                color = Color(0xFF6A6969),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W500
                            ),
                            text = "2025"
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500
                            ),
                            text = "Voyagees details"
                        )
                        Spacer(Modifier.height(10.dp))

                        Box(
                            Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .height(205.dp)
                                        .width(175.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxSize(),
                                        verticalArrangement = Arrangement.SpaceEvenly // Ensures space is even between the rows
                                    ) {
                                        // First row with icon and text
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.passengers),
                                                contentDescription = "Status Icon",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                                tint = Color.Blue
                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = "Home"
                                            )
                                        }

                                        Divider(
                                            color = Color(0xFFA0A0A0),
                                            thickness = 1.dp
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.money_icon),
                                                contentDescription = "Status Icon",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                                tint = Color.Blue
                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = AppConstants.Estimated_Cost.toString()!!
                                            )
                                        }

                                        Divider(
                                            color = Color(0xFFA0A0A0),
                                            thickness = 1.dp
                                        )

                                        // Third row with icon and text
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.clock),
                                                contentDescription = "Status Icon",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                                tint = Color.Blue
                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = AppConstants.Event_Time!!
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.width(5.dp))

                                Card(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .height(205.dp)
                                        .width(175.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxSize(),
                                        verticalArrangement = Arrangement.SpaceEvenly // Ensures space is even between the rows
                                    ) {
                                        // First row with icon and text
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.location_icon),
                                                contentDescription = "Status Icon",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                                tint = Color.Blue
                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = AppConstants.Pick_Up_Loc!!
                                            )
                                        }

                                        Divider(
                                            color = Color(0xFFA0A0A0),
                                            thickness = 1.dp
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.drop_off_loc_icon),
                                                contentDescription = "Status Icon",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                                tint = Color.Red

                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = AppConstants.Drop_Off_Loc!!
                                            )
                                        }

                                        Divider(
                                            color = Color(0xFFA0A0A0),
                                            thickness = 1.dp
                                        )

                                        // Third row with icon and text
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.flag),
                                                contentDescription = "Status Icon",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                                tint = Color.Blue
                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = "Home"
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }

                }



                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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

                    Button(
                        onClick = {
                            onPayNowClick()
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
                            text = stringResource(R.string.pay_now_text),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
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
fun PreviewConfirmBooking() {
    ConfirmBooking(
        navController = rememberNavController(),
        onCancelClick = {},
        onPayNowClick = {}
    )
}