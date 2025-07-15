package com.boatit.boatsharing.ui.captain.voyages.view
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.ui.captain.voyages.model.VoyageDetails
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyageNotification

@Composable
fun PendingVoyages(navController: NavController, notification : VoyageDetails?, onDeclineClick: () -> Unit, onAcceptClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {

        Box {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Map Pin",
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
                    .offset(x = (-10).dp, y = (-10).dp)
                    .alpha(0.3f),
                tint = Color.LightGray
            )
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(notification?.Name.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(notification?.TotalAmount.toString(), fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.passengers),
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(notification?.PastVoyage.toString(), fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.location_icon),
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(notification?.PickupDock.toString(), fontSize = 16.sp)
                }


                Spacer(modifier = Modifier.width(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.drop_off_loc_icon),
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(notification?.DropOffDock.toString(), fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds spacing between buttons
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
                            text = "Decline",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = R.color.button_normal) // Text color matches border
                        )
                    }

                    Button(
                        onClick = {
                            onAcceptClick()
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
                            text = "Accept",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                }
            }

        }
    }


}
