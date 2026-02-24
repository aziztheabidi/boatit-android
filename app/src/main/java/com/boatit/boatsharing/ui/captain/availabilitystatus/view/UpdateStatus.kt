package com.boatit.boatsharing.ui.captain.availabilitystatus

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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.ui.captain.availabilitystatus.viewmodel.UpdateStatusViewModel
import com.boatit.boatsharing.ui.userroles.viewmodel.RoleViewModel
import com.boatit.boatsharing.utils.AppConstants
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun CustomStatusScreen(navController: NavController,  viewModel: UpdateStatusViewModel = koinViewModel()) {

    var title by remember { mutableStateOf("Welcome!") }
    var subtitle by remember { mutableStateOf("Tap the wheel to go online and start getting voyage requests") }
    var image by remember { mutableIntStateOf(R.drawable.wheel_inactive) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun performLogin(){
        navController.navigate(NavigationManager.CAPTAIN_DASHBOARD_SCREEN)
    }

    when (loginState) {
        is NetworkResponse.Success -> {
            if(isLoading){
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, loginState.data?.Message, Toast.LENGTH_SHORT).show()
                performLogin()
            }
        }
        is NetworkResponse.Error -> {
            if(isLoading){
                isLoading = false
                isNetworkError = true
                errorMessage = "Network error, please try again."
                Toast.makeText(context, (loginState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    Box(modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.1f)

        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = subtitle,
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = image),
                contentDescription = "wheel icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clickable {
                        title = "you are Online!"
                        subtitle = "Start accepting  voyager and help voyagers reach there destinations."
                        image = R.drawable.wheel_icon
                        isButtonEnabled = true
                        isLoading = true
                        isNetworkError = true
                        viewModel.status(AppConstants.USER_ID.toString(), true)
                }
            )
        }
    }
}

