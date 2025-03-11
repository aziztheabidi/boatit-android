package com.boatit.boatsharing.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.CHAT_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.ui.login.viewmodel.LoginViewModel
import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.HandleSystemDefaultBars
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@Composable
fun SplashComposable(navController: NavController, viewModel: LoginViewModel = koinViewModel()) {

    HandleSystemDefaultBars(
        statusBarColor = colorResource(R.color.bars_colour),
        navigationBarColor = colorResource(R.color.bars_colour),
    )

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = "Splash Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painter = painterResource(id = R.drawable.boatit_logo),
            contentDescription = "Logo of the app",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center)
        )
    }

    val userData = viewModel.getUserData()
    AppConstants.USER_ID = userData?.UserId
    println("userid" + AppConstants.USER_ID)
    LaunchedEffect(Unit) {
        delay(5000)
        if (userData != null) {
            if(userData.Role.equals("Voyager")){
                navController.navigate(route = "$DASHBOARD_SCREEN/null")
            }else if(userData.Role.equals("Captain")){
                navController.navigate(NavigationManager.CAPTAIN_OFFLINE_SCREEN)
//                navController.navigate(NavigationManager.CAPTAIN_VOYAGES_SCREEN)
            }else{
                navController.navigate(NavigationManager.SELECT_ROLE_SCREEN)
            }
        } else {
            navController.navigate(NavigationManager.VOYAGER_ONBOARDING_SCREEN)
        }
    }
}


@Preview
@Composable
fun PreviewSplashScreen() {
    SplashComposable(navController = rememberNavController())
}