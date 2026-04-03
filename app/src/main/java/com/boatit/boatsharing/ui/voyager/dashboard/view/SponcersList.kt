package com.boatit.boatsharing.ui.voyager.dashboard.view


import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.SponcerVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.SponsorPaymentConfirmationViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.SponsorPaymentSheetConfigViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.VoyagerVoyagesViewModel
import com.boatit.boatsharing.uihelpers.CustomTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SponcersList(navController: NavController,
     viewModel: SponcerVoyagesViewModel = koinViewModel(),
     viewModelStripe: SponsorPaymentSheetConfigViewModel = koinViewModel(),
     viewModelP: SponsorPaymentConfirmationViewModel = koinViewModel()) {

    val context = LocalContext.current
    val voyagesList by viewModel.loginState.collectAsState()
    val paymentState by viewModelP.loginState.collectAsState()
    val declineState by viewModelStripe.declineState.collectAsState()

    when (paymentState) {
        is NetworkResponse.Success -> {
            viewModel.voyages()
        }
        is NetworkResponse.Error -> {
            viewModel.voyages()
        }
        else -> {}
    }

    when (declineState) {
        is NetworkResponse.Success -> {
            viewModel.voyages()
            viewModelStripe.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {
            Toast.makeText(context, declineState.message, Toast.LENGTH_LONG).show()
            viewModelStripe.resetNearbyPlaces()
        }
        else -> {}
    }

    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = stringResource(R.string.voyages_sponcer), onImageClick = {
                navController.popBack()
            })
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                   .padding(horizontal =8.dp)
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                ) {
                    when (voyagesList) {
                        is NetworkResponse.Loading -> {
                            
                        }

                        is NetworkResponse.Error -> {
                            
                        }

                        is NetworkResponse.Success -> {
                            items(voyagesList.data!!.obj.size) { voyage ->
                                SponsorVoyagerItems(
                                    navController = navController,
                                    notification = voyagesList.data!!.obj.get(voyage)
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}

