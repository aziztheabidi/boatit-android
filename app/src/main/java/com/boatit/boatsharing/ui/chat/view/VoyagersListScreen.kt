package com.boatit.boatsharing.ui.chat.view

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
import com.boatit.boatsharing.ui.chat.model.VoyagerProfile
import com.boatit.boatsharing.ui.chat.viewmodel.VoyagersListViewModel
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.alpha
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.res.colorResource



@Composable
fun VoyagersListScreen(navController: NavController, viewModel: VoyagersListViewModel = koinViewModel()) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val voyagesList by viewModel.loginState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.voyages()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.padding(start = 5.dp, end = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(20.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Search messages",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(15.dp)
        ) {
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
                        items(voyagesList.data!!.obj.size) { user ->
                            UserItem(voyagesList.data!!.obj.get(user)) { dat ->
                                val chatId = generateChatId(dat.UserId, AppConstants.USER_ID!!)
                                println(chatId)
                                navController.navigate(route = "$CHAT_SCREEN/${chatId}/${dat.UserId}/${dat.FirstName}/${AppConstants.USER_ID}")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun generateChatId(userId1: String, userId2: String): String {
    return if (userId1 < userId2) "$userId1-$userId2" else "$userId2-$userId1"
}

@Composable
fun UserItem(user: VoyagerProfile, onClick: (user: VoyagerProfile) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().
            clickable { onClick(user) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(text = user.FirstName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Lets Chat...", color = Color.Gray, fontSize = 12.sp)
            }

            Box(
                modifier = Modifier.background(colorResource(R.color.button_normal), CircleShape).size(25.dp),
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

