package com.boatit.boatsharing.ui.business.view


import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.AsyncImage
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.ui.business.model.BusinessData
import com.boatit.boatsharing.ui.business.model.BusinessRequest
import com.boatit.boatsharing.ui.business.model.DeleteRequest
import com.boatit.boatsharing.ui.business.model.DockDropdownItem
import com.boatit.boatsharing.ui.business.viewmodel.BusinessDashViewModel
import com.boatit.boatsharing.ui.business.viewmodel.GetBusinessViewModel
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashboard.view.AcceptVoyagerRequest
import com.boatit.boatsharing.ui.signup.business.viewmodel.BusinessLogoViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomDobField
import com.boatit.boatsharing.uihelpers.SessionDialog
import com.boatit.boatsharing.utils.AppConstants
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessDashboard(navController: NavController,
    viewModelUpdate: BusinessDashViewModel = koinViewModel(),
    viewModelGallery: BusinessLogoViewModel = koinViewModel(),
    viewModel: GetBusinessViewModel = koinViewModel()) {

    val focusManager = LocalFocusManager.current
    val businessDescriptionFocusRequester = remember { FocusRequester() }
    val options = listOf("Yes", "No")
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val galleryState by viewModelGallery.registrationState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var businessDetail by remember { mutableStateOf<BusinessData?>(null) }
    var shores by remember { mutableStateOf<List<DockDropdownItem>?>(null) }
    var zones by remember { mutableStateOf<List<DockDropdownItem>?>(null) }
    var island by remember { mutableStateOf<List<DockDropdownItem>?>(null) }
    val zone = remember { mutableStateOf<Pair<Int, String>?>(Pair(1, "")) }
    val shore = remember { mutableStateOf<Pair<Int, String>?>(Pair(1, "")) }
    val islnd = remember { mutableStateOf<Pair<Int, String>?>(Pair(1, "")) }
    var selectedOption by remember { mutableStateOf("") }
    var businessDescription by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isNetworkError by remember { mutableStateOf(false) }
    val fetchState by viewModel.loginState.collectAsState()
    val registrationState by viewModelUpdate.registrationState.collectAsState()
    val fetchDocksState by viewModel.docksState.collectAsState()
    val logoutEvent by viewModel.logoutEvent.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var isShoreExpanded by remember { mutableStateOf(false) }
    var isIslandExpanded by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    val editableList = remember(businessDetail?.BusinessHours) {
        businessDetail?.BusinessHours?.distinctBy { it.Day }?.map { it.copy() }?.toMutableStateList() ?: mutableStateListOf()
    }

    when (registrationState) {
        is NetworkResponse.Success -> {
            if(isLoading){
                isLoading = true
                isNetworkError = false
                viewModelUpdate.resetNearbyPlaces()
                viewModel.voyages()
                Toast.makeText(context, registrationState.data?.Message , Toast.LENGTH_SHORT).show()
            }
        }
        is NetworkResponse.Error -> {
            if(isLoading){
                isLoading = false
                isNetworkError = true
                errorMessage = "Network error, please try again."
                Toast.makeText(context, (registrationState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
                viewModelUpdate.resetNearbyPlaces()
            }
        }
        else -> {}
    }

    when (fetchState) {
        is NetworkResponse.Success -> {
            businessDetail = fetchState.data?.obj
            zone.value = Pair(businessDetail?.ZoneId!!, businessDetail?.ZoneName!!)
            shore.value = Pair(businessDetail?.ShoreId!!, businessDetail?.ShoreName!!)
            islnd.value = Pair(businessDetail?.IslandId!!, businessDetail?.IslandName!!)
            AppConstants.Busines_DOCK = businessDetail?.IsDock!!
            isLoading = false
            viewModel.resetNearbyPlaces()
        }
        is NetworkResponse.Error -> {
            viewModel.resetNearbyPlaces()
        }
        else -> {}
    }

    when (fetchDocksState) {
        is NetworkResponse.Success -> {
            zones = fetchDocksState.data?.obj?.Zone
            shores = fetchDocksState.data?.obj?.Shore
            island = fetchDocksState.data?.obj?.Island
            viewModel.resetDocks()
        }
        is NetworkResponse.Error -> {
            viewModel.resetDocks()
        }
        else -> {}
    }

    when (galleryState) {
        is NetworkResponse.Success -> {
            if(isLoading){
                isLoading = true
                isNetworkError = false
                Toast.makeText(context, galleryState.data?.Message , Toast.LENGTH_SHORT).show()
                viewModel.voyages()
                viewModelGallery.resetNearbyPlaces()

            }
        }
        is NetworkResponse.Error -> {
            if(isLoading){
                isLoading = false
                isNetworkError = true
                errorMessage = "Network error, please try again."
                Toast.makeText(context, (galleryState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
                viewModelGallery.resetNearbyPlaces()
            }
        }
        else -> {}
    }

    val handleError = {
        errorMessage = null
        isError = false
    }

    fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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
                        .background(White)
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
                                        navController.navigate(NavigationManager.BUSINESS_MENU_OPTIONS_SCREEN)
                                    }
                            )

                        }
                        Spacer(Modifier.height(50.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Card(
                                shape = RoundedCornerShape(15.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                border = BorderStroke(1.dp, color = colorResource(R.color.black)),
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(110.dp)
                            ) {
                                AsyncImage(
                                    model = AppConstants.IMG_PATH + businessDetail?.LogoPath,
                                    contentDescription = "Grid Image",
                                    modifier = Modifier
                                        .height(110.dp)
                                        .width(110.dp)// Keeps all grid items square
                                        .clip(RoundedCornerShape(15.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(Modifier.height(20.dp))

                            Text(
                                style = TextStyle(
                                    color = colorResource(id = R.color.button_normal),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                text = businessDetail?.Name!!
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                text = businessDetail?.BusinessType!!
                            )

                            Spacer(Modifier.height(20.dp))

                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(10.dp), // Corner radius
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(35.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray, // Border color
                                        shape = RoundedCornerShape(20.dp) // Apply same corner radius to border
                                    ),
                                colors = ButtonDefaults.buttonColors(containerColor = White)
                            ) {
                                Text(
                                    text = "Established In : " + businessDetail?.YearOfEstablishment,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.black) // Text color matches border
                                )
                            }

                            Spacer(Modifier.height(20.dp))


                            Text(
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Normal
                                ),
                                text = businessDetail?.Description!!,
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


                        val imageList = remember {
                            mutableStateListOf<String>().apply {
                                businessDetail?.ImagesPath?.let { addAll(it) }
                            }
                        }

                        val galleryLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetMultipleContents()
                        ) { uris ->
                            val remainingSlots = 6 - imageList.size
                            if (remainingSlots > 0) {
                                val newUris = uris.take(remainingSlots).map { it.toString() }
                                imageList.addAll(newUris)
                                val fileList = uris.take(remainingSlots).map { uri -> uriToFile(context, uri) }
                                if (fileList.isNotEmpty()) {
                                    isLoading = true
                                    viewModelGallery.uploadBusinessGallery (AppConstants.USER_ID!!,fileList)
                                }
                               }
                        }

                        ImageGridWithAddOption(
                            imageList = imageList,
                            onAddImageClick = {
                                galleryLauncher.launch("image/*")
                            },
                            onRemoveImage = { index ->
                                viewModelUpdate.deleteImage(profile = DeleteRequest(
                                    userId = AppConstants.USER_ID!!,
                                    path = imageList.get(index)
                                ))
                                imageList.removeAt(index)
                                isLoading = true
                            }
                        )



                        Spacer(Modifier.height(20.dp))

                        EditableLocationSection(
                            navController,
                            location = businessDetail?.Location ?: ""
                        )

                        Spacer(Modifier.height(20.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Business Hours",
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                            IconButton(onClick = { isEditing = !isEditing }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = colorResource(R.color.button_normal)
                                )
                            }
                        }


                        Spacer(Modifier.height(10.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Removed elevation
                            border = BorderStroke(1.dp, Color.Black), // Added black border
                            colors = CardDefaults.cardColors(containerColor = White)
                        ) {
                            val scrollState = rememberScrollState()

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(scrollState)
                                    .padding(10.dp), // Add padding to the container
                                verticalArrangement = Arrangement.spacedBy(12.dp) // Spacing between rows
                            ) {
                                editableList.forEachIndexed { index, hour ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                            Text(
                                                text = hour.Day.orEmpty(),
                                                modifier = Modifier.weight(1f),
                                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                            )
                                            Text(
                                                text = "${hour.StartTime} - ${hour.EndTimeTime}",
                                                modifier = Modifier.weight(2f),
                                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                            )
                                    }
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

                        Box( modifier = Modifier.clickable { isShoreExpanded = true }){
                            CustomDobField(
                                textValue = shore.value?.second!!,
                                placeholderText = stringResource(R.string.shores),
                                onTextChange = { },
                                keyboardType = KeyboardType.Email,
                                maxChars = 100,
                                errorMessage = null,
                                isError = false,
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
                                expanded = isShoreExpanded,
                                onDismissRequest = { isShoreExpanded = false },
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
                                            isShoreExpanded = false
                                           shore.value = Pair(category.Id, category.Name)
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
                                textValue = zone.value?.second!!,
                                placeholderText = stringResource(R.string.zones),
                                onTextChange = {},
                                keyboardType = KeyboardType.Email,
                                maxChars = 100,
                                errorMessage = null,
                                isError = false,
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
                                            zone.value = Pair(category.Id, category.Name)
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

                        Box( modifier = Modifier.clickable { isIslandExpanded = true }){
                            CustomDobField(
                                textValue = islnd.value?.second!!,
                                placeholderText = stringResource(R.string.island),
                                onTextChange = {},
                                keyboardType = KeyboardType.Email,
                                maxChars = 100,
                                errorMessage = null,
                                isError = false,
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
                                expanded = isIslandExpanded,
                                onDismissRequest = { isIslandExpanded = false },
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
                                            isIslandExpanded = false
                                            islnd.value = Pair(category.Id, category.Name)
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

                        AddDockSection(navController,businessDetail?.IsDock!!,businessDetail?.Name!!, businessDetail?.Address!!, businessDetail?.Description!!)

                        Spacer(modifier = Modifier.height(40.dp))
                        CustomButton(
                            text = "Save Changes",
                            isValidate = true,
                            isLoading = isLoading,
                            onButtonClick = {
                                viewModelUpdate.saveBusinessProfile( profile =
                                    BusinessRequest(
                                        AppConstants.Busines_Location!!,
                                        BusinessHours = businessDetail?.BusinessHours!!,
                                        IsDock = AppConstants.Busines_DOCK!!,
                                        Name = businessDetail?.Name!!,
                                        ZoneId = zone.value?.first!!,
                                        ShoreId = shore.value?.first!!,
                                        IslandId = islnd.value?.first!!,
                                        State = AppConstants.Busines_State!!,
                                        City = AppConstants.Busines_City!!,
                                        ZipCode = AppConstants.Busines_Zip!!,
                                        ShoreLine = shore.value?.second!!,
                                        Address = businessDetail?.Address!!,
                                        Latitude = AppConstants.Busines_Lat!!,
                                        Longitude = AppConstants.Busines_Lont!!,
                                    ),
                                )
                                isLoading = true
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                    }

                    if (isEditing) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                isEditing = false
                            },
                            sheetState = sheetState,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            containerColor = White,
                            tonalElevation = 16.dp,
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectVerticalDragGestures { _, dragAmount ->
                                        if (dragAmount > 20) {
                                            coroutineScope.launch {
                                                sheetState.partialExpand()
                                            }
                                        }
                                    }
                                }
                        ) {
                            var expandedRowIndex by remember { mutableStateOf<Int?>(null) }
                            var expandedEndIndex by remember { mutableStateOf<Int?>(null) }

                            Column {
                                editableList.forEachIndexed { index, hour ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        TextField(
                                            value = hour.Day.orEmpty(),
                                            onValueChange = {},
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(White),
                                            textStyle = MaterialTheme.typography.bodySmall,
                                            label = { Text("Day") }
                                        )

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(White)
                                        ) {
                                            TextField(
                                                value = hour.StartTime.orEmpty(),
                                                onValueChange = {},
                                                enabled = false,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { expandedRowIndex = index },
                                                textStyle = MaterialTheme.typography.bodySmall,
                                                label = { Text("Start") }
                                            )

                                            DropdownMenu(
                                                expanded = expandedRowIndex == index,
                                                onDismissRequest = { expandedRowIndex = null },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(300.dp)
                                                    .background(White)
                                                    .padding(horizontal = 8.dp)
                                            ) {
                                                AppConstants.hourList.forEach { time ->
                                                    DropdownMenuItem(
                                                        onClick = {
                                                            editableList[index] = hour.copy(StartTime = time)
                                                            expandedRowIndex = null
                                                        },
                                                        text = {
                                                            Text(
                                                                text = time,
                                                                style = MaterialTheme.typography.bodyMedium,
                                                                modifier = Modifier.padding(vertical = 4.dp)
                                                            )
                                                        }
                                                    )
                                                }
                                            }
                                        }

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(White)
                                        ) {
                                            TextField(
                                                value = hour.EndTimeTime.orEmpty(),
                                                onValueChange = {},
                                                enabled = false,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { expandedEndIndex = index },
                                                textStyle = MaterialTheme.typography.bodySmall,
                                                label = { Text("End") }
                                            )

                                            DropdownMenu(
                                                expanded = expandedEndIndex == index,
                                                onDismissRequest = { expandedEndIndex = null },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(300.dp)
                                                    .background(White)
                                            ) {
                                                AppConstants.hourList.forEach { time ->
                                                    DropdownMenuItem(
                                                        onClick = {
                                                            editableList[index] = hour.copy(EndTimeTime = time)
                                                            expandedEndIndex = null
                                                        },
                                                        text = {
                                                            Text(
                                                                text = time,
                                                                style = MaterialTheme.typography.bodyMedium
                                                            )
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }

                    if(logoutEvent){

                        SessionDialog(
                            text = "Session expired, please login Again",
                            onCancel = {},
                            onPressOk = {
                                navController.navigateWithClearStack(NavigationManager.LOGIN_SCREEN, clearStack = true)
                            },
                            showCancelButton = false
                        )
//                        AlertDialog(
//                            onDismissRequest = { },
//                            title = { Text("Session Expired") },
//                            text = { Text("Login Again") },
//                            confirmButton = {
//                                Button(onClick = {
//                                    navController.navigateWithClearStack(NavigationManager.LOGIN_SCREEN, clearStack = true)
//                                }) {
//                                    Text("OK")
//                                }
//                            }
//                        )
                    }
                }
            }
        },
    )
}

@Composable
fun EditableLocationSection(
    navController: NavController,
    location: String?
) {
    var isEditing by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf(location ?: "") }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedAddress = navBackStackEntry
        ?.savedStateHandle
        ?.get<String>("selected_address")

    LaunchedEffect(selectedAddress) {
        if (!selectedAddress.isNullOrBlank()) {
            address = selectedAddress
            isEditing = false
            navBackStackEntry?.savedStateHandle?.remove<String>("selected_address")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Location",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            IconButton(onClick = {
                isEditing = true
                navController.navigate("map_picker")
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = colorResource(R.color.button_normal)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (isEditing) {
            OutlinedTextField(
                value = address,
                onValueChange = {}, // Read-only
                readOnly = true,
                placeholder = {
                    Text(
                        "Address line 1\nAddress line 2\nAddress line 3",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.button_normal),
                    unfocusedBorderColor = colorResource(R.color.black),
                    unfocusedTextColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = 6,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .background(White)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                    .background(White)
                    .padding(12.dp)
            ) {
                Text(
                    text = if (address.isBlank()) "Address line 1\nAddress line 2\nAddress line 3" else address,
                    color = if (address.isBlank()) Color.Gray else Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun AddDockSection(  navController: NavController,isDock : Boolean ,businessname: String, businessaddress: String, businessdescription: String) {
    var isDockEnabled by remember { mutableStateOf(isDock) }
    var name by remember { mutableStateOf(businessname) }
    var address by remember { mutableStateOf(businessaddress) }
    var description by remember { mutableStateOf(businessdescription) }

    Column(modifier = Modifier.padding(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Dock", fontWeight = FontWeight.Bold, fontSize = 16.sp)


            Switch(
                checked = isDockEnabled,
                onCheckedChange = {
                    isDockEnabled = it
                    AppConstants.Busines_DOCK = isDockEnabled
                },

                colors = SwitchDefaults.colors(
                    checkedThumbColor = White,
                    checkedTrackColor = colorResource(id = R.color.button_normal),
                    uncheckedThumbColor = White,
                    uncheckedTrackColor = Color(0xFFD9D9D9),
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isDockEnabled!!) {
            Text("Name", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("John") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray
                )
            )

            // Observe value from map_picker result
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val selectedAddress = navBackStackEntry
                ?.savedStateHandle
                ?.get<String>("selected_address")

            LaunchedEffect(selectedAddress) {
                if (!selectedAddress.isNullOrBlank()) {
                    address = selectedAddress
                    navBackStackEntry?.savedStateHandle?.remove<String>("selected_address")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Address", fontWeight = FontWeight.Medium)

                IconButton(onClick = {
                    navController.navigate("map_picker")
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = colorResource(R.color.button_normal)
                    )
                }
            }

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                placeholder = { Text("Street no 8......") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray
                )
            )

            Text("Description", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Details ............") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray
                )
            )
        }
    }
}

@Composable
fun ImageGridWithAddOption(
    imageList: SnapshotStateList<String>,
    onAddImageClick: () -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    val columns = 3
    val itemSize = 90.dp
    val spacing = 8.dp

    // +1 ensures room for "Add" button always
    val rows = ((imageList.size + 1 + columns - 1) / columns)
    val totalHeight = (itemSize * rows) + (spacing * (rows - 1)) + 16.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight),
        userScrollEnabled = false,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        // Show existing images
        items(imageList.size) { index ->
            Box(
                modifier = Modifier.size(itemSize)
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = BorderStroke(1.dp, color = colorResource(R.color.black)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    val isLocalImage = imageList[index].startsWith("content://") || imageList[index].startsWith("file://")
                    val imageModel = if (isLocalImage) imageList[index] else AppConstants.IMG_PATH + imageList[index]
                    AsyncImage(
                        model = imageModel,
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.business_placeholder),
                        error = painterResource(id = R.drawable.business_placeholder),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(15.dp))
                    )



                }

                // Delete icon
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove Image",
                    tint = White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(20.dp)
                        .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                        .clickable { onRemoveImage(index) }
                        .padding(2.dp)
                )
            }
        }

        // Show add image box if less than 6 images
        if (imageList.size < 6) {
            item {
                Box(
                    modifier = Modifier
                        .size(itemSize)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                        .clickable { onAddImageClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_icon),
                        contentDescription = "Add",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}




