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
import com.boatit.boatsharing.ui.navigation.navigateWithClearStack
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.CompleteVoyageUiEffect
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.CompleteVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.VoyageSessionStore
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@Composable
fun VoyageStartedScreen(
    navController: NavController,
    viewModel: CompleteVoyageViewModel = koinViewModel(),
    voyageSessionStore: VoyageSessionStore = get(VoyageSessionStore::class.java),
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("Your voyage has been started") }
    val uiState by viewModel.uiState.collectAsState()
    val activeVoyageId by voyageSessionStore.voyageId.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is CompleteVoyageUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                CompleteVoyageUiEffect.NavigateToFeedback -> {
                    voyageSessionStore.clear()
                    navController.navigateWithClearStack(NavigationManager.CAPTAIN_DASHBOARD_SCREEN, clearStack = true)
                }
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
            modifier = Modifier.fillMaxSize(),
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
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap on wheel to complete your voyage",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(R.drawable.wheel_icon),
                contentDescription = "wheel icon",
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .width(100.dp)
                        .height(100.dp).clickable {
                            if (activeVoyageId.isNotBlank()) {
                                viewModel.completeVoyage(
                                    VoyageCompleteRequest(activeVoyageId),
                                )
                            }
                        }
                        .graphicsLayer { alpha = if (uiState.isLoading) 0.6f else 1f },
            )
        }
    }
}
