package com.boatit.boatsharing.features.captain.dashboard.view
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageNotification
import com.boatit.boatsharing.ui.components.SessionDialog

@Composable
fun AcceptVoyagerRequest(
    navController: NavController,
    notification: VoyageNotification?,
    onDeclineClick: () -> Unit,
    onAcceptClick: () -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var showDialogForCancel by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.height(screenHeight * 0.6f),
        contentAlignment = Alignment.TopCenter,
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 40.dp),
            shape =
                RoundedCornerShape(
                    topStart = 45.dp,
                    topEnd = 45.dp,
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize().background(Color.White)
                        .padding(
                            top = 50.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp,
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, color = colorResource(id = R.color.button_normal)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer(Modifier.height(10.dp))
                    notification?.BookingDateTime?.let {
                        Text(
                            style =
                                TextStyle(
                                    color = Color(0xFF6A6969),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W500,
                                ),
                            text = it,
                        )
                    }

                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .width(50.dp)
                                    .height(50.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0E0E0)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = notification?.Name.toString().firstOrNull()?.uppercase() ?: "-",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(notification?.Name.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(notification?.PastVoyages.toString(), color = Color.Gray)
                            Text(notification?.PhoneNumber.toString(), color = Color.Gray)
                        }

                        Column(
                            horizontalAlignment = Alignment.End,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Rating : " + notification?.Rating.toString(), fontWeight = FontWeight.SemiBold) // Rating not in api
                                Icon(
                                    painter = painterResource(id = R.drawable.location_icon_two),
                                    contentDescription = "Rating Icon",
                                    tint = Color.Unspecified,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Card(
                            modifier =
                                Modifier
                                    .padding(5.dp)
                                    .height(205.dp)
                                    .width(175.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .padding(16.dp)
                                        .fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceEvenly, // Ensures space is even between the rows
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.passengers),
                                        contentDescription = "Status Icon",
                                        modifier =
                                            Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                        tint = Color.Unspecified,
                                    )
                                    Text(
                                        style =
                                            TextStyle(
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.W500,
                                            ),
                                        text = notification?.NoOfVoyager.toString(),
                                    )
                                }
                                Divider(
                                    color = Color(0xFFA0A0A0),
                                    thickness = 1.dp,
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.money_icon),
                                        contentDescription = "Status Icon",
                                        modifier =
                                            Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                        tint = Color.Unspecified,
                                    )
                                    Text(
                                        style =
                                            TextStyle(
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.W500,
                                            ),
                                        text = notification?.TotalAmount.toString(),
                                    )
                                }

                                Divider(
                                    color = Color(0xFFA0A0A0),
                                    thickness = 1.dp,
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.clock),
                                        contentDescription = "Status Icon",
                                        modifier =
                                            Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                        tint = Color.Unspecified,
                                    )
                                    Text(
                                        style =
                                            TextStyle(
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.W500,
                                            ),
                                        text =
                                            notification?.Duration.toString().takeIf { it.isNotBlank() }
                                                ?: "---",
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.width(5.dp))

                        Card(
                            modifier =
                                Modifier
                                    .padding(5.dp)
                                    .height(205.dp)
                                    .width(175.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .padding(16.dp)
                                        .fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceEvenly, // Ensures space is even between the rows
                            ) {
                                // First row with icon and text
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.location_icon),
                                        contentDescription = "Status Icon",
                                        modifier =
                                            Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                        tint = Color.Unspecified,
                                    )
                                    Text(
                                        style =
                                            TextStyle(
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.W500,
                                            ),
                                        text = notification?.PickupDock.toString(),
                                    )
                                }

                                Divider(
                                    color = Color(0xFFA0A0A0),
                                    thickness = 1.dp,
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.drop_off_loc_icon),
                                        contentDescription = "Status Icon",
                                        modifier =
                                            Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                        tint = Color.Unspecified,
                                    )
                                    Text(
                                        style =
                                            TextStyle(
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.W500,
                                            ),
                                        text = notification?.DropOffDock.toString(),
                                    )
                                }

                                Divider(
                                    color = Color(0xFFA0A0A0),
                                    thickness = 1.dp,
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.flag),
                                        contentDescription = "Status Icon",
                                        modifier =
                                            Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                        tint = Color.Unspecified,
                                    )
                                    notification?.WaterStay?.let {
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500,
                                                ),
                                            text = it,
                                        )
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = {
                            showDialogForCancel = true
                        },
                        shape = RoundedCornerShape(10.dp), // Corner radius
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(50.dp)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.button_normal), // Border color
                                    shape = RoundedCornerShape(10.dp), // Apply same corner radius to border
                                ),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    ) {
                        Text(
                            text = stringResource(R.string.decline_text),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = R.color.button_normal), // Text color matches border
                        )
                    }

                    Button(
                        onClick = {
                            onAcceptClick()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 1.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal)),
                    ) {
                        Text(
                            text = stringResource(R.string.accept_text),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }

                if (showDialogForCancel) {
                    SessionDialog(
                        text = "Are you sure, you want to decline voyage",
                        onCancel = {
                            showDialogForCancel = false
                        },
                        onPressOk = {
                            showDialogForCancel = false
                            onDeclineClick()
                        },
                        showCancelButton = true,
                    )
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.wheel_icon),
            contentDescription = "Floating Icon",
            contentScale = ContentScale.FillBounds,
            modifier =
                Modifier
                    .size(90.dp)
                    .clickable { navController.navigate(NavigationManager.MENU_OPTIONS_SCREEN) },
        )
    }
}
