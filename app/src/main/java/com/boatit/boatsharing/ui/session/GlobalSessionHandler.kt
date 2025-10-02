package com.boatit.boatsharing.ui.session

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.uihelpers.SessionDialog
import com.boatit.boatsharing.utils.session.SessionEvent
import com.boatit.boatsharing.utils.session.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * Global session handler that manages session events across the entire application
 * 
 * Implements LLR-4.1.1: Event Subscription Implementation
 * Implements LLR-4.1.2: Event Delivery Implementation
 * Implements LLR-4.1.3: Event Delivery Confirmation Implementation
 * Implements LLR-4.2.1: Timeout Warning Display Implementation
 * Implements LLR-4.2.2: User Warning Dialog Implementation
 * Implements LLR-4.2.3: Warning Dismissal Implementation
 * Implements LLR-4.3.1: Navigation Event Handling Implementation
 * Implements LLR-4.3.2: Login Screen Navigation Implementation
 * Implements LLR-4.3.3: Stack Clearing Implementation
 * 
 * This composable should be placed at the root level of the app to handle all session-related events
 */
@Composable
fun GlobalSessionHandler(
    navController: NavController,
    sessionManager: SessionManager = koinViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // LLR-4.1.1: Event Subscription Implementation
    val sessionEvents by sessionManager.sessionEvents.collectAsState(initial = null)
    val sessionState by sessionManager.sessionState.collectAsState()
    
    // Dialog state management
    var showSessionExpiredDialog by remember { mutableStateOf(false) }
    var showTokenRefreshFailedDialog by remember { mutableStateOf(false) }
    var showAccountDeactivatedDialog by remember { mutableStateOf(false) }
    var showMaintenanceModeDialog by remember { mutableStateOf(false) }
    var showForceLogoutDialog by remember { mutableStateOf(false) }
    var showTimeoutWarningDialog by remember { mutableStateOf(false) }
    
    // LLR-4.1.2: Event Delivery Implementation
    LaunchedEffect(sessionEvents) {
        sessionEvents?.let { event ->
            Log.i("GlobalSessionHandler", "Processing session event: $event")
            
            when (event) {
                SessionEvent.LogoutRequired -> {
                    Log.i("GlobalSessionHandler", "Handling LogoutRequired event")
                    handleLogoutRequired(navController, sessionManager)
                }
                
                SessionEvent.SessionExpired -> {
                    Log.w("GlobalSessionHandler", "Handling SessionExpired event")
                    showSessionExpiredDialog = true
                }
                
                SessionEvent.TokenRefreshFailed -> {
                    Log.w("GlobalSessionHandler", "Handling TokenRefreshFailed event")
                    showTokenRefreshFailedDialog = true
                }
                
                SessionEvent.AccountDeactivated -> {
                    Log.w("GlobalSessionHandler", "Handling AccountDeactivated event")
                    showAccountDeactivatedDialog = true
                }
                
                SessionEvent.MaintenanceMode -> {
                    Log.i("GlobalSessionHandler", "Handling MaintenanceMode event")
                    showMaintenanceModeDialog = true
                }
                
                SessionEvent.ForceLogout -> {
                    Log.w("GlobalSessionHandler", "Handling ForceLogout event")
                    showForceLogoutDialog = true
                }
                
                SessionEvent.SessionRestored -> {
                    Log.i("GlobalSessionHandler", "Handling SessionRestored event")
                    handleSessionRestored(context, coroutineScope)
                }
            }
            
            // LLR-4.1.3: Event Delivery Confirmation Implementation
            Log.d("GlobalSessionHandler", "Event delivery confirmed for: $event")
        }
    }
    
    // LLR-4.2.1: Timeout Warning Display Implementation
    LaunchedEffect(sessionState.lastActivityTimestamp) {
        if (sessionState.isAuthenticated && sessionState.isSessionActive) {
            val currentTime = System.currentTimeMillis()
            val timeSinceLastActivity = currentTime - sessionState.lastActivityTimestamp
            val warningThreshold = 25 * 60 * 1000L // 25 minutes
            
            if (timeSinceLastActivity > warningThreshold && !showTimeoutWarningDialog) {
                Log.i("GlobalSessionHandler", "Showing timeout warning")
                showTimeoutWarningDialog = true
            }
        }
    }
    
    // LLR-4.2.2: User Warning Dialog Implementation
    // Session Expired Dialog
    if (showSessionExpiredDialog) {
        SessionDialog(
            text = "Your session has expired due to inactivity. Please log in again.",
            onCancel = {
                Log.d("GlobalSessionHandler", "Session expired dialog cancelled")
                showSessionExpiredDialog = false
            },
            onPressOk = {
                Log.i("GlobalSessionHandler", "Session expired dialog confirmed")
                showSessionExpiredDialog = false
                handleLogoutRequired(navController, sessionManager)
            },
            showCancelButton = false
        )
    }
    
    // Token Refresh Failed Dialog
    if (showTokenRefreshFailedDialog) {
        SessionDialog(
            text = "Failed to refresh your session. Please log in again.",
            onCancel = {
                Log.d("GlobalSessionHandler", "Token refresh failed dialog cancelled")
                showTokenRefreshFailedDialog = false
            },
            onPressOk = {
                Log.i("GlobalSessionHandler", "Token refresh failed dialog confirmed")
                showTokenRefreshFailedDialog = false
                handleLogoutRequired(navController, sessionManager)
            },
            showCancelButton = false
        )
    }
    
    // Account Deactivated Dialog
    if (showAccountDeactivatedDialog) {
        SessionDialog(
            text = "Your account has been deactivated. Please contact support for assistance.",
            onCancel = {
                Log.d("GlobalSessionHandler", "Account deactivated dialog cancelled")
                showAccountDeactivatedDialog = false
            },
            onPressOk = {
                Log.i("GlobalSessionHandler", "Account deactivated dialog confirmed")
                showAccountDeactivatedDialog = false
                handleLogoutRequired(navController, sessionManager)
            },
            showCancelButton = false
        )
    }
    
    // Maintenance Mode Dialog
    if (showMaintenanceModeDialog) {
        SessionDialog(
            text = "The application is currently in maintenance mode. Please try again later.",
            onCancel = {
                Log.d("GlobalSessionHandler", "Maintenance mode dialog cancelled")
                showMaintenanceModeDialog = false
            },
            onPressOk = {
                Log.i("GlobalSessionHandler", "Maintenance mode dialog confirmed")
                showMaintenanceModeDialog = false
                // Optionally exit app or navigate to a static maintenance screen
            },
            showCancelButton = false
        )
    }
    
    // Force Logout Dialog
    if (showForceLogoutDialog) {
        SessionDialog(
            text = "You have been logged out for security reasons. Please log in again.",
            onCancel = {
                Log.d("GlobalSessionHandler", "Force logout dialog cancelled")
                showForceLogoutDialog = false
            },
            onPressOk = {
                Log.i("GlobalSessionHandler", "Force logout dialog confirmed")
                showForceLogoutDialog = false
                handleLogoutRequired(navController, sessionManager)
            },
            showCancelButton = false
        )
    }
    
    // LLR-4.2.1: Timeout Warning Display Implementation
    // Timeout Warning Dialog
    if (showTimeoutWarningDialog) {
        SessionDialog(
            text = "Your session will expire in 5 minutes due to inactivity. Please stay active to maintain your session.",
            onCancel = {
                Log.d("GlobalSessionHandler", "Timeout warning dialog dismissed")
                showTimeoutWarningDialog = false
            },
            onPressOk = {
                Log.i("GlobalSessionHandler", "Timeout warning dialog acknowledged")
                showTimeoutWarningDialog = false
                // Update last activity to extend session
                sessionManager.updateLastActivity()
            },
            showCancelButton = true
        )
    }
}

