package com.boatit.boatsharing.ui.chat.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.utils.AppConstants

@Composable
fun RateYourVoyage(navController: NavController) {

    val focusManager = LocalFocusManager.current
    val reviewFocusRequester = remember { FocusRequester() }

    var reviews by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var reviewText by remember { mutableStateOf("") }
    var ratingText by remember { mutableStateOf("Good") }

    Box(modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,

                    modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.1f),


        )
        Column(
            modifier = Modifier.background(Color.White)
                .fillMaxSize()
                .padding(16.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Your Voyage has been ended!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "Thank your for your ride, give reviews to the captain so that next voyager can get benefitted",
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Rating Icons
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                repeat(5) { index ->
                    Icon(
                        painter = painterResource(id = R.drawable.location_icon_two),// Replace with your rating icon
                        contentDescription = "Rating",
                        modifier = Modifier
                            .size(56.dp)
                            .padding(4.dp)
                            .clickable {
                                ratingText = when (index) {
                                    0 -> "Poor"
                                    1 -> "Good"
                                    2 -> "Very Good"
                                    3 -> "Excellent"
                                    else -> "Outstanding"
                                }
                            },
                        tint = Color.Unspecified// Replace with your icon tint
                    )
                }
            }

            // Rating Text
            Text(
                text = ratingText,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(Modifier.height(10.dp))

            CustomTextField(
                textValue = reviews,
                placeholderText = "Write your review here",
                onTextChange = { reviews = it },
                keyboardType = KeyboardType.Text,
                maxChars = 500,
                singleLine = false,
                minLines= 10,
                errorMessage = null,
                isError = false,
                onClearError = {},
                imeAction = ImeAction.Done,
                showTrailingIcon = false,
                keyboardActions = KeyboardActions(
                    // onNext = { businessTypeFocusRequester.requestFocus() }
                ),
                focusRequester = reviewFocusRequester
            )
            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds spacing between buttons
            ) {


                Button(
                    onClick = { },
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
                        text = "Cancel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.button_normal) // Text color matches border
                    )
                }

                Button(
                    onClick = {

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
                        text = "Submit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

            }

        }
    }
}

@Preview
@Composable
fun PreviewRateYourVoyage() {
    RateYourVoyage(navController = rememberNavController())
}