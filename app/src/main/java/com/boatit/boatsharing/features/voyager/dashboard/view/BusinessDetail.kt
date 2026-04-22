@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:discouraged-comment-location",
)

package com.boatit.boatsharing.features.voyager.dashboard.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.LocalContext
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
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.navigation.navigateToVoyagerDashboard
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessRequest
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.BusinessSelectionStore
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FindBoatPrefillStore
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.VoyagerFollowBusinessViewModel
import com.boatit.boatsharing.ui.components.BusinessAddToVoyage
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@Composable
fun BusinessDetail(
    navController: NavController,
    viewModel: VoyagerFollowBusinessViewModel = koinViewModel(),
    businessSelectionStore: BusinessSelectionStore = get(BusinessSelectionStore::class.java),
) {
    val focusManager = LocalFocusManager.current
    var selectedOption by remember { mutableStateOf("") }
    var businessDescription by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val businessSelectionState by businessSelectionStore.state.collectAsState()
    val selectedBusiness = businessSelectionState.business
    val findBoatPrefillStore: FindBoatPrefillStore = get(FindBoatPrefillStore::class.java)
    val imageBasePath = stringResource(id = R.string.path)
    val isValidate = businessDescription.isNotEmpty() && selectedOption.isNotEmpty()
    val fetchState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    if (selectedBusiness == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    when (fetchState) {
        is NetworkResponse.Success -> {
            if (isLoading) {
                isLoading = false
                if (businessSelectionState.isFollowed) {
                    Toast.makeText(context, "Business Followed Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Business Un-Followed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        is NetworkResponse.Error -> {
            if (isLoading) {
                isLoading = false
            }
        }
        else -> {}
    }

    Scaffold(
        content = { innerPadding ->
            if (isLoading) {
                Dialog(
                    onDismissRequest = {},
                    properties =
                        DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false,
                        ),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier =
                            Modifier
                                .size(100.dp)
                                .background(White, shape = RoundedCornerShape(8.dp)),
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(White),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    bottom = innerPadding.calculateTopPadding() + 25.dp,
                                )
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                    ) {
                        Spacer(Modifier.height(50.dp))
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Card(
                                shape = RoundedCornerShape(15.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                border = BorderStroke(1.dp, color = colorResource(R.color.black)),
                                modifier =
                                    Modifier
                                        .width(110.dp)
                                        .height(110.dp),
                            ) {
                                AsyncImage(
                                    model = imageBasePath + selectedBusiness.LogoPath,
                                    contentDescription = "Grid Image",
                                    modifier =
                                        Modifier
                                            .height(110.dp)
                                            .width(110.dp) // Keeps all grid items square
                                            .clip(RoundedCornerShape(15.dp)),
                                    contentScale = ContentScale.Crop,
                                )
                            }

                            Spacer(Modifier.height(20.dp))

                            Text(
                                style =
                                    TextStyle(
                                        color = colorResource(id = R.color.button_normal),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Normal,
                                    ),
                                text = selectedBusiness.Name,
                            )

                            Text(
                                style =
                                    TextStyle(
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                    ),
                                text = selectedBusiness.BusinessType,
                            )

                            Spacer(Modifier.height(20.dp))

                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(10.dp), // Corner radius
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(35.dp)
                                        .border(
                                            width = 1.dp,
                                            color = Color.Gray, // Border color
                                            shape = RoundedCornerShape(20.dp), // Apply same corner radius to border
                                        ),
                                colors = ButtonDefaults.buttonColors(containerColor = White),
                            ) {
                                Text(
                                    text = "Established In : " + selectedBusiness.YearOfEstablishment,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.black), // Text color matches border
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                style =
                                    TextStyle(
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Normal,
                                    ),
                                text = selectedBusiness.Description,
                            )
                        }

                        Spacer(Modifier.height(30.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Button(
                                onClick = {
                                    isLoading = true
                                    if (businessSelectionState.isFollowed) {
                                        businessSelectionStore.updateFollowState(false)
                                        viewModel.VoyagerUnFollowFunc(
                                            VoyagerFollowBusinessRequest(
                                                BusinessDockId = selectedBusiness.Id,
                                            ),
                                        )
                                    } else {
                                        businessSelectionStore.updateFollowState(true)
                                        viewModel.VoyagerFeedbackFunc(
                                            VoyagerFollowBusinessRequest(
                                                BusinessDockId = selectedBusiness.Id,
                                            ),
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(15.dp),
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal)),
                            ) {
                                Text(
                                    text =
                                        if (businessSelectionState.isFollowed) {
                                            stringResource(
                                                R.string.un_follow,
                                            )
                                        } else {
                                            stringResource(R.string.follow)
                                        },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = White,
                                )
                            }
                            Button(
                                onClick = { showDialog = true },
                                shape = RoundedCornerShape(15.dp), // Corner radius
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .border(
                                            width = 1.dp,
                                            color = colorResource(id = R.color.button_normal), // Border color
                                            shape = RoundedCornerShape(10.dp), // Apply same corner radius to border
                                        ),
                                colors = ButtonDefaults.buttonColors(containerColor = White),
                            ) {
                                Text(
                                    text = stringResource(R.string.add_to_voyage),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = colorResource(id = R.color.button_normal), // Text color matches border
                                )
                            }
                        }

                        Spacer(Modifier.height(30.dp))

                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                ),
                            text = "Gallery",
                        )

                        Spacer(Modifier.height(20.dp))

                        val imageCount = selectedBusiness.ImagesPath.size
                        val columns = 3
                        val itemSize = 90.dp
                        val spacing = 8.dp
                        val rows = (imageCount + columns - 1) / columns
                        val totalHeight = (itemSize * rows) + (spacing * (rows - 1)) + 16.dp

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(totalHeight),
                            // Calculated exact height
                            userScrollEnabled = false,
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(spacing),
                            horizontalArrangement = Arrangement.spacedBy(spacing),
                        ) {
                            items(selectedBusiness.ImagesPath.size) { urlIndex ->

                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                    border = BorderStroke(1.dp, color = colorResource(R.color.black)),
                                    modifier =
                                        Modifier
                                            .size(itemSize),
                                ) {
                                    AsyncImage(
                                        model = imageBasePath + selectedBusiness.ImagesPath[urlIndex],
                                        contentDescription = "Grid Image",
                                        modifier =
                                            Modifier
                                                .size(itemSize)
                                                .clip(RoundedCornerShape(15.dp)),
                                        contentScale = ContentScale.Crop,
                                        placeholder = painterResource(id = R.drawable.business_placeholder),
                                        error = painterResource(id = R.drawable.business_placeholder),
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                ),
                            text = "Location",
                        )

                        Spacer(Modifier.height(10.dp))

                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(45.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Removed elevation
                            border = BorderStroke(1.dp, Color.Black),
                            colors = CardDefaults.cardColors(containerColor = White),
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                            ) {
                                Text(
                                    text = selectedBusiness.Location,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                ),
                            text = "Business Hours",
                        )

                        Spacer(Modifier.height(10.dp))

                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Removed elevation
                            border = BorderStroke(1.dp, Color.Black),
                            colors = CardDefaults.cardColors(containerColor = White),
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                selectedBusiness.BusinessHours.distinctBy { it.Day }.forEach { hour ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Text(
                                            text = hour.Day,
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                                        )

                                        Text(
                                            text = "${hour.StartTime} - ${hour.EndTimeTime}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (showDialog) {
                        BusinessAddToVoyage(
                            onPickupSelected = {
                                findBoatPrefillStore.prefillPickup(
                                    dockId = selectedBusiness.Id,
                                    dockName = selectedBusiness.Name,
                                )
                                showDialog = false
                                navController.navigateToVoyagerDashboard()
                            },
                            onDestinationSelected = {
                                findBoatPrefillStore.prefillDropOff(
                                    dockId = selectedBusiness.Id,
                                    dockName = selectedBusiness.Name,
                                )
                                showDialog = false
                                navController.navigateToVoyagerDashboard()
                            },
                            onDismissRequest = {
                                showDialog = false
                            },
                        )
                    }
                }
            }
        },
    )
}
