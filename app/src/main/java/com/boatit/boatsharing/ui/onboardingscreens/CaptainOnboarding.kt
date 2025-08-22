package com.boatit.boatsharing.ui.onboardingscreens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.navigateWithClearStack
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CaptainOnboarding(navController: NavController, pagerState: PagerState?, scope: CoroutineScope?) {


    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = "Splash Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp) 
        ) {

            Column(

                modifier = Modifier.fillMaxSize(),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(200.dp))

                Image(
                    painter = painterResource(id = R.drawable.voyager_obboarding),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(250.dp)
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    text = stringResource(R.string.captain),
                    modifier = Modifier.padding(10.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    ),
                    text = stringResource(R.string.captain_onboarding_text),
                    modifier = Modifier.padding(10.dp)
                )

                Spacer(modifier = Modifier.weight(1f))


            }


        }

        OnboardingScreenBottomLayout(
            onIconClick = {
                println("move to next screen")
               // navController.navigate(NavigationManager.BUSINESS_ONBOARDING_SCREEN)
                scope?.launch{
                    pagerState?.animateScrollToPage(2)
                }

            },
            onSkipClick = {
                println("skip")
               navController.navigateWithClearStack(NavigationManager.LOGIN_SCREEN,clearStack = true)



            },

            R.drawable.onboarding_step_two,
            modifier = Modifier
                .align(Alignment.BottomCenter)  // Align to the bottom center of the screen
                .padding(bottom = 40.dp, start = 10.dp, end = 10.dp)
        )
    }



}


