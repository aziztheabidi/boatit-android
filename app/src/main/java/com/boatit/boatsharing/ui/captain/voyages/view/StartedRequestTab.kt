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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.CAPTAIN_FEEDBACK_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.CaptainFeedbackRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashboard.view.CaptainVoyageDetails
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.CaptainFeedbackViewModel
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.CompleteVoyageViewModel
import com.boatit.boatsharing.uihelpers.SessionDialog
import com.boatit.boatsharing.utils.AppConstants
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartedRequestTab(navController: NavController,
                      notification : List<VoyageData>,
                      viewModel: CompleteVoyageViewModel = koinViewModel(),
) {

    val startState by viewModel.loginState.collectAsState()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    var showFeedback by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var showVoyagerRequest by rememberSaveable { mutableStateOf(false) }
    var voyageid by rememberSaveable { mutableStateOf(0) }

    var showDialogForComplete by remember { mutableStateOf(false) }

    when (startState) {
        is NetworkResponse.Success -> {
            if (isLoading) {
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, "Voyage Successfully Completed", Toast.LENGTH_SHORT).show()
                navController.navigate(route = "$CAPTAIN_FEEDBACK_SCREEN/" + notification.get(voyageid).Id)
                showFeedback =true
            }
        }
        is NetworkResponse.Error -> {
            if (isLoading) {
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
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

                    // Boat Info Card
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
                                                        fontSize = 12.sp,

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
                                                    text = notification.get(voyage).Duration.takeIf { it.isNotBlank() }
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
                                                    text = notification.get(voyage).DropOffDock
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




                    // Action Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {

                        Button(
                            onClick = {
                                showDialogForComplete = true


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
                                text = "Complete Voyage",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }


                    if(showDialogForComplete){

                        SessionDialog(
                            text = "Are you sure, you want to Complete voyage",
                            onCancel = {
                                showDialogForComplete = false
                            },
                            onPressOk = {
                                showDialogForComplete = false
                                isLoading = true
                                isNetworkError = true
                                voyageid = voyage
                                viewModel.startvoyage(
                                    VoyageCompleteRequest(notification.get(voyage).Id),
                                )

                            },
                            showCancelButton = true
                        )
                    }
                }
            }
        }
    }



}