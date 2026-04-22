package com.boatit.boatsharing.features.captain.availabilitystatus

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.features.captain.availabilitystatus.viewmodel.CaptainStatusUiEffect
import com.boatit.boatsharing.features.captain.availabilitystatus.viewmodel.UpdateStatusViewModel
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@Composable
fun CustomStatusScreen(
    navController: NavController,
    viewModel: UpdateStatusViewModel = koinViewModel(),
    userSessionStore: UserSessionStore = get(UserSessionStore::class.java),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is CaptainStatusUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                CaptainStatusUiEffect.NavigateToDashboard -> {
                    navController.navigate(NavigationManager.CAPTAIN_DASHBOARD_SCREEN)
                }

                CaptainStatusUiEffect.NavigateToOffline -> Unit
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.1f),
        )
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = uiState.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.subtitle,
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = uiState.image),
                contentDescription = "wheel icon",
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clickable(enabled = !uiState.isLoading) {
                            viewModel.setOnlineStatus(true)
                            viewModel.toggleStatus(userSessionStore.currentUserId())
                        },
            )
            if (uiState.isLoading) {
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator()
            }
        }
    }
}
