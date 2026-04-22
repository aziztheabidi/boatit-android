package com.boatit.boatsharing.ui.screens.menu

import androidx.compose.foundation.Image
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.AccountRoutes
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.navigation.navigateWithClearStack
import com.boatit.boatsharing.features.login.viewmodel.LoginViewModel
import com.boatit.boatsharing.data.local.session.SessionController
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BusinessMenuOptions(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val sessionController: SessionController = get(SessionController::class.java)

    val items =
        listOf(
            // BusinessMenuItem(R.drawable.profile_menu_icon, "Profile"),
            BusinessMenuItem(R.drawable.logout_menu, "Logout"),
            BusinessMenuItem(R.drawable.settings, "Settings"),
        )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        RotatingWheelBusinessMenu(items, navController = navController, viewModel = viewModel, sessionController = sessionController)
    }
}

@Composable
fun RotatingWheelBusinessMenu(
    items: List<BusinessMenuItem>,
    wheelSize: Dp = 280.dp,
    navController: NavController,
    viewModel: LoginViewModel,
    sessionController: SessionController,
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val wheelRadius = with(LocalDensity.current) { wheelSize.toPx() } / 2.3f

    // Increased distance between the center and menu items
    val adjustedRadius = wheelRadius * 1.45f

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .pointerInput(Unit) {
                    detectDragGestures { _, delta ->
                        rotationAngle -= delta.x / 2
                    }
                },
    ) {
        Image(
            painter = painterResource(id = R.drawable.wheel),
            contentDescription = "Wheel",
            modifier =
                Modifier
                    .size(wheelSize)
                    .rotate(rotationAngle)
                    .align(Alignment.Center),
        )

        Image(
            painter = painterResource(id = R.drawable.wheel_close),
            contentDescription = "close_wheel",
            contentScale = ContentScale.FillBounds,
            modifier =
                Modifier
                    .size(70.dp)
                    .align(Alignment.Center)
                    .clickable {
                        navController.popBackStack()
                    },
        )

        items.forEachIndexed { index, item ->
            val angle = (360f / items.size) * index + rotationAngle
            val radians = Math.toRadians(angle.toDouble())
            val x = cos(radians) * adjustedRadius
            val y = sin(radians) * adjustedRadius

            val isTop = abs(y) > (adjustedRadius * 0.9) && y < 0
            val scale = if (isTop) 1.2f else 1.0f

            Box(
                modifier =
                    Modifier
                        .offset { IntOffset(x.toInt(), y.toInt()) }
                        .scale(scale)
                        .align(Alignment.Center)
                        .clickable {
                            onBusinessItemClick(item, navController, viewModel, sessionController)
                        },
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(if (isTop) 34.dp else 24.dp),
                        tint = Color.Unspecified,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.label,
                        fontSize = if (isTop) 14.sp else 10.sp,
                        fontWeight = if (isTop) FontWeight.Bold else FontWeight.Normal,
                        color = colorResource(R.color.button_normal),
                        textAlign = TextAlign.Center,
                        lineHeight = 10.sp,
                        modifier =
                            Modifier
                                .padding(horizontal = 4.dp)
                                .then(
                                    if (!isTop) Modifier.widthIn(max = 55.dp) else Modifier.wrapContentWidth(),
                                ),
                    )
                }
            }
        }
    }
}

fun onBusinessItemClick(
    item: BusinessMenuItem,
    navController: NavController,
    viewModel: LoginViewModel,
    sessionController: SessionController,
) {
    when (item.label) {
        "Profile" -> {
            navController.navigate(NavigationManager.BUSINESS_ACCT_INFO_SCREEN)
        }

        "Logout" -> {
            val loginRoute = sessionController.logoutAndResolveRoute()
            navController.navigateWithClearStack(loginRoute, clearStack = true)
        }
        "Settings" -> {
            navController.navigate(route = AccountRoutes.settings("businessRole"))
        }
    }
}

data class BusinessMenuItem(val icon: Int, val label: String)
