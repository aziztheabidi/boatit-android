package com.boatit.boatsharing.ui.captain.voyages.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.AcceptRequestViewModel
import com.boatit.boatsharing.ui.captain.voyages.viewmodel.CaptainVoyagesViewModel
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.utils.AppConstants
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun CaptainVoyages(navController: NavController, viewModel: CaptainVoyagesViewModel = koinViewModel(), viewModelR: AcceptRequestViewModel = koinViewModel()) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val voyagesList by viewModel.loginState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    val requestState by viewModelR.loginState.collectAsState()
    val defaultLatLng = LatLng(40.792240, -73.138260)
    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.voyages_screen), onImageClick = {
                println("clicked...")
            })
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 0.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        when (voyagesList) {
                            is NetworkResponse.Loading -> {
                                println("Loading")
                            }

                            is NetworkResponse.Error -> {
                                println(voyagesList.message)
                            }

                            is NetworkResponse.Success -> {
                                items(voyagesList.data!!.obj.Pending.size) { voyage ->
                                    PendingVoyages(
                                        navController = navController,
                                        notification = voyagesList.data!!.obj.Pending.get(voyage),
                                        onDeclineClick = {
                                            coroutineScope.launch {
                                                println(
                                                    "Declined: ${
                                                        voyagesList.data!!.obj.Pending.get(
                                                            voyage
                                                        ).Id
                                                    }"
                                                )
                                            }
                                        },
                                        onAcceptClick = {
                                            isLoading = true
                                            viewModelR.accept(AcceptVoyageRequest(voyagesList.data!!.obj.Pending.get(voyage).Id!!, AppConstants.USER_ID!!, defaultLatLng.latitude, defaultLatLng.longitude))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
    )
}




@Preview
@Composable
fun PreviewDashboardScreen() {
    CaptainVoyages(
        navController = rememberNavController(),
    )
}