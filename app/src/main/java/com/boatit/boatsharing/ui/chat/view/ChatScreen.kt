package com.boatit.boatsharing.ui.chat.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.abanapps.socailqrscanner.data_layer.model.ChatMessage
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.chat.viewmodel.ChatViewModel
import com.boatit.boatsharing.uihelpers.ChatAppBar
import com.boatit.boatsharing.uihelpers.ComposableUtilsTextField
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.chromium.base.Flag
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(navController: NavController,
       chatId: String,
       currentUserId: String,
       name: String,
       senderId: String,
       viewModel: ChatViewModel = koinViewModel()
) {

    val messages by viewModel.messages.collectAsState(initial = emptyList())

    val messageText = remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    LaunchedEffect(chatId) {
        println(chatId)
        viewModel.listenForMessages(chatId, currentUserId)
        viewModel.markMessagesAsRead(chatId, senderId)
    }


    Box(modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.1f)

        )

        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {


            ChatAppBar(
                userName = "user name",
                userStatus = "pickup to drop off",
                onImageClick = { navController.popBack() }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                state = listState
            ) {
                items(messages) { message ->
                    MessageItem(
                        message = message,
                        currentUserId = currentUserId
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ComposableUtilsTextField(
                    value = messageText.value,
                    onValueChange = { messageText.value = it },
                    hint = "Type a Message",
                    keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
                    isError = false,
                    errorMessage = "",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )

                FloatingActionButton(
                    onClick = {
                        viewModel.sendMessage(chatId, currentUserId, messageText.value)
                        messageText.value = ""
                    },
                    containerColor = colorResource(R.color.button_normal),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }


    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable

fun MessageItem(message: ChatMessage, currentUserId: String) {
    val isCurrentUser = message.user == currentUserId
    var isFlagged by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isCurrentUser) {
            // Left-aligned messages (Receiver)
            Row(verticalAlignment = Alignment.Bottom) {
                Card(
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp, bottomStart = 0.dp),
                    colors = CardDefaults.cardColors(Color(0xFFF0F0F0)),
                    modifier = Modifier.widthIn(max = 300.dp)
                ) {
                    Text(
                        text = message.text ?: "No Chat",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isFlagged) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Flagged",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "message.timestamp",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Message Status",
                            tint = if (message.status == "read") Color.Blue else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        } else {
            // Right-aligned messages (Sender)
            Row(verticalAlignment = Alignment.Bottom) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Message Status",
                            tint = if (message.status == "read") Color.Blue else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text =" message.timestamp",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Card(
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp, bottomEnd = 0.dp),
                    colors = CardDefaults.cardColors(Color(0xFF007AFF)),
                    modifier = Modifier.widthIn(max = 300.dp)
                ) {
                    Text(
                        text = message.text ?: "No Chat",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

