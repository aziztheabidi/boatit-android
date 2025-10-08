package com.boatit.boatsharing.ui.business.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.ui.business.model.BusinessHour
import com.boatit.boatsharing.ui.business.viewmodel.BusinessDashboardViewModel
import com.boatit.boatsharing.ui.business.viewmodel.IBusinessDashboardViewModel
import com.boatit.boatsharing.ui.design.DesignSystem
import com.boatit.boatsharing.ui.business.viewmodel.BusinessDashViewModel
import com.boatit.boatsharing.ui.business.viewmodel.GetBusinessViewModel
import com.boatit.boatsharing.ui.signup.business.viewmodel.BusinessLogoViewModel
import com.boatit.boatsharing.uihelpers.SessionDialog
import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.permissions.PermissionsToAccessCamera
import com.boatit.boatsharing.utils.permissions.PermissionsToAccessGallery
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * BusinessDashboard - Main composable for business dashboard screen
 * 
 * FULFILLS: All UI LLRs (1.2.1 through 1.7.2) - Complete UI implementation
 * 
 * This composable provides a comprehensive business dashboard with profile management,
 * image gallery, location settings, business hours, dock services, and actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessDashboard(
    navController: NavController,
    useMockData: Boolean = true  // Set to true to use mock data for testing
) {
    val context = LocalContext.current
    val viewModel: IBusinessDashboardViewModel = if (useMockData) {
        remember { com.boatit.boatsharing.mocks.MockBusinessDashboardViewModel() }
    } else {
        koinViewModel<BusinessDashboardViewModel>()
    }
    
    // Old Business Dashboard ViewModels - needed for switching
    val viewModelUpdate = koinViewModel<BusinessDashViewModel>()
    val viewModelGallery = koinViewModel<BusinessLogoViewModel>()
    val oldViewModel = koinViewModel<GetBusinessViewModel>()
    val state by viewModel.dashboardState.collectAsState()
    val sessionEvents by viewModel.getSessionEvents().collectAsState(initial = null)
    
    // Dropdown expansion states
    var zoneDropdownExpanded by remember { mutableStateOf(false) }
    var shoreDropdownExpanded by remember { mutableStateOf(false) }
    var islandDropdownExpanded by remember { mutableStateOf(false) }
    
    // Time picker states - FULFILLS: LLR-2.2.1 - Modal Bottom Sheet State Management
    var showTimePicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val coroutineScope = rememberCoroutineScope()
    
    // Image picker states
    var showImagePicker by remember { mutableStateOf(false) }
    var useGallery by remember { mutableStateOf(false) }
    var useCamera by remember { mutableStateOf(false) }
    
    // Logo upload states - FULFILLS: LLR-2.5.2 - Logo Upload Integration
    var showLogoPicker by remember { mutableStateOf(false) }
    var useLogoGallery by remember { mutableStateOf(false) }
    var useLogoCamera by remember { mutableStateOf(false) }
    
    // Toggle state for switching between dashboards
    var useNewDashboard by remember { mutableStateOf(true) }
    
    // Check authentication on launch and initialize backend data
    // FULFILLS: LLR-2.6.1 and LLR-2.6.2 - Backend Integration Initialization
    // FULFILLS: LLR-2.3.1 - Map Picker Navigation Integration (location data retrieval)
    LaunchedEffect(Unit) {
        if (!viewModel.checkAuthentication()) {
            navController.navigate("login")
        } else {
            viewModel.initializeDashboardData()
        }
    }
    
    // Handle map picker result - FULFILLS: LLR-2.3.1 - Map Picker Location Integration
    LaunchedEffect(Unit) {
        val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
        savedStateHandle?.let { handle ->
            handle.getStateFlow<String?>("selected_address", null).collect { selectedAddress ->
                selectedAddress?.let { address ->
                    // Parse and update location data from map picker
                    val addressLines = address.split("\n").filter { it.isNotBlank() }
                    if (addressLines.size >= 5) {
                        val extractedAddress = addressLines[0].substringAfter("Address: ")
                        val extractedCity = addressLines[1].substringAfter("City: ")
                        val extractedState = addressLines[2].substringAfter("State: ")
                        val extractedLat = addressLines[3].substringAfter("Latitude: ").toDoubleOrNull() ?: 0.0
                        val extractedLng = addressLines[4].substringAfter("Longitude: ").toDoubleOrNull() ?: 0.0
                        
                        // Update location data in ViewModel
                        val locationData = com.boatit.boatsharing.ui.business.model.LocationData(
                            address = extractedAddress,
                            city = extractedCity,
                            state = extractedState,
                            latitude = extractedLat,
                            longitude = extractedLng,
                            zone = state.selectedZone ?: "",
                            shore = state.selectedShore ?: "",
                            island = state.selectedIsland ?: "",
                            isWaterfront = true, // Assuming waterfront for marine businesses
                            hasParking = false,
                            hasAccessibility = false,
                            isActive = true
                        )
                        viewModel.updateLocationData(locationData)
                        
                        // Clear the saved state
                        handle.set("selected_address", null)
                        
                        Toast.makeText(context, "Location updated!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = DesignSystem.Spacing.cardPadding, vertical = 0.dp),
        verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.cardPadding)
    ) {
        // Business Menu Navigation Button - FULFILLS: Missing Business Menu Integration
        Box(
            modifier = Modifier
                .width(DesignSystem.Sizing.logoSmall)
                .height(DesignSystem.Sizing.logoSmall + DesignSystem.Spacing.elementSpacing)
                .padding(start = DesignSystem.Spacing.minimalSpacing, top = DesignSystem.Spacing.elementSpacing),
            contentAlignment = Alignment.TopStart,
        ) {
            Image(
                painter = painterResource(id = R.drawable.wheel_icon),
                contentDescription = "Business Menu",
                modifier = Modifier
                    .size(width = DesignSystem.Sizing.logoSmall, height = DesignSystem.Sizing.logoSmall)
                    .clickable(onClick = {
                        navController.navigate(NavigationManager.BUSINESS_MENU_OPTIONS_SCREEN)
                    })
            )
        }
        // Dashboard Toggle Button
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.low),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dashboard Compare Mode",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = { useNewDashboard = !useNewDashboard },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (useNewDashboard) colorResource(R.color.button_normal) else Color.Green
                    )
                ) {
                    Text(
                        text = if (useNewDashboard) "Switch to Old" else "Switch to New",
                        color = Color.White
                    )
                }
            }
        }
        
        // Conditional Dashboard Content
        if (useNewDashboard) {
            // NEW DASHBOARD CONTENT
            // FULFILLS: LLR-1.2.1, LLR-1.2.2 - Business Profile Section
            BusinessProfileSection(state, viewModel) { showLogoPicker = true }
        
        // FULFILLS: LLR-1.3.1, LLR-1.3.2, LLR-1.3.3 - Business Gallery Section
        // FULFILLS: LLR-2.1.1 - Multiple Image Selection
        BusinessGallerySection(state, viewModel, { showImagePicker = true }, context)
        
        // FULFILLS: LLR-1.4.1, LLR-1.4.2 - Business Location Section
        // FULFILLS: LLR-2.6.1 - Backend Dropdown Data Integration
        BusinessLocationSection(
            state = state,
            viewModel = viewModel,
            navController = navController,
            zoneDropdownExpanded = zoneDropdownExpanded,
            shoreDropdownExpanded = shoreDropdownExpanded,
            islandDropdownExpanded = islandDropdownExpanded,
            onZoneExpandedChange = { zoneDropdownExpanded = !zoneDropdownExpanded },
            onShoreExpandedChange = { shoreDropdownExpanded = !shoreDropdownExpanded },
            onIslandExpandedChange = { islandDropdownExpanded = !islandDropdownExpanded },
            zones = state.zones,
            shores = state.shores,
            islands = state.islands
        )
        
        // FULFILLS: LLR-1.5.1, LLR-1.5.2 - Business Hours Section
        BusinessHoursSection(state, viewModel) { showTimePicker = true }
        
        // FULFILLS: LLR-1.6.1, LLR-1.6.2 - Business Dock Section
        BusinessDockSection(state, viewModel, navController)
        
            // FULFILLS: LLR-1.7.1, LLR-1.7.2 - Business Actions Section
            BusinessActionsSection(state, viewModel)
        } else {
            // OLD DASHBOARD CONTENT - Render actual OldBusinessDashboard
            OldBusinessDashboard(
                navController = navController,
                viewModelUpdate = viewModelUpdate,
                viewModelGallery = viewModelGallery,
                viewModel = oldViewModel
            )
        }
    }
    
    // Advanced Business Hours Editor - FULFILLS: LLR-2.2.1 - Modal Bottom Sheet Implementation
    if (showTimePicker) {
        AdvancedBusinessHoursModal(
            sheetState = sheetState,
            onDismiss = { showTimePicker = false },
            onSave = { updatedHours ->
                viewModel.saveBusinessHours(updatedHours)
                showTimePicker = false
                Toast.makeText(context, "Business hours updated!", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    // Image Picker Dialog
    if (showImagePicker) {
        AlertDialog(
            onDismissRequest = { showImagePicker = false },
            title = { Text("Add Business Image") },
            text = { 
                Text("Choose an image source:")
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showImagePicker = false
                        useGallery = true
                    }
                ) {
                    Text("Gallery")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showImagePicker = false
                        useCamera = true
                    }
                ) {
                    Text("Camera")
                }
            }
        )
    }
    
    // Logo Picker Dialog - FULFILLS: LLR-2.5.2 - Logo Upload Integration
    if (showLogoPicker) {
        AlertDialog(
            onDismissRequest = { showLogoPicker = false },
            title = { Text("Select Logo Source") },
            text = { Text("Choose how to add your business logo:") },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showLogoPicker = false
                        useLogoGallery = true
                    }
                ) {
                    Text("Gallery")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showLogoPicker = false
                        useLogoCamera = true
                    }
                ) {
                    Text("Camera")
                }
            }
        )
    }
    
    // Gallery Picker (triggered by flag) - FULFILLS: LLR-2.1.1 - Multiple Image Selection
    if (useGallery) {
        PermissionsToAccessGalleryMultiple(
            onImagesSelected = { selectedUris: List<Uri> ->
                // FULFILLS: LLR-2.1.3 - Backend Image Upload Integration
                // Add URIs to UI state immediately for visual feedback
                val newImageUris = selectedUris.map { it.toString() }
                val updatedImageList = state.imageList + newImageUris
                viewModel.updateImageList(updatedImageList)
                
                // Upload to backend
                viewModel.uploadImagesToBackend(selectedUris, context)
                
                useGallery = false
                Toast.makeText(context, "Uploading ${selectedUris.size} images...", Toast.LENGTH_SHORT).show()
            },
            onPermissionGranted = { /* Already handled */ },
            onPermissionDenied = { 
                useGallery = false
                Toast.makeText(context, "Gallery permission denied", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    // Camera Picker (triggered by flag)
    if (useCamera) {
        PermissionsToAccessCamera(
            onImageCaptured = { uri: Uri? ->
                uri?.let {
                    val uriString = it.toString()
                    val newImageList = state.imageList + uriString
                    viewModel.updateImageList(newImageList)
                    useCamera = false
                    Toast.makeText(context, "Image captured!", Toast.LENGTH_SHORT).show()
                } ?: run {
                    useCamera = false
                    Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    
    // Logo Gallery Picker - FULFILLS: LLR-2.5.2 - Logo Upload Integration (Gallery)
    if (useLogoGallery) {
        PermissionsToAccessGallery(
            onImageSelected = { uri: Uri ->
                // Update logo path in business data
                val logoUri = uri.toString()
                val updatedData = state.businessData?.copy(logoPath = logoUri)
                    ?: com.boatit.boatsharing.ui.business.model.BusinessProfileInfo(logoPath = logoUri)
                viewModel.updateBusinessData(updatedData)
                useLogoGallery = false
                Toast.makeText(context, "Logo uploaded!", Toast.LENGTH_SHORT).show()
            },
            onPermissionGranted = { /* Already handled by gallery */ },
            onPermissionDenied = { 
                useLogoGallery = false
                Toast.makeText(context, "Gallery permission denied", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    // Logo Camera Picker - FULFILLS: LLR-2.5.2 - Logo Upload Integration (Camera)
    if (useLogoCamera) {
        PermissionsToAccessCamera(
            onImageCaptured = { uri: Uri? ->
                uri?.let {
                    // Update logo path in business data
                    val logoUri = it.toString()
                    val updatedData = state.businessData?.copy(logoPath = logoUri)
                        ?: com.boatit.boatsharing.ui.business.model.BusinessProfileInfo(logoPath = logoUri)
                    viewModel.updateBusinessData(updatedData)
                    useLogoCamera = false
                    Toast.makeText(context, "Logo captured!", Toast.LENGTH_SHORT).show()
                } ?: run {
                    useLogoCamera = false
                    Toast.makeText(context, "Failed to capture logo", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    
    // Session Expiration Dialog - FULFILLS: Missing SessionDialog Integration
    sessionEvents?.let { event ->
        when (event) {
            is com.boatit.boatsharing.utils.session.SessionEvent.SessionExpired,
            is com.boatit.boatsharing.utils.session.SessionEvent.TokenRefreshFailed,
            is com.boatit.boatsharing.utils.session.SessionEvent.AccountDeactivated -> {
                SessionDialog(
                    text = "Session expired, please login again",
                    onCancel = { /* No cancellation for expired session */ },
                    onPressOk = {
                        navController.navigateWithClearStack(NavigationManager.LOGIN_SCREEN, clearStack = true)
                    },
                    showCancelButton = false
                )
            }
            else -> { /* Other events handled elsewhere */ }
        }
    }
}

/**
 * Business Profile Section - OLD DASHBOARD STYLE
 * 
 * FULFILLS: LLR-1.2.1 - Business Profile Display
 * FULFILLS: LLR-1.2.2 - Business Profile Editing
 * 
 * This section now matches the old dashboard's layout and styling
 */
@Composable
private fun BusinessProfileSection(
    state: com.boatit.boatsharing.ui.business.model.BusinessDashboardState,
    viewModel: IBusinessDashboardViewModel,
    onShowLogoPicker: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)
        ) {
            // OLD DASHBOARD STYLE - Centered layout with logo at top
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Business Logo - OLD DASHBOARD STYLE (Clickable)
                Card(
                    shape = RoundedCornerShape(DesignSystem.CornerRadius.large),
                    elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.high),
                    border = BorderStroke(DesignSystem.Border.width, color = colorResource(R.color.black)),
                    modifier = Modifier
                        .width(DesignSystem.Sizing.logoSize)
                        .height(DesignSystem.Sizing.logoSize)
                        .clickable { onShowLogoPicker() }
                ) {
                    if (state.businessData?.logoPath.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "No logo - Click to upload",
                                        modifier = Modifier.size(DesignSystem.Sizing.iconLarge),
                                        tint = androidx.compose.ui.graphics.Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(DesignSystem.Spacing.minimalSpacing))
                                Text(
                                    text = "Click to upload",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = androidx.compose.ui.graphics.Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                            AsyncImage(
                                model = state.businessData?.logoPath,
                                contentDescription = "Business Logo - Click to change",
                                modifier = Modifier
                                    .height(DesignSystem.Sizing.logoSize)
                                    .width(DesignSystem.Sizing.logoSize)
                                    .clip(RoundedCornerShape(DesignSystem.CornerRadius.large)),
                                contentScale = ContentScale.Crop
                            )
                    }
                }

                Spacer(Modifier.height(DesignSystem.Spacing.elementSpacing))

                // Business Name - OLD DASHBOARD STYLE
                Text(
                    text = state.businessData?.businessName ?: "Loading...",
                    color = colorResource(id = R.color.button_normal),
                    fontSize = DesignSystem.Typography.businessName,
                    fontWeight = FontWeight.Normal
                )

                Spacer(Modifier.height(DesignSystem.Spacing.smallSpacing))

                // Business Type - OLD DASHBOARD STYLE
                Text(
                    text = state.businessData?.businessType ?: "Loading...",
                    color = androidx.compose.ui.graphics.Color.Gray,
                    fontSize = DesignSystem.Typography.businessType,
                    fontWeight = FontWeight.Normal
                )

                Spacer(Modifier.height(DesignSystem.Spacing.elementSpacing))

                // Year Established Button - OLD DASHBOARD STYLE
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(DesignSystem.CornerRadius.medium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DesignSystem.Sizing.buttonHeight)
                        .border(
                            width = DesignSystem.Border.width,
                            color = androidx.compose.ui.graphics.Color.Gray,
                            shape = RoundedCornerShape(DesignSystem.CornerRadius.xlarge)
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "Established In : " + (state.businessData?.yearEstablished?.toString() ?: ""),
                        fontSize = DesignSystem.Typography.buttonText,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black)
                    )
                }

                Spacer(Modifier.height(DesignSystem.Spacing.elementSpacing))

                // Business Description - OLD DASHBOARD STYLE
                Text(
                    text = state.businessData?.businessDescription ?: "Loading...",
                    color = androidx.compose.ui.graphics.Color.Gray,
                    fontSize = DesignSystem.Typography.businessDescription,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

/**
 * Business Gallery Section
 * 
 * FULFILLS: LLR-1.3.1 - Image Gallery Display
 * FULFILLS: LLR-1.3.2 - Image Upload Button
 * FULFILLS: LLR-1.3.3 - Image Removal
 */
@Composable
private fun BusinessGallerySection(
    state: com.boatit.boatsharing.ui.business.model.BusinessDashboardState,
    viewModel: IBusinessDashboardViewModel,
    onShowImagePicker: () -> Unit,
    context: android.content.Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)
        ) {
            Text(
                text = "Business Gallery",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            // Image Gallery
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.smallSpacing)
            ) {
                items(state.imageList) { imageUrl ->
                    Box {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Business Image",
                            modifier = Modifier
                                .size(DesignSystem.Sizing.galleryImageSize)
                                .padding(DesignSystem.Spacing.minimalSpacing),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = {
                                // FULFILS: LLR-2.1.1 - Image Deletion Implementation
                                val updatedList = state.imageList.toMutableList().apply { remove(imageUrl) }
                                viewModel.updateImageList(updatedList)
                                Toast.makeText(context, "Image removed", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Image",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
            
            // Upload Button
            FloatingActionButton(
                onClick = onShowImagePicker,
                modifier = Modifier.size(DesignSystem.Sizing.iconXLarge)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Image"
                )
            }
        }
    }
}

/**
 * Business Location Section
 * 
 * FULFILLS: LLR-1.4.1 - Location Dropdowns
 * FULFILLS: LLR-1.4.2 - Address Display and Edit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BusinessLocationSection(
    state: com.boatit.boatsharing.ui.business.model.BusinessDashboardState,
    viewModel: IBusinessDashboardViewModel,
    navController: NavController,
    zoneDropdownExpanded: Boolean,
    shoreDropdownExpanded: Boolean,
    islandDropdownExpanded: Boolean,
    onZoneExpandedChange: (Boolean) -> Unit,
    onShoreExpandedChange: (Boolean) -> Unit,
    onIslandExpandedChange: (Boolean) -> Unit,
    zones: List<com.boatit.boatsharing.ui.business.model.DockDropdownItem>,
    shores: List<com.boatit.boatsharing.ui.business.model.DockDropdownItem>,
    islands: List<com.boatit.boatsharing.ui.business.model.DockDropdownItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)
        ) {
            Text(
                text = "Business Location",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            // Zone Selection
            ExposedDropdownMenuBox(
                expanded = zoneDropdownExpanded,
                onExpandedChange = onZoneExpandedChange
            ) {
                OutlinedTextField(
                    value = state.selectedZone ?: "",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Zone") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
                )
                ExposedDropdownMenu(
                    expanded = zoneDropdownExpanded,
                    onDismissRequest = { onZoneExpandedChange(false) }
                ) {
                    // Zone options from backend data
                    // FULFILLS: LLR- the state's dropdown items
                    zones.forEach { zone ->
                        DropdownMenuItem(
                            text = { Text(zone.Name) },
                            onClick = {
                                viewModel.updateSelectedZone(zone.Name)
                                onZoneExpandedChange(false)
                                viewModel.validateForm()
                            }
                        )
                    }
                }
            }
            
            // Shore Selection
            ExposedDropdownMenuBox(
                expanded = shoreDropdownExpanded,
                onExpandedChange = onShoreExpandedChange
            ) {
                OutlinedTextField(
                    value = state.selectedShore ?: "",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Shore") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
                )
                ExposedDropdownMenu(
                    expanded = shoreDropdownExpanded,
                    onDismissRequest = { onShoreExpandedChange(false) }
                ) {
                    // Shore options from backend data
                    // FULFILLS: LLR-2.6.1 - Backend Shore Dropdown Integration
                    shores.forEach { shore ->
                        DropdownMenuItem(
                            text = { Text(shore.Name) },
                            onClick = {
                                viewModel.updateSelectedShore(shore.Name)
                                onShoreExpandedChange(false)
                                viewModel.validateForm()
                            }
                        )
                    }
                }
            }
            
            // Island Selection
            ExposedDropdownMenuBox(
                expanded = islandDropdownExpanded,
                onExpandedChange = onIslandExpandedChange
            ) {
                OutlinedTextField(
                    value = state.selectedIsland ?: "",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Island") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
                )
                ExposedDropdownMenu(
                    expanded = islandDropdownExpanded,
                    onDismissRequest = { onIslandExpandedChange(false) }
                ) {
                    // Island options from backend data
                    // FULFILLS: LLR-2.6.1 - Backend Island Dropdown Integration
                    islands.forEach { island ->
                        DropdownMenuItem(
                            text = { Text(island.Name) },
                            onClick = {
                                viewModel.updateSelectedIsland(island.Name)
                                onIslandExpandedChange(false)
                                viewModel.validateForm()
                            }
                        )
                    }
                }
            }
            
            // Address Display and Edit - FULFILLS: LLR-2.3.1 - Map Picker Integration Display
            Column(
                verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.smallSpacing)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Business Location",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = {
                            navController.navigate("map_picker")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Address",
                            tint = colorResource(R.color.button_normal)
                        )
                    }
                }
                
                // Display location details from map picker
                state.locationData?.let { location ->
                    if (location.address.isNotBlank()) {
                        Text(
                            text = location.address,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (location.city.isNotBlank()) {
                            Text(
                                text = "${location.city}, ${location.state}",
                                style = MaterialTheme.typography.bodySmall,
                                color = androidx.compose.ui.graphics.Color.Gray
                            )
                        }
                        if (location.latitude != 0.0 && location.longitude != 0.0) {
                            Text(
                                text = "Coordinates: ${String.format("%.4f", location.latitude)}, ${String.format("%.4f", location.longitude)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = androidx.compose.ui.graphics.Color.Gray
                            )
                        }
                    } else {
                        Text(
                            text = "No location selected",
                            style = MaterialTheme.typography.bodyMedium,
                            color = androidx.compose.ui.graphics.Color.Gray
                        )
                    }
                } ?: run {
                    Text(
                        text = "No location selected", 
                        style = MaterialTheme.typography.bodyMedium,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }
        }
    }
}

/**
 * Business Hours Section
 * 
 * FULFILLS: LLR-1.5.1 - Hours Display
 * FULFILLS: LLR-1.5.2 - Hours Editing
 */
@Composable
private fun BusinessHoursSection(
    state: com.boatit.boatsharing.ui.business.model.BusinessDashboardState,
    viewModel: IBusinessDashboardViewModel,
    onShowTimePicker: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)
        ) {
            Text(
                text = "Business Hours",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            // Hours Display
            if (state.businessHours.isNotEmpty()) {
                state.businessHours.forEach { hour ->
                    Text(
                        text = "${hour.Day}: ${hour.StartTime} - ${hour.EndTimeTime}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Text(
                    text = "No business hours set",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            // Edit Hours Button
            Button(
                onClick = onShowTimePicker,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.button_normal)
                )
            ) {
                Text("Edit Hours")
            }
        }
    }
}

/**
 * Business Dock Section
 * 
 * FULFILLS: LLR-1.6.1 - Dock Toggle
 * FULFILLS: LLR-1.6.2 - Dock Information Display
 */
@Composable
private fun BusinessDockSection(
    state: com.boatit.boatsharing.ui.business.model.BusinessDashboardState,
    viewModel: IBusinessDashboardViewModel,
    navController: NavController
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)
        ) {
            Text(
                text = "Dock Services",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            // Dock Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Enable Dock Services",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = state.dockEnabled,
                    onCheckedChange = { enabled ->
                        viewModel.updateDockEnabled(enabled)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = colorResource(R.color.button_normal),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFD9D9D9),
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
            
            // Enhanced Dock Information Display - FULFILLS: LLR-2.4.1 - Dock Service Details Enhancement
            if (state.dockEnabled) {
                val dockData = state.dockData
                if (dockData != null) {
                    // Dock Header Information
                    Text(
                        text = "Dock Information:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    if (dockData.dockName.isNotBlank()) {
                        Text(
                            text = "• Name: ${dockData.dockName}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    if (dockData.dockType.isNotBlank()) {
                        Text(
                            text = "• Type: ${dockData.dockType}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    // Boat Specifications
                    if (dockData.maxBoatLength > 0) {
                        Text(
                            text = "• Max boat length: ${dockData.maxBoatLength} ft",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    if (dockData.maxBoatWidth > 0) {
                        Text(
                            text = "• Max boat width: ${dockData.maxBoatWidth} ft",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    if (dockData.maxBoatDraft > 0) {
                        Text(
                            text = "• Max boat draft: ${dockData.maxBoatDraft} ft",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    // Slip Information
                    if (dockData.totalSlips > 0) {
                        Text(
                            text = "• Available slips: ${dockData.availableSlips}/${dockData.totalSlips}",
                    style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    // Pricing Information
                    Text(
                        text = "• Pricing:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    
                    if (dockData.hourlyRate > 0) {
                        Text(
                            text = "  - Hourly: $${dockData.hourlyRate}/hour",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    if (dockData.dailyRate > 0) {
                        Text(
                            text = "  - Daily: $${dockData.dailyRate}/day",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    if (dockData.monthlyRate > 0) {
                        Text(
                            text = "  - Monthly: $${dockData.monthlyRate}/month",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    // Amenities Information
                    Column {
                        Text(
                            text = "• Amenities:",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Row(modifier = Modifier.padding(start = DesignSystem.Spacing.minimalSpacing)) {
                            Text(
                                text = buildString {
                                    if (dockData.hasPower) append("Power ") 
                                    if (dockData.hasWater) append("Water ") 
                                    if (dockData.hasWifi) append("WiFi ") 
                                    if (dockData.hasRestrooms) append("Restrooms ") 
                                    if (dockData.hasShowers) append("Showers ") 
                                    if (dockData.hasFuel) append("Fuel ") 
                                    if (dockData.hasPumpout) append("Pumpout ")
                                }.trim(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        
                        if (dockData.hasPower.not() && dockData.hasWater.not() && dockData.hasWifi.not() && 
                            dockData.hasRestrooms.not() && dockData.hasShowers.not() && dockData.hasFuel.not() && dockData.hasPumpout.not()) {
                            Text(
                                text = "None listed",
                                style = MaterialTheme.typography.bodySmall,
                                color = androidx.compose.ui.graphics.Color.Gray
                            )
                        }
                    }
                } else {
                    // Default dock information when no specific dock data
                    Text(
                        text = "Dock Information:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "• Standard dock services available",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Spacer(modifier = Modifier.height(DesignSystem.Spacing.smallSpacing))
                
                // Dock Details Form - FULFILLS: Missing Complete Dock Form
                var dockName by remember { mutableStateOf(state.dockData?.dockName ?: state.businessData?.businessName ?: "") }
                var dockAddress by remember { mutableStateOf(state.dockData?.dockName ?: state.locationData?.address ?: "") }
                var dockDescription by remember { mutableStateOf(state.businessData?.businessDescription ?: "") }
                
                // Name Field
                Text(
                    text = "Dock Name",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                OutlinedTextField(
                    value = dockName,
                    onValueChange = { dockName = it },
                    placeholder = { Text("Enter dock name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = DesignSystem.Spacing.minimalSpacing),
                    singleLine = true,
                    shape = RoundedCornerShape(DesignSystem.CornerRadius.small),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray
                    )
                )
                
                // Address Field with Edit Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dock Address", 
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    IconButton(onClick = { navController.navigate("map_picker") }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Address",
                            tint = colorResource(R.color.button_normal)
                        )
                    }
                }
                OutlinedTextField(
                    value = dockAddress,
                    onValueChange = { dockAddress = it },
                    placeholder = { Text("Enter dock address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = DesignSystem.Spacing.minimalSpacing),
                    singleLine = true,
                    shape = RoundedCornerShape(DesignSystem.CornerRadius.small),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray
                    )
                )
                
                // Description Field
                Text(
                    text = "Dock Description",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                OutlinedTextField(
                    value = dockDescription,
                    onValueChange = { dockDescription = it },
                    placeholder = { Text("Enter dock details") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DesignSystem.Sizing.textFieldHeight)
                        .padding(vertical = DesignSystem.Spacing.minimalSpacing),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(DesignSystem.CornerRadius.small),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray
                    )
                )
            }
        }
    }
}

/**
 * Business Actions Section
 * 
 * FULFILLS: LLR-1.7.1 - Save Button
 * FULFILLS: LLR-1.7.2 - Loading State Display
 */
@Composable
private fun BusinessActionsSection(
    state: com.boatit.boatsharing.ui.business.model.BusinessDashboardState,
    viewModel: IBusinessDashboardViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DesignSystem.Elevation.none),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)
        ) {
            Text(
                text = "Actions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            // Save Button with Loading State
            Button(
                onClick = {
                    viewModel.saveBusinessProfile()
                },
                enabled = state.isButtonEnabled && !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.button_normal)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(DesignSystem.Sizing.iconSmall),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(DesignSystem.Spacing.smallSpacing))
                }
                Text(
                    text = if (state.isLoading) "Saving..." else "Save Changes"
                )
            }
            
            // Error Display
            if (state.isError && !state.errorMessage.isNullOrBlank()) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * PermissionsToAccessGalleryMultiple - Enhanced gallery access for multiple image selection
 * 
 * FULFILLS: LLR-2.1.1 - Multiple Image Selection Implementation
 * 
 * This composable provides multiple image selection from gallery using
 * ActivityResultContracts.GetMultipleContents() for enhanced user experience.
 */
@Composable
private fun PermissionsToAccessGalleryMultiple(
    onImagesSelected: (List<Uri>) -> Unit,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    var permissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Multiple images gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            onImagesSelected(uris)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    val permission = if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    // Check permission and launch gallery
    androidx.compose.runtime.LaunchedEffect(permission) {
        val isPermissionGranted = androidx.core.content.ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (!isPermissionGranted) {
            permissionLauncher.launch(permission)
        } else {
            galleryLauncher.launch("image/*")
        }
    }

    // Handle permission granted case
    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            galleryLauncher.launch("image/*")
        }
    }
}

/**
 * AdvancedBusinessHoursModal - Advanced business hours editing with ModalBottomSheet
 * 
 * FULFILLS: LLR-2.2.1 - Modal Bottom Sheet Implementation
 * FULFILLS: LLR-2.2.2 - Time Slot Dropdown Integration
 * 
 * This composable provides comprehensive business hours editing with:
 * - ModalBottomSheet with drag gestures
 * - Time slot dropdowns using AppConstants.hourList
 * - Individual day editing capabilities
 * - Save/Cancel functionality
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdvancedBusinessHoursModal(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSave: (List<BusinessHour>) -> Unit
) {
    var expandedRowIndex by remember { mutableStateOf<Int?>(null) }
    var expandedEndIndex by remember { mutableStateOf<Int?>(null) }
    
    // Default days and editable business hours
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    var editableHours by remember { 
        mutableStateOf(
            daysOfWeek.map { day ->
                BusinessHour(Day = day, StartTime = "09:00", EndTimeTime = "17:00")
            }
        )
    }
    
    val coroutineScope = rememberCoroutineScope()
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = DesignSystem.CornerRadius.modal, topEnd = DesignSystem.CornerRadius.modal),
        containerColor = Color.White,
        tonalElevation = DesignSystem.Elevation.modal,
        modifier = Modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount > DesignSystem.Interaction.dragThreshold) {
                        coroutineScope.launch {
                            sheetState.partialExpand()
                        }
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DesignSystem.Spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.cardPadding)
        ) {
            // Header
            Text(
                text = "Edit Business Hours",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Days editing section
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.sectionSpacing)
            ) {
                items(editableHours.size) { index ->
                    val hour = editableHours[index]
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(DesignSystem.Spacing.smallSpacing),
                        horizontalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.smallSpacing),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Day TextField
                        OutlinedTextField(
                            value = hour.Day.orEmpty(),
                            onValueChange = { },
                            modifier = Modifier.weight(0.5f),
                            label = { Text("Day") },
                            readOnly = true
                        )
                        
                        // Start Time Dropdown - FULFILLS: LLR-2.2.2 - Time Slot Dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = hour.StartTime.orEmpty(),
                                onValueChange = { },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Open") },
                                readOnly = true,
                                textStyle = MaterialTheme.typography.bodySmall,
                                suffix = { 
                                    IconButton(onClick = { expandedRowIndex = if (expandedRowIndex == index) null else index }) {
                                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                                    }
                                }
                            )
                            
                            DropdownMenu(
                                expanded = expandedRowIndex == index,
                                onDismissRequest = { expandedRowIndex = null },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(DesignSystem.Sizing.dropdownHeight)
                                    .background(Color.White)
                            ) {
                                AppConstants.hourList.forEach { time ->
                                    DropdownMenuItem(
                                        onClick = {
                                            editableHours = editableHours.toMutableList().apply {
                                                set(index, hour.copy(StartTime = time))
                                            }
                                            expandedRowIndex = null
                                        },
                                        text = {
                                            Text(
                                                text = time,
                                                modifier = Modifier.padding(vertical = DesignSystem.Spacing.minimalSpacing)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        
                        // End Time Dropdown - FULFILLS: LLR-2.2.2 - Time Slot Dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = hour.EndTimeTime.orEmpty(),
                                onValueChange = { },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Close") },
                                readOnly = true,
                                textStyle = MaterialTheme.typography.bodySmall,
                                suffix = { 
                                    IconButton(onClick = { expandedEndIndex = if (expandedEndIndex == index) null else index }) {
                                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                                    }
                                }
                            )
                            
                            DropdownMenu(
                                expanded = expandedEndIndex == index,
                                onDismissRequest = { expandedEndIndex = null },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(DesignSystem.Sizing.dropdownHeight)
                                    .background(Color.White)
                            ) {
                                AppConstants.hourList.forEach { time ->
                                    DropdownMenuItem(
                                        onClick = {
                                            editableHours = editableHours.toMutableList().apply {
                                                set(index, hour.copy(EndTimeTime = time))
                                            }
                                            expandedEndIndex = null
                                        },
                                        text = {
                                            Text(
                                                text = time,
                                                modifier = Modifier.padding(vertical = DesignSystem.Spacing.minimalSpacing)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DesignSystem.Spacing.smallSpacing)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                
                Button(
                    onClick = { onSave(editableHours) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.button_normal)
                    )
                ) {
                    Text("Save Hours")
                }
            }
        }
    }
}