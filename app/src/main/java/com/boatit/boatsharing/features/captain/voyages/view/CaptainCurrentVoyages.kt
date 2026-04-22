package com.boatit.boatsharing.features.chat.view

import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.popBack
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageData
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.AcceptRequestUiEffect
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.AcceptRequestViewModel
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.CaptainActiveVoyagesViewModel
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiEffect
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiEvent
import com.boatit.boatsharing.features.captain.voyages.view.AcceptedRequestTab
import com.boatit.boatsharing.features.captain.voyages.view.StartedRequestTab
import com.boatit.boatsharing.features.captain.voyages.viewmodel.ICaptainCurrentVoyagesViewModel
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.SessionDialog
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@Composable
fun CaptainCurrentVoyages(
    navController: NavController,
    viewModel: ICaptainCurrentVoyagesViewModel = koinViewModel<CaptainActiveVoyagesViewModel>(),
    viewModelR: AcceptRequestViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val tabTitles = listOf("Pending", "Accepted", "Started")
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(CaptainCurrentVoyagesUiEvent.Initialize)
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEffects.collectLatest { effect ->
            when (effect) {
                is CaptainCurrentVoyagesUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.voyage_text), onImageClick = {
                navController.popBack()
            })
        },
        containerColor = Color.White,
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier.fillMaxSize()
                        .padding(
                            top = innerPadding.calculateTopPadding() + 20.dp,
                            start = 5.dp,
                            end = 5.dp,
                            bottom = 5.dp,
                        ),
            ) {
                Box(
                    modifier =
                        Modifier
                            .height(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = 0.5.dp,
                                color = colorResource(R.color.button_normal),
                                shape = RoundedCornerShape(10.dp),
                            ),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White),
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            val isSelected = state.selectedTabIndex == index

                            val shape =
                                if (isSelected) {
                                    RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                                } else {
                                    RoundedCornerShape(0.dp)
                                }
                            val offsetModifier = if (isSelected) Modifier.offset(x = 0.dp, y = (-1).dp) else Modifier

                            Box(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .then(offsetModifier)
                                        .clip(shape)
                                        .background(
                                            if (isSelected) colorResource(R.color.button_normal) else Color.White,
                                        )
                                        .clickable { viewModel.onEvent(CaptainCurrentVoyagesUiEvent.SelectTab(index)) },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = title,
                                    color = if (isSelected) Color.White else colorResource(R.color.button_normal),
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    }
                }

                when (state.selectedTabIndex) {
                    0 -> Tab1Content(state.pending)
                    1 ->
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.White),
                        ) {
                            AcceptedRequestTab(navController, state.accepted)
                        }
                    2 ->
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.White),
                        ) {
                            StartedRequestTab(navController, state.started)
                        }
                }
            }
        },
    )
}

@Composable
fun Tab1Content(notification: List<VoyageData>) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        PendingCardList(notification)
    }
}

@Composable
fun PendingCardList(notification: List<VoyageData>) {
    LazyColumn {
        items(notification.size) { voyage ->
            PendingCard(notification.get(voyage))
        }
    }
}

@Composable
fun PendingCard(
    notification: VoyageData?,
    viewModelR: AcceptRequestViewModel = koinViewModel(),
    viewModel: ICaptainCurrentVoyagesViewModel = koinViewModel<CaptainActiveVoyagesViewModel>(),
    userSessionStore: UserSessionStore = get(UserSessionStore::class.java),
) {
    val defaultLatLng = LatLng(40.792240, -73.138260)
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    var showDialogForAccept by remember { mutableStateOf(false) }
    var showDialogForCancel by remember { mutableStateOf(false) }

    LaunchedEffect(viewModelR) {
        viewModelR.uiEffect.collectLatest { effect ->
            if (!isLoading) return@collectLatest

            when (effect) {
                is AcceptRequestUiEffect.Accepted -> {
                    isLoading = false
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    viewModel.onEvent(CaptainCurrentVoyagesUiEvent.RefreshVoyages)
                    viewModelR.resetNearbyPlaces()
                }

                is AcceptRequestUiEffect.Declined -> {
                    isLoading = false
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    viewModel.onEvent(CaptainCurrentVoyagesUiEvent.RefreshVoyages)
                    viewModelR.resetNearbyPlaces()
                }

                is AcceptRequestUiEffect.ShowToast -> {
                    isLoading = false
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (isLoading) {
        Dialog(
            onDismissRequest = {},
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .size(100.dp)
                        .background(White, shape = RoundedCornerShape(8.dp)),
            ) {
                CircularProgressIndicator()
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(
                        top = 15.dp,
                        start = 15.dp,
                        end = 15.dp,
                        bottom = 15.dp,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Boat Info Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
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
                        text = notification?.BookingDateTime?.split(",")?.getOrNull(0).orEmpty(),
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
                            modifier = Modifier.fillMaxWidth(),
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
                                                ),
                                            text = notification?.NoOfVoyager?.toString().orEmpty(),
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
                                                ),
                                            text =
                                                notification?.Duration?.takeIf { it.isNotBlank() }
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
                        text = "Decline",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.button_normal), // Text color matches border
                    )
                }

                Button(
                    onClick = {
                        showDialogForAccept = true
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
                        text = "Accept",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }
            }

            if (showDialogForAccept) {
                SessionDialog(
                    text = "Are you sure, you want to Accept voyage",
                    onCancel = {
                        showDialogForAccept = false
                    },
                    onPressOk = {
                        showDialogForAccept = false
                        val userId = userSessionStore.currentUserId()
                        viewModelR.accept(
                            AcceptVoyageRequest(
                                Id = notification?.Id.orEmpty(),
                                CaptainUserId = userId,
                                CaptainBookingLatitude = defaultLatLng.latitude,
                                CaptainBookingLongitude = defaultLatLng.longitude,
                            ),
                        )
                    },
                    showCancelButton = true,
                )
            }

            if (showDialogForCancel) {
                SessionDialog(
                    text = "Are you sure, you want to decline voyage",
                    onCancel = {
                        showDialogForCancel = false
                    },
                    onPressOk = {
                        showDialogForCancel = false
                        val userId = userSessionStore.currentUserId()
                        viewModelR.decline(
                            AcceptVoyageRequest(
                                Id = notification?.Id.orEmpty(),
                                CaptainUserId = userId,
                                CaptainBookingLatitude = defaultLatLng.latitude,
                                CaptainBookingLongitude = defaultLatLng.longitude,
                            ),
                        )
                    },
                    showCancelButton = true,
                )
            }
        }
    }
}
