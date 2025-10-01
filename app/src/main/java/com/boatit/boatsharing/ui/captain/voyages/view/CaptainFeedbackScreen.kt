package com.boatit.boatsharing.ui.captain.availabilitystatus

import VoyageData
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.ui.captain.dashboard.model.CaptainFeedbackRequest
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.CaptainFeedbackViewModel
import com.gowtham.ratingbar.RatingBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun CaptainFeedbackScreen(navController: NavController,notification : String,
                          viewModelFb: CaptainFeedbackViewModel = koinViewModel(), ) {
    var title by remember { mutableStateOf("Your voyage has been ended!") }
    var image by remember { mutableIntStateOf(R.drawable.wheel_inactive) }
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0f) }

    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val feedbackState by viewModelFb.loginState.collectAsState()

    when (feedbackState) {
        is NetworkResponse.Success -> {
            if (isLoading) {
                isLoading = false
                Toast.makeText(context, "Feedback Submitted", Toast.LENGTH_SHORT).show()
                navController.navigateWithClearStack(NavigationManager.CAPTAIN_DASHBOARD_SCREEN, clearStack = true)
            }
        }
        is NetworkResponse.Error -> {
            if (isLoading) {
                isLoading = false
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    Box(modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Thank you for your services, give reviews to the voyage so that next voyager can get benefited",
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))


            RatingBar(
                value = rating,
                painterEmpty = painterResource(id = R.drawable.location_icon_two),
                painterFilled = painterResource(id = R.drawable.location_icon_fill),
                onValueChange = {
                    rating = it
                },
                onRatingChanged = {
                }
            )


            Spacer(modifier = Modifier.height(24.dp))


            TextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, colorResource(id = R.color.bars_colour), shape = RoundedCornerShape(8.dp)),
                placeholder = { Text("Write your review here...") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                ),
                maxLines = 10,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigateWithClearStack(NavigationManager.CAPTAIN_DASHBOARD_SCREEN, clearStack = true)

                    },
                    shape = RoundedCornerShape(10.dp), // Corner radius
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.button_normal), // Border color
                            shape = RoundedCornerShape(10.dp) // Apply same corner radius to border
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "Later",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.button_normal) // Text color matches border
                    )
                }

                Button(
                    onClick = {
                        isLoading = true
                        viewModelFb.captainFeedbackFunc(CaptainFeedbackRequest(notification, rating.toInt(), reviewText ))
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 1.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal))
                ) {
                    Text(
                        text = stringResource(R.string.fb_submit_text),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

