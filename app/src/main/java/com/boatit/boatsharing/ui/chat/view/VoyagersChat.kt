package com.boatit.boatsharing.ui.chat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R

@Composable
fun VoyagerChat(navController: NavController) {
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
        // First Message
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Bhuban K.C", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "I see, thanks for informing!", color = Color.Gray)
                }
                Box(
                    modifier = Modifier
                        .background(colorResource(R.color.button_normal), CircleShape)
                        .size(25.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "2", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Second Message
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(15.dp)
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Elina", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "Initate chat... ", color = Color.Gray)
                }

                Box(
                    modifier = Modifier

                        .size(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.message_icon),
                        contentDescription = "Message",
                        tint = Color.Unspecified, modifier = Modifier.size(50.dp)
                    )
                }
            }
            // Map Pin Icon (Overlay)
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Map Pin",
                modifier = Modifier
                    .size(48.dp)
                    .alpha(0.1f),
                tint = Color.Gray
            )
            // Large "V" (Bottom Right)
            Text(
                text = "V",
                fontSize = 40.sp,
                color = Color(0xFF3F51B5),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .alpha(0.1f)
            )
        }
    }
}

@Preview
@Composable
fun PreviewVoyagerChat() {
    VoyagerChat(navController = rememberNavController())
}