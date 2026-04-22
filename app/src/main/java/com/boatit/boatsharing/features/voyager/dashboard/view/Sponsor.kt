@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:max-line-length",
)

package com.boatit.boatsharing.features.voyager.dashboard.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.popBack
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageSponsorUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CreateVoyageSponsorViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.ICreateVoyageSponsorViewModel
import com.boatit.boatsharing.ui.components.CustomButton
import com.boatit.boatsharing.ui.components.CustomTextField
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.FormStepsViews
import org.koin.androidx.compose.koinViewModel

@Composable
fun SponsorScreen(
    navController: NavController,
    sponsorViewModel: ICreateVoyageSponsorViewModel = koinViewModel<CreateVoyageSponsorViewModel>(),
) {
    val context = LocalContext.current
    val isValidate = true
    val sponsorUiState by sponsorViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        sponsorViewModel.onEvent(CreateVoyageSponsorUiEvent.Initialize)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = "Add sponsors", onImageClick = {
                navController.popBackStack()
            })
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(
                            top = innerPadding.calculateTopPadding() + 15.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = innerPadding.calculateTopPadding() + 25.dp,
                        )
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
            ) {
                FormStepsViews(
                    numberOfViews = 1,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 1,
                )

                Spacer(Modifier.height(30.dp))

                CustomTextField(
                    textValue = sponsorUiState.searchQuery,
                    placeholderText = "Search",
                    onTextChange = {
                        sponsorViewModel.onEvent(CreateVoyageSponsorUiEvent.UpdateSearchQuery(it))
                    },
                    keyboardType = KeyboardType.Text,
                    maxChars = 100,
                    errorMessage = null,
                    isError = false,
                    onClearError = {},
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = {}),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.passengers),
                            contentDescription = "Icon",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified,
                        )
                    },
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                        ),
                    text = "Add Sponsors",
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    style =
                        TextStyle(
                            color = Color.DarkGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                    text = "Hey, sponsors are users you follow through the 'Connect with Voyagers' option in the Menu. All registered voyagers are shown in this list.",
                )

                Spacer(Modifier.height(10.dp))

                if (sponsorUiState.voyagersLoadError != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = sponsorUiState.voyagersLoadError.orEmpty(),
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = "Retry",
                            color = colorResource(id = R.color.button_normal),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.W600,
                            modifier =
                                Modifier.clickable {
                                    sponsorViewModel.onEvent(CreateVoyageSponsorUiEvent.LoadFollowedVoyagers)
                                },
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                }

                if (sponsorUiState.isVoyagersLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.button_normal),
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                }

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(400.dp) // Set a fixed height for the Card
                            .padding(5.dp),
                ) {
                    LazyColumn {
                        items(sponsorUiState.filteredFollowedVoyagers.size) { prediction ->
                            val user = sponsorUiState.filteredFollowedVoyagers[prediction]
                            val selectedSponsor = sponsorUiState.sponsorEntries.find { it.VoyagerUserId == user.UserId }
                            val isAlreadyAdded = selectedSponsor != null
                            val sponsorAmountText =
                                if ((selectedSponsor?.AmountToPay ?: 0.0) > 0.0) {
                                    selectedSponsor?.AmountToPay?.toString().orEmpty()
                                } else {
                                    ""
                                }

                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                            ) {
                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                if (!isAlreadyAdded) {
                                                    sponsorViewModel.onEvent(
                                                        CreateVoyageSponsorUiEvent.AddSponsor(
                                                            voyagerUserId = user.UserId,
                                                            voyagerUserName = user.FirstName,
                                                        ),
                                                    )
                                                    Toast.makeText(context, "Sponsor Added", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    sponsorViewModel.onEvent(
                                                        CreateVoyageSponsorUiEvent.RemoveSponsor(user.UserId),
                                                    )
                                                    Toast.makeText(context, "Sponsor Removed", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                ) {
                                    Row(
                                        modifier =
                                            Modifier
                                                .weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
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
                                        Text(
                                            text = user.FirstName,
                                            fontSize = 16.sp,
                                            color = Color.Black,
                                            modifier = Modifier.padding(0.dp),
                                        )
                                    }

                                    Checkbox(
                                        checked = isAlreadyAdded,
                                        onCheckedChange = null,
                                        colors =
                                            CheckboxDefaults.colors(
                                                checkedColor = colorResource(id = R.color.button_normal),
                                                uncheckedColor = Color.Gray,
                                                checkmarkColor = Color.White,
                                            ),
                                    )
                                }

                                if (isAlreadyAdded) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    CustomTextField(
                                        textValue = sponsorAmountText,
                                        placeholderText = "Sponsor amount",
                                        onTextChange = { value ->
                                            val amount = value.toDoubleOrNull() ?: 0.0
                                            sponsorViewModel.onEvent(
                                                CreateVoyageSponsorUiEvent.UpdateSponsorAmount(
                                                    voyagerUserId = user.UserId,
                                                    amountToPay = amount,
                                                ),
                                            )
                                        },
                                        keyboardType = KeyboardType.Decimal,
                                        maxChars = 8,
                                        errorMessage = null,
                                        isError = false,
                                        onClearError = {},
                                        imeAction = ImeAction.Done,
                                        keyboardActions = KeyboardActions(onDone = {}),
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                CustomButton(
                    text = "Back",
                    isValidate = isValidate,
                    isLoading = false,
                    onButtonClick = {
                        navController.popBack()
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        },
    )
}

@Preview
@Composable
fun SponsorScreen() {
    SponsorScreen(navController = rememberNavController())
}
