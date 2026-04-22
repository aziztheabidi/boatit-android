@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:discouraged-comment-location",
    "ktlint:standard:curly-spacing",
    "ktlint:standard:no-line-break-after-else",
    "ktlint:standard:if-else-wrapping",
)

package com.boatit.boatsharing.features.voyager.dashboard.view
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.features.voyager.dashboard.model.BookedVoyage
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyages
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CancelBookedVoyageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FutureConfirmVoyagerItems(
    navController: NavController,
    notification: BookedVoyage?,
    viewModelCancel: CancelBookedVoyageViewModel = koinViewModel(),
) {
    var loading by remember { mutableStateOf(false) }

    Card(
        modifier =
            Modifier.fillMaxWidth().padding(10.dp)
                .border(0.5.dp, colorResource(id = R.color.button_normal), RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(
                        top = 5.dp,
                        start = 5.dp,
                        end = 5.dp,
                        bottom = 5.dp,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().padding(10.dp),
            ) {
                Column {
                    Spacer(Modifier.height(5.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Normal,
                                ),
                            text = notification?.BookingDateTime.orEmpty(),
                        )
                    }

                    Spacer(Modifier.height(7.dp))

                    Text(
                        style =
                            TextStyle(
                                color = Color(0xFF6A6969),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W500,
                            ),
                        text = notification?.Name.orEmpty(),
                    )
                    Spacer(Modifier.height(7.dp))
                    Text(
                        style =
                            TextStyle(
                                color = Color(0xFF6A6969),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W500,
                            ),
                        text = notification?.BookingDateTime.orEmpty(),
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        style =
                            TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                            ),
                        text = "Voyagees Details",
                    )
                    Spacer(Modifier.height(10.dp))

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
                                        .weight(1f),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                            ) {
                                Column(
                                    modifier =
                                        Modifier
                                            .padding(10.dp)
                                            .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly, // Ensures space is even between the rows
                                ) {
                                    // First row with icon and text
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
                                                    fontWeight = FontWeight.W400,
                                                ),
                                            text = notification?.NoOfVoyagers.toString(),
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
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                ),
                                            text = notification?.AmountToPay.toString(),
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp,
                                    )

                                    // Third row with icon and text
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
                                                    fontWeight = FontWeight.W400,
                                                ),
                                            text = notification?.Duration.toString(),
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
                                        .weight(1f),
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
                                                    fontWeight = FontWeight.W400,
                                                ),
                                            text = notification?.PickupDock.orEmpty(),
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
                                                    fontWeight = FontWeight.W400,
                                                ),
                                            text = notification?.DropOffDock.orEmpty(),
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp,
                                    )

                                    // Third row with icon and text
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
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W400,
                                                ),
                                            text = notification?.WaterStay.orEmpty(),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp), // Only left and right padding
            ) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(10.dp), // Corner radius
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(
                                width = 0.5.dp,
                                color = colorResource(id = R.color.black), // Border color
                                shape = RoundedCornerShape(8.dp), // Apply same corner radius to border
                            ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                ) {
                    Text(
                        text = notification?.OTP.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(id = R.color.black), // Text color matches border
                    )
                }
            }
            Spacer(Modifier.height(10.dp))

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp), // Only left and right padding
            ) {
                Button(
                    onClick = {
                        viewModelCancel.fetchNearbyPlaces(
                            CancelBookedVoyages(
                                notification?.Id.orEmpty(),
                                "",
                            ),
                        )
                        loading = true
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 1.dp)
                            .border(
                                width = 0.5.dp,
                                color = colorResource(id = R.color.button_normal), // Border color
                                shape = RoundedCornerShape(10.dp), // Apply same corner radius to border
                            ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                ) {
                    Text(
                        text = "Cancel Voyage",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.button_normal), // Border color
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}
