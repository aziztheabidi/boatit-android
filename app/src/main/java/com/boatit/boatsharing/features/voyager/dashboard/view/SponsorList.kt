@file:Suppress("ktlint:standard:function-naming")

package com.boatit.boatsharing.features.voyager.dashboard.view

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.navigation.popBack
import com.boatit.boatsharing.features.voyager.dashboard.domain.model.SponsorVoyageDomainModel
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagerPayment
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.SponsorPaymentConfirmationViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.SponsorPaymentSheetConfigViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.SponsorVoyagesUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.SponsorVoyagesUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.SponsorVoyagesViewModel
import com.boatit.boatsharing.ui.components.CustomTopBar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SponsorList(
    navController: NavController,
    viewModel: SponsorVoyagesViewModel = koinViewModel(),
    viewModelStripe: SponsorPaymentSheetConfigViewModel = koinViewModel(),
    viewModelP: SponsorPaymentConfirmationViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val paymentUi by viewModelP.uiState.collectAsState()
    val paymentState = paymentUi.networkState
    val sponsorUi by viewModelStripe.uiState.collectAsState()
    val declineState = sponsorUi.declinePaymentState

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is SponsorVoyagesUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    LaunchedEffect(paymentState) {
        when (paymentState) {
            is NetworkResponse.Success,
            is NetworkResponse.Error,
            -> viewModel.onEvent(SponsorVoyagesUiEvent.Refresh)

            else -> Unit
        }
    }

    LaunchedEffect(declineState) {
        when (declineState) {
            is NetworkResponse.Success -> {
                viewModel.onEvent(SponsorVoyagesUiEvent.Refresh)
                viewModelStripe.resetPaymentSheetState()
            }

            is NetworkResponse.Error -> {
                Toast.makeText(context, declineState.message, Toast.LENGTH_LONG).show()
                viewModelStripe.resetPaymentSheetState()
            }

            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(SponsorVoyagesUiEvent.Refresh)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = stringResource(R.string.voyages_sponsor), onImageClick = {
                navController.popBack()
            })
        },
        content = { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
            ) {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                ) {
                    items(items = uiState.voyages, key = { it.id }) { voyage ->
                        SponsorVoyagerItems(
                            navController = navController,
                            notification = voyage.toDto(),
                        )
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun PreviewSponsorList() {
    SponsorList(navController = rememberNavController())
}

private fun SponsorVoyageDomainModel.toDto(): SponsorVoyagerPayment {
    return SponsorVoyagerPayment(
        Id = id,
        Name = name,
        VoyagerName = voyagerName,
        VoyagerPhoneNumber = voyagerPhoneNumber,
        PickupDock = pickupDock,
        PickupDockLatitude = pickupDockLatitude,
        PickupDockLongitude = pickupDockLongitude,
        DropOffDock = dropOffDock,
        DropOffDockLatitude = dropOffDockLatitude,
        DropOffDockLongitude = dropOffDockLongitude,
        AmountToPay = amountToPay,
        NoOfVoyagers = noOfVoyagers,
        WaterStay = waterStay,
        Duration = duration,
        BookingDateTime = bookingDateTime,
        VoyageStatus = voyageStatus,
    )
}
