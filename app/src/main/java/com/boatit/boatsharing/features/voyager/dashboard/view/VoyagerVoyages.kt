@file:Suppress("ktlint:standard:function-naming")

package com.boatit.boatsharing.features.voyager.dashboard.view

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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.navigation.popBack
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.VoyagerVoyagesViewModel
import com.boatit.boatsharing.ui.components.CustomTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun VoyagerVoyages(
    navController: NavController,
    viewModel: VoyagerVoyagesViewModel = koinViewModel(),
) {
    val voyagesList by viewModel.loginState.collectAsState()

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
            LazyColumn(
                modifier =
                    Modifier.fillMaxSize()
                        .padding(innerPadding),
            ) {
                when (voyagesList) {
                    is NetworkResponse.Loading -> {
                    }
                    is NetworkResponse.Error -> {
                    }
                    is NetworkResponse.Success -> {
                        val pastVoyages = voyagesList.data?.obj.orEmpty()
                        items(pastVoyages.size) { voyage ->
                            PastVoyages(
                                navController = navController,
                                notification = pastVoyages[voyage],
                            )
                        }
                    }
                }
            }
        },
    )
}
