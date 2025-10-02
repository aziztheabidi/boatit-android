package com.boatit.boatsharing.ui.voyager.dashboard.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager.CHAT_SCREEN
import com.boatit.boatsharing.ui.chat.viewmodel.VoyagersListViewModel
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.alpha
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.chat.model.VoyagerInfo
import com.boatit.boatsharing.ui.chat.viewmodel.FollowViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.model.BusinessData
import com.boatit.boatsharing.ui.voyager.dashboard.model.SponsorVoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyageNotification
import com.boatit.boatsharing.ui.voyager.dashboard.view.FutureConfirmVoyagerItems
import com.boatit.boatsharing.ui.voyager.dashboard.view.FutureVoyagerItems
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.FetchBusinessViewModel


@Composable
fun BusinessListScreen(navController: NavController,
                       viewModel: FetchBusinessViewModel = koinViewModel(),
                       viewModelF: FollowViewModel = koinViewModel()) {

    val context = LocalContext.current
    val voyagesList by viewModel.loginState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Followed", "All")
    var showVoyagerRequest by rememberSaveable { mutableStateOf(false) }
    var notification by remember { mutableStateOf<VoyageNotification?>(null) }
    val followState by viewModelF.nearbyPlaces.collectAsState()

    when (followState) {
        is NetworkResponse.Success -> {
            if (isLoading) {
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, "Voyager Followed", Toast.LENGTH_SHORT).show()
                showVoyagerRequest = false
                AppConstants.Voyage_ID = notification?.Id
            }
        }
        is NetworkResponse.Error -> {
            if (isLoading) {
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.businesses), onImageClick = {
                navController.popBack()
            })
        },
        containerColor = White,
        content = { innerPadding ->
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding()
        ) {
            Column(modifier = Modifier
                .padding(
                    top = 15.dp,
                    start = 5.dp,
                    end = 5.dp,
                )
                .fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 0.5.dp,
                            color = colorResource(R.color.button_normal),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            val isSelected = selectedTabIndex == index

                            val shape = if (isSelected) {
                                RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                            } else {
                                RoundedCornerShape(0.dp)
                            }
                            val offsetModifier = if (isSelected) Modifier.offset(x = 0.dp, y = (-1).dp) else Modifier

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .then(offsetModifier)
                                    .clip(shape)
                                    .background(
                                        if (isSelected) colorResource(R.color.button_normal) else Color.White
                                    )
                                    .clickable { selectedTabIndex = index },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = title,
                                    color = if (isSelected) Color.White else colorResource(R.color.button_normal),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))


                when (selectedTabIndex) {
                    0 ->  {
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
                                    items(voyagesList.data!!.obj.Followed.size) { user ->
                                        UserItem(voyagesList.data!!.obj.Followed.get(user)) { dat ->
                                            AppConstants.Business = dat
                                            AppConstants.Business_Status = true
                                            navController.navigate(NavigationManager.BUSINESS_DETAIL_SCREEN)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    1 ->   LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp),
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
                                items(voyagesList.data!!.obj.UnFollowed.size) { user ->
                                    UserItem(voyagesList.data!!.obj.UnFollowed.get(user)) { dat ->
                                        AppConstants.Business = dat
                                        AppConstants.Business_Status = false
                                        navController.navigate(NavigationManager.BUSINESS_DETAIL_SCREEN)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

        },
    )
}


@Composable
fun UserItem(user: BusinessData, onClick: (user: BusinessData) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.fillMaxWidth().
        border(0.5.dp, colorResource(id = R.color.button_normal), RoundedCornerShape(15.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(10.dp)
                    .clickable {onClick(user)},
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Row {
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            border = BorderStroke(1.dp, color = colorResource(R.color.black)),
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp)
                        ) {
                            AsyncImage(
                                model = "",
                                contentDescription = "Grid Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(15.dp)) ,
                                placeholder = painterResource(id = R.drawable.business_placeholder),
                                error = painterResource(id = R.drawable.business_placeholder)
                            )
                        }


                        Spacer(Modifier.width(10.dp))

                        Column {
                            Text(text = user.Name,  fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = user.BusinessType, fontWeight = FontWeight.Normal, fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = user.Description, color = Color.Gray, fontSize = 12.sp)
                }

            }
        }
    }


}

@Composable
fun UserItemFollow(user: BusinessData, onClick: (user: VoyagerInfo) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {},
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Row {
                    AsyncImage(
                        model = "https://picsum.photos/200/300?random=1",
                        contentDescription = "Grid Image",
                        modifier = Modifier
                            .aspectRatio(1f) // Keeps all grid items square
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = user.Name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Text(text = "Lets Chat...", color = Color.Gray, fontSize = 12.sp)
            }

            Box(
                modifier = Modifier
                    .background(colorResource(R.color.button_normal), CircleShape)
                    .size(25.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                )
            }
        }
    }
}

