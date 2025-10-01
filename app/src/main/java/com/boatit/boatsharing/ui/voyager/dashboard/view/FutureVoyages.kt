package com.boatit.boatsharing.ui.voyager.dashboard.view


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.layout.*
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset

import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.popBack
import androidx.compose.foundation.layout.height

import com.boatit.boatsharing.ui.voyager.dashboard.model.BookedVoyageObj
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.ConfirmBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.FutureVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.SponsorPaymentConfirmationViewModel
import com.boatit.boatsharing.uihelpers.CustomTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun FutureVoyages(navController: NavController,
                  viewModelCancel: CancelBookedVoyageViewModel = koinViewModel(),
                  viewModelConfirm: ConfirmBookedVoyageViewModel = koinViewModel(),
                  viewModelP: SponsorPaymentConfirmationViewModel = koinViewModel(),
                  viewModel: FutureVoyagesViewModel = koinViewModel()) {

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("UnConfirmed", "Pending")
    val context = LocalContext.current
    val paymentState by viewModelP.loginState.collectAsState()
    val voyagesList by viewModel.loginState.collectAsState()
    val CancelState by viewModelCancel.nearbyPlaces.collectAsState()
    val ConfirmState by viewModelConfirm.nearbyPlaces.collectAsState()
    var gettingData by remember { mutableStateOf(true) }
    var voyages by remember { mutableStateOf<BookedVoyageObj?>(null) }

    when (CancelState) {
        is NetworkResponse.Success -> {
            Toast.makeText(context, CancelState.message.toString(), Toast.LENGTH_SHORT).show()
            gettingData = true
            viewModel.voyages()
            viewModelCancel.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {
            Toast.makeText(context, CancelState.message, Toast.LENGTH_SHORT).show()
            gettingData = true
            viewModel.voyages()
            viewModelCancel.resetNearbyPlaces()
        }
        else -> {}
    }

    when (voyagesList) {
        is NetworkResponse.Success -> {
            if(gettingData) {
                gettingData = false
                voyages = voyagesList.data?.obj
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                viewModel.resetNearbyPlaces()
            }
        }
        is NetworkResponse.Error -> {
            gettingData = true
            println("Message" + voyagesList.message)
            Toast.makeText(context, voyagesList.message, Toast.LENGTH_SHORT).show()
            viewModel.resetNearbyPlaces()
        }
        else -> {}
    }

    when (ConfirmState) {
        is NetworkResponse.Success -> {
            Toast.makeText(context, "Voyage Confirmed", Toast.LENGTH_SHORT).show()
            viewModel.voyages()
            gettingData = true
            viewModelConfirm.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {}
        else -> {}
    }

    when (paymentState) {
        is NetworkResponse.Success -> {
            viewModel.voyages()
            gettingData = true
            viewModelP.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {
            viewModel.voyages()
            gettingData = true
            viewModelP.resetNearbyPlaces()
        }
        else -> {}
    }

    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.voyages_future), onImageClick = {
                navController.popBack()
            })
        },
        containerColor = White,
        content = { innerPadding ->
            Box {
                if (!gettingData){
                    Column(
                        modifier = Modifier
                            .padding(
                                top = innerPadding.calculateTopPadding() + 15.dp,
                                start = 5.dp,
                                end = 5.dp,
                            )
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    width = 0.5.dp,
                                    color = colorResource(R.color.button_normal),
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(White)
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    val isSelected = selectedTabIndex == index

                                    val shape = if (isSelected) {
                                        RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                                    } else {
                                        RoundedCornerShape(0.dp)
                                    }
                                    val offsetModifier = if (isSelected) Modifier.offset(
                                        x = 0.dp,
                                        y = (-1).dp
                                    ) else Modifier

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .then(offsetModifier)
                                            .clip(shape)
                                            .background(
                                                if (isSelected) colorResource(R.color.button_normal) else White
                                            )
                                            .clickable { selectedTabIndex = index },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = title,
                                            color = if (isSelected) White else colorResource(R.color.button_normal),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }

                        when (selectedTabIndex) {
                            0 -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    if (!(voyages?.UnConfirmed?.Id.equals(""))) {
                                        FutureVoyagerItems(
                                            navController = navController,
                                            notification = voyages?.UnConfirmed
                                        )
                                    }

                                }
                            }

                            1 -> LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                            ) {
                                items(voyages?.Confirmed?.size!!) { voyage ->
                                    FutureConfirmVoyagerItems(
                                        navController = navController,
                                        notification = voyages?.Confirmed?.get(voyage)
                                    )
                                }
                            }
                        }
                    }
                }
                if (gettingData) {
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
            }
        },
    )
}
