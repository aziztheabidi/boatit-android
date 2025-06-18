package com.boatit.boatsharing.ui.voyager.dashbaord.view

import android.widget.Toast
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
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.ui.chat.model.VoyagerInfo
import com.boatit.boatsharing.ui.chat.viewmodel.FollowViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BusinessData
import com.boatit.boatsharing.ui.voyager.dashbaord.model.SponsorVoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyageNotification
import com.boatit.boatsharing.ui.voyager.dashbaord.view.FutureConfirmVoyagerItems
import com.boatit.boatsharing.ui.voyager.dashbaord.view.FutureVoyagerItems
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FetchBusinessViewModel


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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0))
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(15.dp)
        ) {
            Column(modifier = Modifier
                .padding(
                    top = 15.dp,
                    start = 20.dp,
                    end = 20.dp,
                )
                .fillMaxSize()) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color.White
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    color = if (selectedTabIndex == index) Color.White else colorResource(R.color.button_normal)
                                )
                            },
                            modifier = Modifier.background(if (selectedTabIndex == index) colorResource(R.color.button_normal)else Color.White)
                        )
                    }
                }
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
                                    items(voyagesList.data!!.obj.Followed.size) {}
                                }
                            }
                        }
                    }
                    1 ->   LazyColumn(
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
                                items(voyagesList.data!!.obj.UnFollowed.size) { user ->
                                    UserItem(voyagesList.data!!.obj.UnFollowed.get(user)) { dat ->
                                        AppConstants.Business = dat
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
}


@Composable
fun UserItem(user: BusinessData, onClick: (user: BusinessData) -> Unit) {

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth().
        padding(10.dp).
        border(1.dp, Color.Blue, RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {onClick(user)},
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Row {
                        AsyncImage(
                            model = "https://picsum.photos/200/300?random=1",
                            contentDescription = "Grid Image",
                            modifier = Modifier
                                .width(80.dp)
                                .height(80.dp)
                                .aspectRatio(1f) // Keeps all grid items square
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.width(10.dp))

                        Column {
                            Text(text = user.Name,  fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = user.BusinessType, fontWeight = FontWeight.Normal, fontSize = 14.sp)
                        }
                    }
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

