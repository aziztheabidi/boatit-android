package com.boatit.boatsharing.features.chat.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.navigation.InteractionRoutes
import com.boatit.boatsharing.ui.navigation.popBack
import com.boatit.boatsharing.features.chat.model.FollowRequest
import com.boatit.boatsharing.features.chat.model.VoyagerInfo
import com.boatit.boatsharing.features.chat.viewmodel.FollowViewModel
import com.boatit.boatsharing.features.chat.viewmodel.VoyagersListViewModel
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get
import java.time.format.TextStyle

@Composable
fun VoyagersListScreen(
    navController: NavController,
    viewModel: VoyagersListViewModel = koinViewModel(),
    viewModelF: FollowViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val voyagesList by viewModel.loginState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    var followed = viewModel.filteredBoatListFollowed
    var allusers = viewModel.filteredBoatList
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Followed", "All")
    val followState by viewModelF.nearbyPlaces.collectAsState()
    val searchQuery = viewModel.searchQuery
    val userSessionStore: UserSessionStore = get(UserSessionStore::class.java)
    val currentUserId = userSessionStore.currentUserId()

    when (voyagesList) {
        is NetworkResponse.Loading -> {
        }
        is NetworkResponse.Error -> {
            viewModel.resetNearbyPlaces()
        }
        is NetworkResponse.Success -> {
            voyagesList.data?.let { viewModel.onBoatList(it) }
            viewModel.resetNearbyPlaces()
        }
    }

    when (followState) {
        is NetworkResponse.Success -> {
            isLoading = false
            isNetworkError = false
            viewModelF.resetNearbyPlaces()
            viewModel.voyages()
            Toast.makeText(context, "Voyager Followed", Toast.LENGTH_SHORT).show()
        }
        is NetworkResponse.Error -> {
            isLoading = false
            isNetworkError = false
            viewModelF.resetNearbyPlaces()
        }
        else -> {}
    }

    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0)),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { navController.popBack() },
                modifier = Modifier.padding(start = 5.dp, end = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                )
            }

            TextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = {
                    Text(
                        text = "Search voyagers",
                        color = Color.Gray,
                        fontSize = 12.sp,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.DarkGray,
                    )
                },
                textStyle =
                    androidx.compose.ui.text.TextStyle(
                        fontSize = 12.sp,
                        color = Color.Black,
                    ),
                singleLine = true,
                shape = RoundedCornerShape(50),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .background(Color(0xFFF7F7F8), RoundedCornerShape(50)),
                // light gray bg
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        unfocusedTextColor = Color.Gray,
                    ),
            )
        }
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(
                            top = 15.dp,
                            start = 10.dp,
                            end = 10.dp,
                        )
                        .fillMaxSize(),
            ) {
                Box(
                    modifier =
                        Modifier
                            .height(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = 0.5.dp,
                                color = colorResource(R.color.button_normal),
                                shape = RoundedCornerShape(10.dp),
                            ),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White),
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            val isSelected = selectedTabIndex == index
                            val shape =
                                if (isSelected) {
                                    RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                                } else {
                                    RoundedCornerShape(0.dp)
                                }
                            val offsetModifier = if (isSelected) Modifier.offset(x = 0.dp, y = (-1).dp) else Modifier

                            Box(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .then(offsetModifier)
                                        .clip(shape)
                                        .background(
                                            if (isSelected) colorResource(R.color.button_normal) else Color.White,
                                        )
                                        .clickable { selectedTabIndex = index },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = title,
                                    color = if (isSelected) Color.White else colorResource(R.color.button_normal),
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                when (selectedTabIndex) {
                    0 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(followed.size) { user ->
                                UserItemFollow(followed.get(user)) { dat ->
                                    val chatId = generateChatId(dat.UserId, currentUserId)
                                    navController.navigate(
                                        route =
                                            InteractionRoutes.chat(
                                                chatId = chatId,
                                                currentUserId = dat.UserId,
                                                name = dat.FirstName,
                                                senderId = currentUserId,
                                            ),
                                    )
                                }
                            }
                        }
                    }
                    1 ->
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(allusers.size) { user ->
                                UserItem(allusers.get(user)) { dat ->
                                    val chatId = generateChatId(dat.UserId, currentUserId)

                                    navController.navigate(
                                        route =
                                            InteractionRoutes.chat(
                                                chatId = chatId,
                                                currentUserId = dat.UserId,
                                                name = dat.FirstName,
                                                senderId = currentUserId,
                                            ),
                                    )
                                }
                            }
                        }
                }
            }
        }
    }
}

fun generateChatId(
    userId1: String,
    userId2: String,
): String {
    return if (userId1 < userId2) "$userId1-$userId2" else "$userId2-$userId1"
}

@Composable
fun UserItem(
    user: VoyagerInfo,
    viewModel: FollowViewModel = koinViewModel(),
    onClick: (user: VoyagerInfo) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(10.dp),
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .clickable { onClick(user) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier =
                    Modifier
                        .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Circular letter
                Box(
                    modifier =
                        Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = user.FirstName.firstOrNull()?.uppercase() ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name and subtitle
                Column {
                    Text(
                        text = user.FirstName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "Let's Chat...",
                        color = Color.Gray,
                        fontSize = 12.sp,
                    )
                }
            }

//            Column (
//                modifier = Modifier
//                .padding(start = 10.dp, end = 10.dp)){
//                Text(text = user.FirstName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                Text(text = "Lets Chat...", color = Color.Gray, fontSize = 12.sp)
//            }

            Button(
                onClick = {
                    viewModel.followFunc(
                        FollowRequest(
                            VoyagerUserId = user.UserId,
                        ),
                    )
                },
                shape = RoundedCornerShape(10.dp),
                modifier =
                    Modifier
                        .width(90.dp)
                        .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal)),
            ) {
                Text(
                    text = "Follow",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun UserItemFollow(
    user: VoyagerInfo,
    onClick: (user: VoyagerInfo) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
//            .clickable { onClick(user) }
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier =
                    Modifier
                        .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Circular letter
                Box(
                    modifier =
                        Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = user.FirstName.firstOrNull()?.uppercase() ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name and subtitle
                Column {
                    Text(
                        text = user.FirstName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "Let's Chat...",
                        color = Color.Gray,
                        fontSize = 12.sp,
                    )
                }
            }

            Button(
                onClick = {
                    onClick(user)
                },
                shape = RoundedCornerShape(10.dp),
                modifier =
                    Modifier
                        .width(100.dp)
                        .height(35.dp)
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.button_normal), // Border color
                            shape = RoundedCornerShape(10.dp), // Apply same corner radius to border
                        ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            ) {
                Text(
                    text = "Message",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.button_normal),
                )
            }

//            Column (
//                modifier = Modifier
//                    .padding(start = 10.dp, end = 10.dp)
//            ){
//                Text(text = user.FirstName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                Text(text = "Lets Chat...", color = Color.Gray, fontSize = 12.sp)
//            }
        }
    }
}
