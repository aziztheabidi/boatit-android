package com.boatit.boatsharing.ui.voyager.dashbaord.view


import android.widget.Toast
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
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.SponcerVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.SponsorPaymentConfirmationViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.VoyagerVoyagesViewModel
import com.boatit.boatsharing.uihelpers.CustomTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SponcersList(navController: NavController,
     viewModel: SponcerVoyagesViewModel = koinViewModel(),
     viewModelP: SponsorPaymentConfirmationViewModel = koinViewModel()) {

    val context = LocalContext.current
    val voyagesList by viewModel.loginState.collectAsState()
    val paymentState by viewModelP.loginState.collectAsState()

    when (paymentState) {
        is NetworkResponse.Success -> {
            Toast.makeText(context, "Payment Successfull", Toast.LENGTH_SHORT).show()
            viewModel.voyages()
        }
        is NetworkResponse.Error -> {
            Toast.makeText(context, paymentState.message, Toast.LENGTH_SHORT).show()
            viewModel.voyages()
        }
        else -> {}
    }

    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.voyages_sponcer), onImageClick = {
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
                            SponsorVoyagerItems(
                                navController = navController,
                                notification = voyagesList.data!!.obj.get(voyage))
                        }
                    }
                }
            }
        },
    )
}


@Preview
@Composable
fun SponcersList() {
    SponsorScreen(navController = rememberNavController())
}