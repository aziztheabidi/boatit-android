package com.boatit.boatsharing.ui.chat.view


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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageDetails
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.CaptainActiveVoyagesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CaptainCurrentVoyages(navController: NavController, viewModel: CaptainActiveVoyagesViewModel = koinViewModel()) {

    var selectedTabIndex by remember { mutableStateOf(1) }
    val tabTitles = listOf("Ongoing Requests", "InProcess Requests")
    val voyagesList by viewModel.loginState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var Ongoing by remember { mutableStateOf(listOf<VoyageDetails>()) }
    var InProcess by remember { mutableStateOf(listOf<VoyageDetails>()) }

    when (voyagesList) {
        is NetworkResponse.Loading -> {
            println("Loading")
        }

        is NetworkResponse.Error -> {
            println(voyagesList.message)
        }

        is NetworkResponse.Success -> {
            InProcess = voyagesList.data?.obj?.Ongoing!!
            Ongoing = voyagesList.data?.obj?.Ongoing!!
        }
    }

    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color.White // Make indicator transparent
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) Color.White else colorResource(R.color.button_normal)
                        )
                    },
                    modifier = Modifier.background(if (selectedTabIndex == index) colorResource(R.color.button_normal)else Color.White)
                )
            }
        }
        when (selectedTabIndex) {
            0 -> Tab1Content(Ongoing)
            1 -> Tab2Content(InProcess)
        }
    }
}

@Composable
fun Tab1Content(notification : List<VoyageDetails>) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        OnGoingCardList(notification)
    }
}

@Composable
fun Tab2Content(notification : List<VoyageDetails>) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        InProcessList(notification)
    }
}

@Composable
fun OnGoingCardList(notification : List<VoyageDetails>) {
    LazyColumn {
        items(notification.size) { voyage ->
            OnGoingCard(notification.get(voyage))
        }
    }
}
@Composable
fun InProcessList(notification : List<VoyageDetails>) {
    LazyColumn {
        items(notification.size) { voyage ->
            InProcessCard(notification.get(voyage))
        }
    }
}

@Composable
fun OnGoingCard(notification : VoyageDetails?) {
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
                    Text(text = notification?.VoyagerName.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = notification?.AmountToPay.toString(), fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Spacer(modifier = Modifier.height(4.dp)) // Reduced spacing

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.passengers),
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(R.color.button_normal)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = notification?.NoOfVoyager.toString(), fontSize = 16.sp)
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
                    Text(text = notification?.PickupDock.toString(), fontSize = 16.sp)
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
                    Text(text = notification?.DropOffDock.toString(), fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds spacing between buttons
                ) {
                    Button(
                        onClick = { },
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
                            text = "Details",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = R.color.button_normal) // Text color matches border
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End // Align icons to the end
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.call_icon),
                            contentDescription = "Message",
                            tint = Color.Unspecified, modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.message_icon),
                            contentDescription = "Message",
                            tint = Color.Unspecified, modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun InProcessCard(notification : VoyageDetails?) {
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
                    Text(text = notification?.VoyagerName.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = notification?.AmountToPay.toString(), fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Spacer(modifier = Modifier.height(4.dp)) // Reduced spacing

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.passengers),
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(R.color.button_normal)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = notification?.NoOfVoyager.toString(), fontSize = 16.sp)
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
                    Text(text = notification?.PickupDock.toString(), fontSize = 16.sp)
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
                    Text(text = notification?.DropOffDock.toString(), fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds spacing between buttons
                ) {
                    Button(
                        onClick = { },
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
                            text = "Details",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = R.color.button_normal) // Text color matches border
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End // Align icons to the end
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.call_icon),
                            contentDescription = "Message",
                            tint = Color.Unspecified, modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.message_icon),
                            contentDescription = "Message",
                            tint = Color.Unspecified, modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }
        }
    }


}

@Preview
@Composable
fun PreviewCaptainRequests() {
    CaptainCurrentVoyages(navController = rememberNavController())
}