package com.boatit.boatsharing.ui.voyager.dashbaord.view

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ActiveVoyageDetails
import com.boatit.boatsharing.utils.AppConstants

@Composable

fun ConfirmBooking(
    navController: NavController,
    voyage: ActiveVoyageDetails,
    onCancelClick: () -> Unit,
    onPayNowClick: () -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier = Modifier.height(screenHeight * 0.75f),
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
                    .background(Color.White)
                    .verticalScroll(rememberScrollState()) // Add this
                    .padding(16.dp),
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
                                text = voyage.BookingDateTime
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.pending),
                                    contentDescription = "Status Icon",
                                    modifier = Modifier
                                        .size(25.dp) // Adjust size as needed
                                        .padding(end = 5.dp), // Add some space between text and icon
                                    tint = Color.Unspecified // Change color of the icon
                                )
                                Text(
                                    style = TextStyle(
                                        color = Color(0xFF797979),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W500
                                    ),
                                    text = voyage.Status
                                )

                            }
                        }

                        Spacer(Modifier.height(7.dp))

                        Text(
                            style = TextStyle(
                                color = Color(0xFF6A6969),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W500
                            ),
                            text = voyage.Name
                        )
                        Spacer(Modifier.height(7.dp))
                        Text(
                            style = TextStyle(
                                color = Color(0xFF6A6969),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W500
                            ),
                            text = voyage.BookingDateTime
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
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
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.passengers),
                                                contentDescription = "Status Icon",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                                tint = Color.Unspecified
                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = voyage.NoOfVoyagers.toString()
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
                                                tint = Color.Unspecified
                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = voyage.AmountToPay.toString()
                                               // text = AppConstants.Estimated_Cost.toString()!!
                                            )
                                        }

                                        Divider(
                                            color = Color(0xFFA0A0A0),
                                            thickness = 1.dp
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.clock),
                                                contentDescription = "Status Icon",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                                tint = Color.Unspecified
                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text =voyage.Duration.toString().takeIf { it.isNotBlank() }
                                                    ?: "---"
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
                                                tint = Color.Unspecified
                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = voyage.PickupDock
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
                                                tint = Color.Unspecified

                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = voyage.DropOffDock
                                            )
                                        }

                                        Divider(
                                            color = Color(0xFFA0A0A0),
                                            thickness = 1.dp
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.flag),
                                                contentDescription = "Status Icon",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                                tint = Color.Unspecified
                                            )
                                            Text(
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500
                                                ),
                                                text = voyage.WaterStay
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Captain Image
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = voyage.CaptainName.firstOrNull()?.uppercase() ?: "-",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = voyage.CaptainName,
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
                            Text(text = voyage.Rating.toString(),
                                fontWeight = FontWeight.Bold)
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
                            text = voyage.BoatModel,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3366FF)
                        )
                        Text(text = voyage.BoatName!!, fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


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
