package com.boatit.boatsharing.ui.business.view


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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.ui.business.model.BusinessData
import com.boatit.boatsharing.ui.business.model.DockDropdownItem
import com.boatit.boatsharing.ui.business.viewmodel.GetBusinessViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDobField
import com.boatit.boatsharing.uihelpers.CustomDropDown
import com.boatit.boatsharing.uihelpers.CustomTextField
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel

@Composable
fun BusinessDashboard(navController: NavController, viewModel: GetBusinessViewModel = koinViewModel(),) {

    val focusManager = LocalFocusManager.current
    val businessDescriptionFocusRequester = remember { FocusRequester() }
    val options = listOf("Yes", "No")
    var BDetail by remember { mutableStateOf<BusinessData?>(null) }
    var shores by remember { mutableStateOf<List<DockDropdownItem>?>(null) }
    var zones by remember { mutableStateOf<List<DockDropdownItem>?>(null) }
    var island by remember { mutableStateOf<List<DockDropdownItem>?>(null) }
    var zone by remember { mutableStateOf("") }
    var shore by remember { mutableStateOf("") }
    var islnd by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var businessDescription by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isNetworkError by remember { mutableStateOf(false) }
    val isValidate = businessDescription.isNotEmpty()&&selectedOption.isNotEmpty()
    val fetchState by viewModel.loginState.collectAsState()
    val fetchDocksState by viewModel.docksState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var expandeds by remember { mutableStateOf(false) }
    var expandedi by remember { mutableStateOf(false) }

    val dummyImageUrls = listOf(
        "https://picsum.photos/200/300?random=1",
        "https://picsum.photos/200/300?random=2",
        "https://picsum.photos/200/300?random=3",
        "https://picsum.photos/200/300?random=4",
        "https://picsum.photos/200/300?random=5",
        "https://picsum.photos/200/300?random=6"
    )

    when (fetchState) {
        is NetworkResponse.Success -> {
            BDetail = fetchState.data?.obj
            isLoading = false
        }
        is NetworkResponse.Error -> {}
        else -> {}
    }

    when (fetchDocksState) {
        is NetworkResponse.Success -> {
            zones = fetchDocksState.data?.obj?.Zone
            shores = fetchDocksState.data?.obj?.Shore
            island = fetchDocksState.data?.obj?.Island
        }
        is NetworkResponse.Error -> {}
        else -> {}
    }

    val handleError = {
        errorMessage = null
        isError = false
    }

    LaunchedEffect(Unit) {
        viewModel.voyages()
        viewModel.docks()
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

                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(100.dp)
                                .padding(start = 20.dp, top = 20.dp),
                            contentAlignment = Alignment.TopStart,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.wheel_icon),
                                contentDescription = "Icon Image",
                                modifier = Modifier
                                    .size(width = 80.dp, height = 80.dp)
                                    .clickable {
                                        navController.navigate(NavigationManager.CAPTAIN_MENU_OPTIONS_SCREEN)
                                    }
                            )

                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            AsyncImage(
                                model = "https://testbyfarhan.squarecod.com/" + BDetail?.LogoPath,
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
                                text = BDetail?.BusinessType!!
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
                                    text = BDetail?.Location!!,
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
                                BDetail?.BusinessHours?.distinctBy { it.Day }?.forEach { hour ->
                                    Text(
                                        text = "${hour.Day}: ${hour.StartTime} - ${hour.EndTimeTime}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            text = stringResource(R.string.shores)
                        )

                        Spacer(Modifier.height(10.dp))

                        Box( modifier = Modifier.clickable { expandeds = true }){
                            CustomDobField(
                                textValue = shore,
                                placeholderText = stringResource(R.string.shores),
                                onTextChange = { shore = it },
                                keyboardType = KeyboardType.Email,
                                maxChars = 100,
                                errorMessage = if (shore.isNotEmpty()&& shore.length <= 3) stringResource(
                                    R.string.pickup_location_text) else null,
                                isError = shore.isNotEmpty()&& shore.length <= 3,
                                onClearError = handleError,
                                imeAction = ImeAction.Next,
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.location_icon),
                                        contentDescription = "Icon",
                                        modifier = Modifier.size(20.dp),
                                        tint = colorResource(R.color.button_normal)
                                    )
                                }

                            )

                            DropdownMenu(
                                expanded = expandeds,
                                onDismissRequest = { expandeds = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surface,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 4.dp)
                            ) {
                                shores?.forEach { category ->
                                    DropdownMenuItem(
                                        onClick = {
                                            expandeds = false
                                            shore = category.Name
                                        },
                                        text = {
                                            Text(
                                                text = category.Name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                            )
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            text = stringResource(R.string.zones)
                        )

                        Spacer(Modifier.height(10.dp))

                        Box( modifier = Modifier.clickable { expanded = true }){
                            CustomDobField(
                                textValue = zone,
                                placeholderText = stringResource(R.string.zones),
                                onTextChange = { zone = it },
                                keyboardType = KeyboardType.Email,
                                maxChars = 100,
                                errorMessage = if (zone.isNotEmpty()&& zone.length <= 3) stringResource(
                                    R.string.pickup_location_text) else null,
                                isError = zone.isNotEmpty()&& zone.length <= 3,
                                onClearError = handleError,
                                imeAction = ImeAction.Next,
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.location_icon),
                                        contentDescription = "Icon",
                                        modifier = Modifier.size(20.dp),
                                        tint = colorResource(R.color.button_normal)
                                    )
                                }

                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surface,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 4.dp)
                            ) {
                                zones?.forEach { category ->
                                    DropdownMenuItem(
                                        onClick = {
                                            expanded = false
                                            zone = category.Name
                                        },
                                        text = {
                                            Text(
                                                text = category.Name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                            )
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            text = stringResource(R.string.island)
                        )

                        Spacer(Modifier.height(10.dp))

                        Box( modifier = Modifier.clickable { expandedi = true }){
                            CustomDobField(
                                textValue = islnd,
                                placeholderText = stringResource(R.string.island),
                                onTextChange = { islnd = it },
                                keyboardType = KeyboardType.Email,
                                maxChars = 100,
                                errorMessage = if (islnd.isNotEmpty()&& islnd.length <= 3) stringResource(
                                    R.string.pickup_location_text) else null,
                                isError = islnd.isNotEmpty()&& islnd.length <= 3,
                                onClearError = handleError,
                                imeAction = ImeAction.Next,
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.location_icon),
                                        contentDescription = "Icon",
                                        modifier = Modifier.size(20.dp),
                                        tint = colorResource(R.color.button_normal)
                                    )
                                }

                            )

                            DropdownMenu(
                                expanded = expandedi,
                                onDismissRequest = { expandedi = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surface,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 4.dp)
                            ) {
                                island?.forEach { category ->
                                    DropdownMenuItem(
                                        onClick = {
                                            expandedi = false
                                            islnd = category.Name
                                        },
                                        text = {
                                            Text(
                                                text = category.Name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                            )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        CustomButton(
                            text = "Save Changes",
                            isValidate = isValidate,
                            isLoading = isLoading,
                            onButtonClick = {
                                isButtonEnabled = true
                                isLoading = true
                                focusManager.clearFocus()
                                println("perform network call")

                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun PreviewBusinessDashboard() {
    BusinessDashboard(navController = rememberNavController())
}