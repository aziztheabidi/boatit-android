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

import com.boatit.boatsharing.ui.voyager.dashbaord.model.BookedVoyageObj
import com.boatit.boatsharing.ui.voyager.dashbaord.model.TravelNowObj
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.ConfirmBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FutureVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.TravelNowViewModel
import com.boatit.boatsharing.uihelpers.CustomTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun TravelNow(
    navController: NavController,
    viewModel: TravelNowViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsState()

    uiState.toastMessage?.let { msg ->
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        viewModel.clearToast()
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.travel_now), onImageClick = {
                navController.popBackStack()
            })
        },
        containerColor = Color.White,
        content = { innerPadding ->
            if (uiState.isLoading) {
                Dialog(
                    onDismissRequest = {},
                    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .background(White, shape = RoundedCornerShape(8.dp))
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .fillMaxSize()
                ) {
                    TravelNowItem(
                        navController = navController,
                        notification = uiState.voyage
                    )
                }
            }
        },
    )
}

