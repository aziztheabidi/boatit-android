package com.boatit.boatsharing.features.business.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.AsyncImage
import com.boatit.boatsharing.R
import com.boatit.boatsharing.mocks.MockBusinessDashboardViewModel
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.navigation.navigateToMapPicker
import com.boatit.boatsharing.ui.navigation.navigateWithClearStack
import com.boatit.boatsharing.features.business.model.BusinessDashboardState
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiEffect
import com.boatit.boatsharing.features.business.model.BusinessDashboardUiEvent
import com.boatit.boatsharing.features.business.model.BusinessHour
import com.boatit.boatsharing.features.business.model.DockData
import com.boatit.boatsharing.features.business.model.LocationData
import com.boatit.boatsharing.features.business.viewmodel.BusinessDashboardViewModel
import com.boatit.boatsharing.features.business.viewmodel.IBusinessDashboardViewModel
import com.boatit.boatsharing.ui.components.SessionDialog
import com.boatit.boatsharing.data.local.session.SessionController
import com.boatit.boatsharing.data.local.session.SessionEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get
import java.io.File

object DesignSystem {
    object Spacing {
        val none: Dp = 0.dp
        val minimalSpacing: Dp = 4.dp
        val smallSpacing: Dp = 8.dp
        val sectionSpacing: Dp = 12.dp
        val cardPadding: Dp = 16.dp
        val elementSpacing: Dp = 20.dp
        val largeSpacing: Dp = 24.dp
    }

    object Sizing {
        val iconSmall: Dp = 16.dp
        val iconMedium: Dp = 24.dp
        val iconLarge: Dp = 32.dp
        val iconXLarge: Dp = 48.dp
        val logoSize: Dp = 110.dp
        val logoSmall: Dp = 80.dp
        val buttonHeight: Dp = 35.dp
        val dropdownHeight: Dp = 300.dp
        val galleryImageSize: Dp = 120.dp
    }

    object Typography {
        val businessName: TextUnit = 22.sp
        val businessType: TextUnit = 16.sp
        val businessDescription: TextUnit = 14.sp
    }

    object CornerRadius {
        val small: Dp = 8.dp
        val medium: Dp = 10.dp
        val large: Dp = 15.dp
        val modal: Dp = 16.dp
    }

    object Elevation {
        val none: Dp = 0.dp
        val high: Dp = 6.dp
        val modal: Dp = 16.dp
    }

    object Border {
        val width: Dp = 1.dp
    }

    object Alpha {
        const val overlay: Float = 0.1f
    }

    object Interaction {
        const val dragThreshold: Float = 20f
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessDashboard(
    navController: NavController,
    useMockData: Boolean = false,
    sessionController: SessionController = get(SessionController::class.java),
) {
    val viewModel: IBusinessDashboardViewModel =
        if (useMockData) {
            remember { MockBusinessDashboardViewModel() }
        } else {
            koinViewModel<BusinessDashboardViewModel>()
        }

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showSessionExpiredDialog by remember { mutableStateOf(false) }

    var zoneDropdownExpanded by remember { mutableStateOf(false) }
    var shoreDropdownExpanded by remember { mutableStateOf(false) }
    var islandDropdownExpanded by remember { mutableStateOf(false) }

    var showImagePicker by remember { mutableStateOf(false) }
    var showLogoPicker by remember { mutableStateOf(false) }
    var showHoursEditor by remember { mutableStateOf(false) }

    val hoursSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedAddress = navBackStackEntry?.savedStateHandle?.get<String>("selected_address")

    LaunchedEffect(Unit) {
        viewModel.onEvent(BusinessDashboardUiEvent.Initialize)
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEffects.collectLatest { effect ->
            when (effect) {
                is BusinessDashboardUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                BusinessDashboardUiEffect.SessionExpired -> {
                    showSessionExpiredDialog = true
                }
            }
        }
    }

    LaunchedEffect(selectedAddress) {
        if (!selectedAddress.isNullOrBlank()) {
            val currentLocation = state.locationData
            viewModel.onEvent(
                BusinessDashboardUiEvent.UpdateLocationData(
                    currentLocation.copy(location = selectedAddress),
                ),
            )
            navBackStackEntry?.savedStateHandle?.remove<String>("selected_address")
        }
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents(),
        ) { uris ->
            if (uris.isNotEmpty()) {
                val remainingSlots = (6 - state.imageList.size).coerceAtLeast(0)
                val selectedUris = uris.take(remainingSlots)
                val updatedImageList = state.imageList + selectedUris.map(Uri::toString)
                viewModel.onEvent(BusinessDashboardUiEvent.UpdateImageList(updatedImageList))

                val files = selectedUris.mapNotNull { uriToFile(context, it) }
                if (files.isNotEmpty()) {
                    viewModel.onEvent(BusinessDashboardUiEvent.UploadGalleryImages(files))
                }
            }
        }

    val logoLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri: Uri? ->
            uri?.let {
                val file = uriToFile(context, uri)
                if (file != null) {
                    viewModel.onEvent(BusinessDashboardUiEvent.UploadLogo(uri.toString(), file))
                }
            }
        }