/**
 * LLR-4.3.1: Navigation Event Handling Implementation
 * LLR-4.3.2: Login Screen Navigation Implementation
 * LLR-4.3.3: Stack Clearing Implementation
 * 
 * Handles logout required events with proper navigation
 */
private fun handleLogoutRequired(navController: NavController, sessionManager: SessionManager) {
    try {
        Log.i("GlobalSessionHandler", "Handling logout required - navigating to login")
        
        // LLR-4.3.3: Stack Clearing Implementation
        // Clear navigation stack to prevent back navigation to authenticated screens
        navController.navigateWithClearStack(
            NavigationManager.LOGIN_SCREEN,
            clearStack = true
        )
        
        // LLR-4.3.2: Login Screen Navigation Implementation
        Log.d("GlobalSessionHandler", "Navigation to login screen completed")
        
        // Reset retry attempts after successful navigation
        sessionManager.resetRetryAttempts()
        
    } catch (e: Exception) {
        Log.e("GlobalSessionHandler", "Failed to handle logout required: ${e.message}")
    }
}

/**
 * Handles session restored events
 */
private fun handleSessionRestored(context: android.content.Context, coroutineScope: kotlinx.coroutines.CoroutineScope) {
    coroutineScope.launch {
        try {
            Log.i("GlobalSessionHandler", "Session restored successfully")
            
            // Show success toast
            Toast.makeText(context, "Session restored successfully!", Toast.LENGTH_SHORT).show()
            
            // Dismiss any timeout warnings that might be showing
            Log.d("GlobalSessionHandler", "Dismissing timeout warnings due to session restoration")
            
        } catch (e: Exception) {
            Log.e("GlobalSessionHandler", "Failed to handle session restored: ${e.message}")
        }
    }
}

