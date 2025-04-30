package com.boatit.boatsharing.ui.voyager.dashbaord.view


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.captain.dashbaord.model.VoyageDetails
import com.boatit.boatsharing.ui.chat.view.InProcessList
import com.boatit.boatsharing.ui.chat.view.OnGoingCardList
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BookedVoyageObj
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.ConfirmBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FutureVoyagesViewModel
import com.boatit.boatsharing.uihelpers.CustomTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun FutureVoyages(navController: NavController,
                  viewModelCancel: CancelBookedVoyageViewModel = koinViewModel(),
                  viewModelConfirm: ConfirmBookedVoyageViewModel = koinViewModel(),
                  viewModel: FutureVoyagesViewModel = koinViewModel()) {

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("UnConfirmed", "Confirmed")
    val context = LocalContext.current
    val voyagesList by viewModel.loginState.collectAsState()
    val CancelState by viewModelCancel.nearbyPlaces.collectAsState()
    val ConfirmState by viewModelConfirm.nearbyPlaces.collectAsState()
    var getingData by remember { mutableStateOf(true) }
    var voyages by remember { mutableStateOf<BookedVoyageObj?>(null) }

    when (CancelState) {
        is NetworkResponse.Success -> {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
            getingData = true
            viewModel.voyages()
            viewModelCancel.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {
            Toast.makeText(context, CancelState.message, Toast.LENGTH_SHORT).show()
            getingData = true
            viewModel.voyages()
            viewModelCancel.resetNearbyPlaces()
        }
        else -> {}
    }

    when (voyagesList) {
        is NetworkResponse.Success -> {
            if(getingData) {
                getingData = false
                voyages = voyagesList.data?.obj
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            }
        }
        is NetworkResponse.Error -> {
            getingData = true
            println("Message" + voyagesList.message)
            Toast.makeText(context, voyagesList.message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    when (ConfirmState) {
        is NetworkResponse.Success -> {
            Toast.makeText(context, ConfirmState.message, Toast.LENGTH_SHORT).show()
            viewModel.voyages()
            viewModelConfirm.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {}
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
        containerColor = Color.White,
        content = { innerPadding ->
            if (getingData) {
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
            else {
                Column(modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding() + 15.dp,
                        start = 20.dp,
                        end = 20.dp,
                    )
                    .fillMaxSize()) {
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
                        0 ->  {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(innerPadding),
                            ) {
                                FutureVoyagerItems(
                                    navController = navController,
                                    notification = voyages?.UnConfirmed
                                )
                            }
                        }
                        1 ->  LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                        ) {
                            items(voyages?.Confirmed?.size!!) { voyage ->
                                FutureConfirmVoyagerItems(
                                    navController = navController,
                                    notification = voyages?.Confirmed?.get(voyage))
                            }
                        }
                        }
                    }
            }
        },
    )
}
