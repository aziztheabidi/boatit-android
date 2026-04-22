@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:property-naming",
    "ktlint:standard:curly-spacing",
    "ktlint:standard:no-line-break-after-else",
)

package com.boatit.boatsharing.features.voyager.dashboard.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.ConfirmBookedVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.TravelNowViewModel
import com.boatit.boatsharing.ui.components.CustomTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun TravelNow(
    navController: NavController,
    viewModelCancel: CancelBookedVoyageViewModel = koinViewModel(),
    viewModelConfirm: ConfirmBookedVoyageViewModel = koinViewModel(),
    viewModel: TravelNowViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsState()

    val CancelState by viewModelCancel.nearbyPlaces.collectAsState()
    val ConfirmState by viewModelConfirm.confirmationState.collectAsState()

    LaunchedEffect(CancelState) {
        when (CancelState) {
            is NetworkResponse.Success -> {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                viewModel.loadVoyages()
                viewModelCancel.resetNearbyPlaces()
            }

            is NetworkResponse.Error -> {
                Toast.makeText(context, CancelState.message, Toast.LENGTH_SHORT).show()
                viewModel.loadVoyages()
                viewModelCancel.resetNearbyPlaces()
            }

            else -> Unit
        }
    }

    LaunchedEffect(ConfirmState) {
        when (ConfirmState) {
            is NetworkResponse.Success -> {
                Toast.makeText(context, "Voyage Confirmed", Toast.LENGTH_SHORT).show()
                viewModel.loadVoyages()
                viewModelConfirm.resetConfirmationState()
            }

            is NetworkResponse.Error,
            is NetworkResponse.Loading,
            -> Unit
        }
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.travel_now), onImageClick = {
                navController.popBackStack()
            })
        },
        containerColor = Color.White,
        content = { innerPadding ->
            if (uiState.isLoading || uiState.voyage == null) {
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
            } else {
                Column(
                    modifier =
                        Modifier
                            .padding(top = innerPadding.calculateTopPadding())
                            .fillMaxSize(),
                ) {
                    if (!(uiState.voyage?.Id.equals(""))) {
                        TravelNowItem(
                            navController = navController,
                            notification = uiState.voyage,
                        )
                    } else
                        {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Text("No data found")
                            }
                        }
                }
            }
        },
    )
}
