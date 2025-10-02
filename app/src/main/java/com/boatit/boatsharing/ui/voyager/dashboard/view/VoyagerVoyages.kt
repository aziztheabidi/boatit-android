package com.boatit.boatsharing.ui.voyager.dashboard.view


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
import androidx.compose.ui.graphics.Color
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
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.VoyagerVoyagesViewModel
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.utils.AppConstants
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.layout.Spacer
import com.boatit.boatsharing.routes.popBack
import androidx.compose.foundation.layout.height


@Composable
fun VoyagerVoyages(navController: NavController, viewModel: VoyagerVoyagesViewModel = koinViewModel()) {

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
                modifier = Modifier.fillMaxSize().
                padding(innerPadding),
            ) {
                when (voyagesList) {
                    is NetworkResponse.Loading -> {
                        println("Loading")
                    }
                    is NetworkResponse.Error -> {
                        println(voyagesList.message)
                    }
                    is NetworkResponse.Success -> {
                        items(voyagesList.data!!.obj.size) { voyage ->
                            PastVoyages(
                                navController = navController,
                                notification = voyagesList.data!!.obj.get(voyage))
                        }
                    }
                }
            }
        },
    )
}
