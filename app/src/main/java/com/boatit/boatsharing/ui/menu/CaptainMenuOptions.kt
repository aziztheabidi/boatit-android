package com.boatit.boatsharing.ui.menu


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.USER_ACCOUNT_INFO_SCREEN
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.login.viewmodel.LoginViewModel
import com.boatit.boatsharing.ui.onboardingscreens.BusinessOnboarding
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import org.koin.androidx.compose.koinViewModel
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CaptainMenuOptions(navController: NavController, viewModel: LoginViewModel = koinViewModel()) {

    val items = listOf(
        CaptainMenuItem(R.drawable.current_marker, ""),
        CaptainMenuItem(R.drawable.current_marker, "Profile"),
        CaptainMenuItem(R.drawable.current_marker, "History"),
        CaptainMenuItem(R.drawable.current_marker, "Role"),
        CaptainMenuItem(R.drawable.current_marker, "Logout"),
        CaptainMenuItem(R.drawable.current_marker, "Voyages")
    )

    Box(modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize() .graphicsLayer(alpha = 0.1f)

        )
        RotatingWheelCaptainMenu(items, navController =  navController, viewModel = viewModel)
    }
}

@Composable
fun RotatingWheelCaptainMenu(
    items: List<CaptainMenuItem>,
    wheelSize: Dp = 280.dp,
    navController: NavController,
    viewModel: LoginViewModel
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val wheelRadius = with(LocalDensity.current) { wheelSize.toPx() } / 2.3f

    // Increase the distance of menu items from the center of the wheel by adding extra padding
    val adjustedRadius = wheelRadius * 1.3f  // Increase this multiplier to control the spacing

    Box(
        modifier = Modifier
            .fillMaxSize() // Fill the entire screen to allow centering
            .wrapContentSize(Alignment.Center) // Center the Box in the parent container
            .pointerInput(Unit) {
                detectDragGestures { _, delta ->
                    rotationAngle += delta.x / 2  // Adjust rotation sensitivity
                }
            }
    ) {
        // **Rotating Wheel Image**
        Image(
            painter = painterResource(id = R.drawable.wheel),
            contentDescription = "Wheel",
            modifier = Modifier
                .size(wheelSize) // Set size for the wheel
                .rotate(rotationAngle) // The wheel rotates based on the rotation angle
                .align(Alignment.Center) // Ensure it's centered in the Box
        )

        Image(
            painter = painterResource(id = R.drawable.wheel_close),
            contentDescription = "close_wheel",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Center).
                clickable {
                    navController.popBack()
                }
        )

        items.forEachIndexed { index, item ->
            val angle = (360f / items.size) * index + rotationAngle
            val radians = Math.toRadians(angle.toDouble())
            val x = cos(radians) * adjustedRadius
            val y = sin(radians) * adjustedRadius

            // Apply zoom effect to the item closest to the center (top position)
            val isTop = abs(y) > (adjustedRadius * 0.9) && y < 0

            val scale = when {
                isTop -> 1.2f  // Top item zoom
              //  isBottom -> 1.0f // Bottom item zoom
                else -> 1.0f // Rest of the items stay at normal size
            }

            Box(
                modifier = Modifier
                    .offset { IntOffset(x.toInt(), y.toInt()) } // Position items around the wheel
                    .scale(scale) // Apply zoom effect
                    .align(Alignment.Center)
                    .clickable {

                        onCaptainItemClick(item,navController,viewModel)
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Adjusted icon size
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(if (isTop) 34.dp else 24.dp),
                        tint = Color.Unspecified
                    )
                    // Adjusted text size
                    Text(
                        text = item.label,
                        fontSize = if (isTop ) 14.sp else 10.sp, // Adjust text size
                        fontWeight = if (isTop ) FontWeight.Bold else FontWeight.Normal,
                        color = colorResource(R.color.button_normal)
                    )
                }
            }
        }
    }
}

fun onCaptainItemClick(item: CaptainMenuItem, navController: NavController, viewModel: LoginViewModel) {
    when (item.label) {
        "Sponsors" -> {}
        "Profile" -> {
            navController.navigate(route = "$USER_ACCOUNT_INFO_SCREEN/voyagerRole")
        }
        "Past Voyages" -> {}
        "Chat Screen" -> {}
        "Logout" -> {
            viewModel.clearUserData()
            navController.navigateWithClearStack(NavigationManager.LOGIN_SCREEN, clearStack = true)
        }
        "Voyages" -> {
            navController.navigate(NavigationManager.CAPTAIN_CURRENT_VOYAGES_SCREEN)
        }
    }
}


data class CaptainMenuItem(val icon: Int, val label: String)