    if (showImagePicker) {
        AlertDialog(
            onDismissRequest = { showImagePicker = false },
            title = { Text("Add Business Image") },
            text = { Text("Choose image source") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showImagePicker = false
                        galleryLauncher.launch("image/*")
                    },
                ) {
                    Text("Gallery")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImagePicker = false }) {
                    Text("Cancel")
                }
            },
        )
    }

    if (showLogoPicker) {
        AlertDialog(
            onDismissRequest = { showLogoPicker = false },
            title = { Text("Upload Business Logo") },
            text = { Text("Pick a logo image from your gallery") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoPicker = false
                        logoLauncher.launch("image/*")
                    },
                ) {
                    Text("Gallery")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoPicker = false }) {
                    Text("Cancel")
                }
            },
        )
    }

    if (showSessionExpiredDialog) {
        SessionDialog(
            text = "Session expired, please login again",
            onCancel = {},
            onPressOk = {
                showSessionExpiredDialog = false
                val loginRoute = sessionController.resolveRedirectRoute(SessionEvent.SessionExpired)
                if (loginRoute != null) {
                    navController.navigateWithClearStack(loginRoute, clearStack = true)
                }
            },
            showCancelButton = false,
        )
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = DesignSystem.Spacing.cardPadding, vertical = DesignSystem.Spacing.none),
        verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.cardPadding),
    ) {
        Box(
            modifier =
                Modifier
                    .width(DesignSystem.Sizing.logoSmall)
                    .height(DesignSystem.Sizing.logoSmall + DesignSystem.Spacing.elementSpacing)
                    .padding(start = DesignSystem.Spacing.minimalSpacing, top = DesignSystem.Spacing.elementSpacing),
            contentAlignment = Alignment.TopStart,
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.wheel_icon),
                contentDescription = "Business Menu",
                modifier =
                    Modifier
                        .size(width = DesignSystem.Sizing.logoSmall, height = DesignSystem.Sizing.logoSmall)
                        .clickable {
                            navController.navigate(NavigationManager.BUSINESS_MENU_OPTIONS_SCREEN)
                        },
            )
        }

        BusinessProfileSection(state, onShowLogoPicker = { showLogoPicker = true })

        BusinessGallerySection(
            state = state,
            onShowImagePicker = { showImagePicker = true },
            onRemoveImage = { imageUrl ->
                viewModel.onEvent(BusinessDashboardUiEvent.RemoveImage(imageUrl))
            },
        )

        BusinessLocationSection(
            navController = navController,
            state = state,
            onLocationChanged = { locationData ->
                viewModel.onEvent(BusinessDashboardUiEvent.UpdateLocationData(locationData))
            },
        )

        BusinessHoursSection(state = state, onShowTimePicker = { showHoursEditor = true })

        if (showHoursEditor) {
            AdvancedBusinessHoursModal(
                sheetState = hoursSheetState,
                onDismiss = { showHoursEditor = false },
                onSave = { hours ->
                    viewModel.onEvent(BusinessDashboardUiEvent.SaveBusinessHours(hours))
                    showHoursEditor = false
                },
                initialHours = state.businessHours,
            )
        }

        BusinessLocationDropdownSection(
            state = state,
            zoneDropdownExpanded = zoneDropdownExpanded,
            shoreDropdownExpanded = shoreDropdownExpanded,
            islandDropdownExpanded = islandDropdownExpanded,
            onZoneExpand = { zoneDropdownExpanded = it },
            onShoreExpand = { shoreDropdownExpanded = it },
            onIslandExpand = { islandDropdownExpanded = it },
            onZoneSelected = { id, name -> viewModel.onEvent(BusinessDashboardUiEvent.UpdateSelectedZone(id, name)) },
            onShoreSelected = { id, name -> viewModel.onEvent(BusinessDashboardUiEvent.UpdateSelectedShore(id, name)) },
            onIslandSelected = { id, name -> viewModel.onEvent(BusinessDashboardUiEvent.UpdateSelectedIsland(id, name)) },
        )

        BusinessDockSection(
            state = state,
            navController = navController,
            onDockEnabledChanged = { viewModel.onEvent(BusinessDashboardUiEvent.UpdateDockEnabled(it)) },
            onDockDataChanged = { viewModel.onEvent(BusinessDashboardUiEvent.UpdateDockData(it)) },
        )

        Button(
            onClick = { viewModel.onEvent(BusinessDashboardUiEvent.SaveBusinessProfile) },
            enabled = state.isButtonEnabled,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.button_normal)),
        ) {
            Text("Save Changes")
        }
    }
}

