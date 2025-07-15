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
import androidx.compose.ui.text.style.TextAlign
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
        CaptainMenuItem(R.drawable.profile_menu_icon, "Profile"),
        CaptainMenuItem(R.drawable.logout_menu, "Logout"),
        CaptainMenuItem(R.drawable.upcoming_voyages_menu, "Voyages")
    )

    Box(modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

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

    // Increased distance between the center and menu items
    val adjustedRadius = wheelRadius * 1.45f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .pointerInput(Unit) {
                detectDragGestures { _, delta ->
                    rotationAngle -= delta.x / 2
                }
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.wheel),
            contentDescription = "Wheel",
            modifier = Modifier
                .size(wheelSize)
                .rotate(rotationAngle)
                .align(Alignment.Center)
        )

        Image(
            painter = painterResource(id = R.drawable.wheel_close),
            contentDescription = "close_wheel",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Center)
                .clickable {
                    navController.popBackStack()
                }
        )

        items.forEachIndexed { index, item ->
            val angle = (360f / items.size) * index + rotationAngle
            val radians = Math.toRadians(angle.toDouble())
            val x = cos(radians) * adjustedRadius
            val y = sin(radians) * adjustedRadius

            val isTop = abs(y) > (adjustedRadius * 0.9) && y < 0
            val scale = if (isTop) 1.2f else 1.0f

            Box(
                modifier = Modifier
                    .offset { IntOffset(x.toInt(), y.toInt()) }
                    .scale(scale)
                    .align(Alignment.Center)
                    .clickable {
                        onCaptainItemClick(item, navController, viewModel)
                    }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(if (isTop) 34.dp else 24.dp),
                        tint = Color.Unspecified
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.label,
                        fontSize = if (isTop) 14.sp else 10.sp,
                        fontWeight = if (isTop) FontWeight.Bold else FontWeight.Normal,
                        color = colorResource(R.color.button_normal),
                        textAlign = TextAlign.Center,
                        lineHeight = 10.sp,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .then(
                                if (!isTop) Modifier.widthIn(max = 55.dp) else Modifier.wrapContentWidth()
                            )
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
            navController.navigate(NavigationManager.CAPTAIN_INFO_SCREEN)
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
