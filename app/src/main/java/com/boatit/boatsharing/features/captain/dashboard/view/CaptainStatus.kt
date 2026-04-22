package com.boatit.boatsharing.features.captain.dashboard.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.navigation.navigateWithClearStack
import com.boatit.boatsharing.features.captain.availabilitystatus.viewmodel.CaptainStatusUiEffect
import com.boatit.boatsharing.features.captain.availabilitystatus.viewmodel.UpdateStatusViewModel
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@Composable
fun CaptainStatus(
    navController: NavController,
    statusText: String,
    viewModelStatus: UpdateStatusViewModel = koinViewModel(),
    userSessionStore: UserSessionStore = get(UserSessionStore::class.java),
) {
    Log.e("statusText", statusText)

    val context = LocalContext.current
    val uiState by viewModelStatus.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModelStatus.uiEffect.collectLatest { effect ->
            when (effect) {
                is CaptainStatusUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                CaptainStatusUiEffect.NavigateToOffline -> {
                    navController.navigateWithClearStack(NavigationManager.CAPTAIN_OFFLINE_SCREEN, clearStack = true)
                }

                CaptainStatusUiEffect.NavigateToDashboard -> Unit
            }
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        // Adjust distance from the top
        contentAlignment = Alignment.TopCenter, // Center horizontally at the top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.wheel_active),
                contentDescription = "wheel icon",
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .alpha(if (uiState.isLoading) 0.6f else 1f)
                        .clickable {
                            if (uiState.isLoading) return@clickable
                            viewModelStatus.setOnlineStatus(false)
                            viewModelStatus.toggleStatus(userSessionStore.currentUserId())
                        },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier =
                    Modifier
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(20.dp),
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp),
            ) {
                Text(
                    text = statusText,
                    color = colorResource(R.color.button_normal),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                )
            }
        }
    }
}
