package com.boatit.boatsharing.features.captain.voyages.view

import android.widget.Toast
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.popBack
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.AcceptRequestViewModel
import com.boatit.boatsharing.features.captain.voyages.viewmodel.CaptainVoyagesUiEffect
import com.boatit.boatsharing.features.captain.voyages.viewmodel.CaptainVoyagesViewModel
import com.boatit.boatsharing.ui.components.CustomTopBar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun CaptainVoyages(
    navController: NavController,
    viewModel: CaptainVoyagesViewModel = koinViewModel(),
    viewModelR: AcceptRequestViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is CaptainVoyagesUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.voyages_past), onImageClick = {
                navController.popBack()
            })
        },
        containerColor = Color.White,
        content = { innerPadding ->
            Box(
                modifier =
                    Modifier.fillMaxSize()
                        .padding(innerPadding),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(bottom = 0.dp),
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(uiState.voyages.size) { voyage ->
                            CaptainPastVoyages(
                                navController = navController,
                                notification = uiState.voyages[voyage].toDto(),
                            )
                        }
                    }
                }
            }
        },
    )
}

private fun com.boatit.boatsharing.features.captain.domain.model.CaptainCompletedVoyageDomainModel.toDto():
    com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyage {
    return com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyage(
        Id = id,
        Name = name,
        VoyagerUserId = voyagerUserId,
        VoyagerName = voyagerName,
        VoyagerPhoneNumber = voyagerPhoneNumber,
        Rating = rating,
        PickupDock = pickupDock,
        PickupDockLatitude = pickupDockLatitude,
        PickupDockLongitude = pickupDockLongitude,
        DropOffDock = dropOffDock,
        DropOffDockLatitude = dropOffDockLatitude,
        DropOffDockLongitude = dropOffDockLongitude,
        NoOfVoyager = noOfVoyager,
        AmountToPay = amountToPay,
        WaterStay = waterStay,
        Duration = duration,
        BookingDateTime = bookingDateTime,
    )
}
