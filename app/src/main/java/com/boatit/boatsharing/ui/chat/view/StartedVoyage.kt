package com.boatit.boatsharing.ui.chat.view

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager

@Composable

fun StartedVoyage(
    navController: NavController,
  ) {
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
            Column(
                modifier = Modifier
                    .fillMaxSize().background(Color.White)
                    .padding(top = 50.dp,
                        start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Enjoy your cruise...",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
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

                Spacer(modifier = Modifier.width(15.dp))
                // Boat Info Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, color = colorResource(id = R.color.button_normal)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.boatit_logo), // Replace with actual image
                            contentDescription = "Boat Image",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Boat Name", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Cabin Cruise", color = Color.Gray)
                            Text("Capt. William Kidd", color = Color.Gray)
                            Text("15 Seater", color = Color.Gray)
                            Text("1379+ Voyages", color = Color.Gray)
                        }

                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("4.8", fontWeight = FontWeight.SemiBold)
                                Icon(
                                    painter = painterResource(id = R.drawable.location_icon_two),
                                    contentDescription = "Rating Icon",
                                    tint = Color.Unspecified
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Pickup & Pricing Info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min), // Ensures both cards match the tallest one
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(
                            1.dp,
                            color = colorResource(id = R.color.button_normal)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight() // Ensures card fills the available height
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.location_icon),
                                    contentDescription = "Pickup",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Pickup Location",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                )
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) // Changed from HorizontalDivider to Divider
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.drop_off_loc_icon),
                                    contentDescription = "Dropoff",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Drop Off Location",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(
                            1.dp,
                            color = colorResource(id = R.color.button_normal)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight() // Ensures card fills the available height
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.passengers),
                                    contentDescription = "Passengers",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "12 Passengers",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                )
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.money_icon),
                                    contentDescription = "money",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("$120", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


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
fun PreviewStartedVoyage() {
    StartedVoyage(
        navController = rememberNavController(),

    )
}