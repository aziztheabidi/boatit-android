package com.boatit.boatsharing.ui.captain.voyages.view

import VoyageData
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.captain.dashbaord.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashbaord.view.CaptainVoyageDetails
import com.boatit.boatsharing.ui.captain.dashbaord.viewmodel.StartVoyageViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcceptedRequestTab(
    navController: NavController,
    notification : List<VoyageData>,
    viewModelStart: StartVoyageViewModel = koinViewModel()
) {

    val startState by viewModelStart.loginState.collectAsState()

    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var showVoyagerRequest by rememberSaveable { mutableStateOf(false) }
    var voyageid by rememberSaveable { mutableStateOf(0) }

    when (startState) {
        is NetworkResponse.Success -> {
            if (isLoading) {
                isLoading = false
                Toast.makeText(context, "Voyage Started.", Toast.LENGTH_SHORT).show()
            }
        }
        is NetworkResponse.Error -> {
            if (isLoading) {
                isLoading = false
                Toast.makeText(context, "Unable To Start", Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    if (isLoading) {
        Dialog(
            onDismissRequest = {},
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ){
            Box(
                contentAlignment=  Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(White, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }
        }
    }

    LazyColumn {
        items(notification.size) { voyage ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(
                            top = 15.dp,
                            start = 15.dp, end = 15.dp, bottom = 15.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column() {
                            Spacer(Modifier.height(5.dp))

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
                                    text = notification.get(voyage).BookingDateTime
                                )

                            }

                            Spacer(Modifier.height(7.dp))

                            Text(
                                style = TextStyle(
                                    color = Color(0xFF6A6969),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.W500
                                ),
                                text = notification.get(voyage).Name
                            )
                            Spacer(Modifier.height(7.dp))
                            Text(
                                style = TextStyle(
                                    color = Color(0xFF6A6969),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.W500
                                ),
                                text = notification.get(voyage).BookingDateTime.split(",").get(0)
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 14.sp,

                                ),
                                text = "Voyagees Details"
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
                                            .weight(1f),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(10.dp)
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
                                                    tint = Color.Unspecified
                                                )
                                                Text(
                                                    style = TextStyle(
                                                        color = Color.Black,
                                                        fontSize = 12.sp,

                                                    ),
                                                    text = notification.get(voyage).NoOfVoyager.toString()
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
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold

                                                    ),
                                                    text = notification.get(voyage).AmountToPay.toString()
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
                                                    tint = Color.Unspecified
                                                )
                                                Text(
                                                    style = TextStyle(
                                                        color = Color.Black,
                                                        fontSize = 12.sp,
                                                    ),
                                                    text = notification.get(voyage).Duration!!
                                                )
                                            }
                                        }
                                    }

                                    Spacer(Modifier.width(5.dp))

                                    Card(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .height(205.dp)
                                            .weight(1f),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(10.dp)
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

                                                    ),
                                                    text = notification.get(voyage).PickupDock
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

                                                    ),
                                                    text = notification.get(voyage).DropOffDock!!
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
                                                    tint = Color.Unspecified
                                                )
                                                Text(
                                                    style = TextStyle(
                                                        color = Color.Black,
                                                        fontSize = 12.sp,

                                                    ),
                                                    text = notification.get(voyage).WaterStay
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
                        modifier = Modifier.fillMaxWidth(),
                    ) {

                        Button(
                            onClick = {
                                 showVoyagerRequest = true
                                 voyageid = voyage
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
                                text = "Start Now",
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

    if (showVoyagerRequest) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.partialExpand()
                }
                showVoyagerRequest = false
            },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.Transparent,
            tonalElevation = 16.dp,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(50.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                )
            },
            modifier = Modifier
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > 20) {
                            coroutineScope.launch {
                                sheetState.partialExpand()
                            }
                        }
                    }
                }
        ) {
            CaptainVoyageDetails(
                navController, notification.get(voyageid),
                notification.get(voyageid).Id,
                notification.get(voyageid).VoyagerName,
                onDeclineClick = {
                    showVoyagerRequest = false
                },
                onAcceptClick = { otp ->
                    isLoading = true
                    viewModelStart.startvoyage(VoyageStartRequest(notification.get(voyageid).Id, otp))
                }
            )
        }
    }

}