private fun uriToFile(
    context: android.content.Context,
    uri: Uri,
): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        tempFile
    } catch (_: Exception) {
        null
    }
}

@Composable
private fun BusinessProfileSection(
    state: BusinessDashboardState,
    onShowLogoPicker: () -> Unit,
) {
    val imageBasePath = stringResource(R.string.path)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Card(
                    shape = RoundedCornerShape(DesignSystem.CornerRadius.large),
                    elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.high),
                    border = BorderStroke(DesignSystem.Border.width, color = colorResource(R.color.black)),
                    modifier =
                        Modifier
                            .width(DesignSystem.Sizing.logoSize)
                            .height(DesignSystem.Sizing.logoSize)
                            .clickable { onShowLogoPicker() },
                ) {
                    if (state.businessData?.LogoPath.isNullOrBlank()) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray.copy(alpha = DesignSystem.Alpha.overlay)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "No logo - Click to upload",
                                    modifier = Modifier.size(DesignSystem.Sizing.iconLarge),
                                    tint = Color.Gray,
                                )
                                Spacer(modifier = Modifier.height(DesignSystem.Spacing.minimalSpacing))
                                Text(
                                    text = "Click to upload",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    } else {
                        AsyncImage(
                            model = imageBasePath + state.businessData?.LogoPath,
                            contentDescription = "Business Logo - Click to change",
                            modifier =
                                Modifier
                                    .height(DesignSystem.Sizing.logoSize)
                                    .width(DesignSystem.Sizing.logoSize)
                                    .clip(RoundedCornerShape(DesignSystem.CornerRadius.large)),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(DesignSystem.Spacing.elementSpacing))

                Text(
                    text = state.businessData?.Name.orEmpty(),
                    fontSize = DesignSystem.Typography.businessName,
                    color = colorResource(R.color.button_normal),
                )

                Text(
                    text = state.businessData?.BusinessType.orEmpty(),
                    fontSize = DesignSystem.Typography.businessType,
                    color = Color.Gray,
                )

                Spacer(modifier = Modifier.height(DesignSystem.Spacing.smallSpacing))

                Text(
                    text = "Established In: ${state.businessData?.YearOfEstablishment ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(DesignSystem.Spacing.smallSpacing))

                Text(
                    text = state.businessData?.Description.orEmpty(),
                    fontSize = DesignSystem.Typography.businessDescription,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun BusinessGallerySection(
    state: BusinessDashboardState,
    onShowImagePicker: () -> Unit,
    onRemoveImage: (String) -> Unit,
) {
    val imageBasePath = stringResource(R.string.path)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing),
        ) {
            Text(
                text = "Business Gallery",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.smallSpacing)) {
                items(state.imageList) { imageUrl ->
                    Box {
                        val imageModel =
                            if (
                                imageUrl.startsWith("content://") || imageUrl.startsWith("file://") || imageUrl.startsWith("http")
                            ) {
                                imageUrl
                            } else {
                                imageBasePath + imageUrl
                            }

                        AsyncImage(
                            model = imageModel,
                            contentDescription = "Business Image",
                            modifier =
                                Modifier
                                    .size(DesignSystem.Sizing.galleryImageSize)
                                    .clip(RoundedCornerShape(DesignSystem.CornerRadius.medium)),
                            contentScale = ContentScale.Crop,
                        )

                        IconButton(
                            onClick = { onRemoveImage(imageUrl) },
                            modifier = Modifier.align(Alignment.TopEnd),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Image",
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = onShowImagePicker,
                modifier = Modifier.size(DesignSystem.Sizing.iconXLarge),
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Image")
            }
        }
    }
}

@Composable
private fun BusinessLocationSection(
    navController: NavController,
    state: BusinessDashboardState,
    onLocationChanged: (LocationData) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Business Location",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(
                    onClick = {
                        navController.navigateToMapPicker()
                    },
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Location")
                }
            }

            OutlinedTextField(
                value = state.locationData.location,
                onValueChange = { onLocationChanged(state.locationData.copy(location = it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Address") },
                maxLines = 4,
            )
        }
    }
}

@Composable
private fun BusinessLocationDropdownSection(
    state: BusinessDashboardState,
    zoneDropdownExpanded: Boolean,
    shoreDropdownExpanded: Boolean,
    islandDropdownExpanded: Boolean,
    onZoneExpand: (Boolean) -> Unit,
    onShoreExpand: (Boolean) -> Unit,
    onIslandExpand: (Boolean) -> Unit,
    onZoneSelected: (Int, String) -> Unit,
    onShoreSelected: (Int, String) -> Unit,
    onIslandSelected: (Int, String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing),
        ) {
            DropdownField(
                label = "Shore",
                selectedValue = state.selectedShore,
                expanded = shoreDropdownExpanded,
                onExpand = onShoreExpand,
                items = state.shores.map { it.Id to it.Name },
                onSelected = onShoreSelected,
            )

            DropdownField(
                label = "Zone",
                selectedValue = state.selectedZone,
                expanded = zoneDropdownExpanded,
                onExpand = onZoneExpand,
                items = state.zones.map { it.Id to it.Name },
                onSelected = onZoneSelected,
            )

            DropdownField(
                label = "Island",
                selectedValue = state.selectedIsland,
                expanded = islandDropdownExpanded,
                onExpand = onIslandExpand,
                items = state.islands.map { it.Id to it.Name },
                onSelected = onIslandSelected,
            )
        }
    }
}

