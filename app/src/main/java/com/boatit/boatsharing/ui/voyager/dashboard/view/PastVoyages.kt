package com.boatit.boatsharing.ui.voyager.dashboard.view
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyageDetails


@Composable
fun PastVoyages(navController: NavController, notification : VoyageDetails?) {

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
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
                    Text(notification?.CaptainName.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Column(horizontalAlignment = Alignment.End) {
                        Text(notification?.AmountToPay.toString(), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.passengers),
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(R.color.button_normal)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(notification?.BoatName.toString(), fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.location_icon),
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(R.color.button_normal)
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(notification?.Status.toString(), fontSize = 18.sp, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic)
                }
            }
        }
    }


}


