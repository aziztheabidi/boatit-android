package com.boatit.boatsharing.uihelpers


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.onboardingscreens.CaptainOnboarding
import com.boatit.boatsharing.ui.onboardingscreens.VoyagerOnboarding
import com.boatit.boatsharing.ui.onboardingscreens.BusinessOnboarding
import com.boatit.boatsharing.utils.HandleSystemDefaultBars
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPager(navController: NavController) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    HandleSystemDefaultBars(
        statusBarColor = colorResource(R.color.bars_colour),
        navigationBarColor = colorResource(R.color.bars_colour),
    )


    HorizontalPager(
        count = 3,
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> VoyagerOnboarding(
                navController,
                pagerState = pagerState,
                scope = scope
            )
            1 -> CaptainOnboarding(
                navController,
                pagerState = pagerState,
                scope = scope
            )
            2 -> BusinessOnboarding(navController)
        }
    }

}
