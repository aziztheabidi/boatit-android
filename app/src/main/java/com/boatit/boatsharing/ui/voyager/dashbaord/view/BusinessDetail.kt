package com.boatit.boatsharing.ui.voyager.dashbaord.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BusinessData
import com.boatit.boatsharing.ui.voyager.dashbaord.model.CancelBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ConfirmBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FetchBusinessViewModel
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel

@Composable
fun BusinessDetail(navController: NavController, viewModel: FetchBusinessViewModel = koinViewModel(),) {

    val focusManager = LocalFocusManager.current
    var selectedOption by remember { mutableStateOf("") }
    var businessDescription by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    val isValidate = businessDescription.isNotEmpty()&&selectedOption.isNotEmpty()
    val fetchState by viewModel.loginState.collectAsState()

    val dummyImageUrls = listOf(
        "https://picsum.photos/200/300?random=1",
        "https://picsum.photos/200/300?random=2",
        "https://picsum.photos/200/300?random=3",
        "https://picsum.photos/200/300?random=4",
        "https://picsum.photos/200/300?random=5",
        "https://picsum.photos/200/300?random=6"
    )

    val handleError = {
        errorMessage = null
        isError = false
    }

    LaunchedEffect(Unit) {
        println(AppConstants.Business)
    }

    Scaffold(
        content = { innerPadding ->
            if (isLoading) {
                Dialog(
                    onDismissRequest = {},
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .background(White, shape = RoundedCornerShape(8.dp))
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White) // 👈 White background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = innerPadding.calculateTopPadding() + 25.dp,
                            )
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            AsyncImage(
                                model = "https://testbyfarhan.squarecod.com/" + AppConstants.Business?.LogoPath,
                                contentDescription = "Grid Image",
                                modifier = Modifier
                                    .height(110.dp)
                                    .width(110.dp)// Keeps all grid items square
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.height(20.dp))

                            Text(
                                style = TextStyle(
                                    color = Color.Blue,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                text = "Sky Boating Ltd"
                            )

                            Text(
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                text = AppConstants.Business?.BusinessType!!
                            )

                            Spacer(Modifier.height(20.dp))

                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(10.dp), // Corner radius
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray, // Border color
                                        shape = RoundedCornerShape(20.dp) // Apply same corner radius to border
                                    ),
                                colors = ButtonDefaults.buttonColors(containerColor = White)
                            ) {
                                Text(
                                    text = "Established In : 19 November 2010",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = colorResource(id = R.color.black) // Text color matches border
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Normal
                                ),
                                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                            )
                        }

                        Spacer(Modifier.height(30.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
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
                                    text = stringResource(R.string.follow),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
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
                                ,
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal))
                            ) {
                                Text(
                                    text = stringResource(R.string.add_dock),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(Modifier.height(30.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            text = "Gallery"
                        )

                        Spacer(Modifier.height(20.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(dummyImageUrls.size) { url ->
                                AsyncImage(
                                    model = dummyImageUrls[url],
                                    contentDescription = "Grid Image",
                                    modifier = Modifier
                                        .aspectRatio(1f) // Keeps all grid items square
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            text = "Location"
                        )

                        Spacer(Modifier.height(10.dp))


                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = White)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                            ) {
                                Text(
                                    text = AppConstants.Business?.Location!!,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            text = "Business Hours"
                        )

                        Spacer(Modifier.height(10.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = White)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AppConstants.Business?.BusinessHours?.distinctBy { it.Day }?.forEach { hour ->
                                    Text(
                                        text = "${hour.Day}: ${hour.StartTime} - ${hour.EndTimeTime}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                    }
                }
            }
        },
    )
}