@Composable
private fun DropdownField(
    label: String,
    selectedValue: String,
    expanded: Boolean,
    onExpand: (Boolean) -> Unit,
    items: List<Pair<Int, String>>,
    onSelected: (Int, String) -> Unit,
) {
    Box {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onExpand(true) },
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                IconButton(onClick = { onExpand(!expanded) }) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpand(false) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.second) },
                    onClick = {
                        onSelected(item.first, item.second)
                        onExpand(false)
                    },
                )
            }
        }
    }
}

@Composable
private fun BusinessDockSection(
    state: BusinessDashboardState,
    navController: NavController,
    onDockEnabledChanged: (Boolean) -> Unit,
    onDockDataChanged: (DockData) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Add Dock", style = MaterialTheme.typography.titleMedium)
                Switch(
                    checked = state.dockEnabled,
                    onCheckedChange = onDockEnabledChanged,
                    colors =
                        SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = colorResource(R.color.button_normal),
                        ),
                )
            }

            if (state.dockEnabled) {
                OutlinedTextField(
                    value = state.dockData.name,
                    onValueChange = { onDockDataChanged(state.dockData.copy(name = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Dock Name") },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Dock Address", style = MaterialTheme.typography.bodyLarge)
                    IconButton(onClick = { navController.navigateToMapPicker() }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Dock Address")
                    }
                }

                OutlinedTextField(
                    value = state.dockData.address,
                    onValueChange = { onDockDataChanged(state.dockData.copy(address = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Address") },
                )

                OutlinedTextField(
                    value = state.dockData.description,
                    onValueChange = { onDockDataChanged(state.dockData.copy(description = it)) },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                    label = { Text("Description") },
                )
            }
        }
    }
}

@Composable
private fun BusinessHoursSection(
    state: BusinessDashboardState,
    onShowTimePicker: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing),
        ) {
            Text(
                text = "Business Hours",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            if (state.businessHours.isNotEmpty()) {
                state.businessHours.forEach { hour ->
                    Text(
                        text = "${hour.Day}: ${hour.StartTime} - ${hour.EndTimeTime}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            } else {
                Text(
                    text = "No business hours set",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Button(
                onClick = onShowTimePicker,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.button_normal)),
            ) {
                Text("Edit Hours")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdvancedBusinessHoursModal(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSave: (List<BusinessHour>) -> Unit,
    initialHours: List<BusinessHour>,
) {
    var expandedRowIndex by remember { mutableStateOf<Int?>(null) }
    var expandedEndIndex by remember { mutableStateOf<Int?>(null) }
    val hourList = remember { List(24) { hour -> String.format("%02d:00:00", hour) } }

    val fallbackHours =
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            .map { day -> BusinessHour(Day = day, StartTime = "09:00:00", EndTimeTime = "17:00:00") }

    var editableHours by remember {
        mutableStateOf(if (initialHours.isEmpty()) fallbackHours else initialHours)
    }

    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape =
            RoundedCornerShape(
                topStart = DesignSystem.CornerRadius.modal,
                topEnd = DesignSystem.CornerRadius.modal,
            ),
        containerColor = Color.White,
        tonalElevation = DesignSystem.Elevation.modal,
        modifier =
            Modifier
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > DesignSystem.Interaction.dragThreshold) {
                            coroutineScope.launch {
                                sheetState.partialExpand()
                            }
                        }
                    }
                },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.cardPadding),
        ) {
            Text(
                text = "Edit Business Hours",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)) {
                items(editableHours.size) { index ->
                    val hour = editableHours[index]

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.smallSpacing),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            value = hour.Day,
                            onValueChange = {},
                            modifier = Modifier.weight(0.7f),
                            label = { Text("Day") },
                            readOnly = true,
                        )

                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = hour.StartTime,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Open") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = {
                                        expandedRowIndex = if (expandedRowIndex == index) null else index
                                    }) {
                                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                                    }
                                },
                            )

                            DropdownMenu(
                                expanded = expandedRowIndex == index,
                                onDismissRequest = { expandedRowIndex = null },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(DesignSystem.Sizing.dropdownHeight)
                                        .background(Color.White),
                            ) {
                                hourList.forEach { time ->
                                    DropdownMenuItem(
                                        onClick = {
                                            editableHours =
                                                editableHours.toMutableList().apply {
                                                    set(index, hour.copy(StartTime = time))
                                                }
                                            expandedRowIndex = null
                                        },
                                        text = {
                                            Text(
                                                text = time,
                                                modifier = Modifier.padding(vertical = DesignSystem.Spacing.minimalSpacing),
                                            )
                                        },
                                    )
                                }
                            }
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = hour.EndTimeTime,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Close") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = {
                                        expandedEndIndex = if (expandedEndIndex == index) null else index
                                    }) {
                                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                                    }
                                },
                            )

                            DropdownMenu(
                                expanded = expandedEndIndex == index,
                                onDismissRequest = { expandedEndIndex = null },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(DesignSystem.Sizing.dropdownHeight)
                                        .background(Color.White),
                            ) {
                                hourList.forEach { time ->
                                    DropdownMenuItem(
                                        onClick = {
                                            editableHours =
                                                editableHours.toMutableList().apply {
                                                    set(index, hour.copy(EndTimeTime = time))
                                                }
                                            expandedEndIndex = null
                                        },
                                        text = {
                                            Text(
                                                text = time,
                                                modifier = Modifier.padding(vertical = DesignSystem.Spacing.minimalSpacing),
                                            )
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.smallSpacing),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = { onSave(editableHours) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.button_normal)),
                ) {
                    Text("Save Hours")
                }
            }
        }
    }
}
