package com.boatit.boatsharing.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.ui.navigation.AccountRoutes
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.components.CustomTopBar
import com.boatit.boatsharing.ui.components.SettingsCard

@Composable
fun SettingsScreen(
    navController: NavController,
    value: String?,
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(
                text = stringResource(R.string.settings_),
                onImageClick = {
                    navController.popBackStack()
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(
                            top = innerPadding.calculateTopPadding() + 15.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = innerPadding.calculateTopPadding() + 25.dp,
                        )
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
            ) {
                Spacer(Modifier.height(30.dp))

                SettingsCard(
                    label = stringResource(R.string.forgot_password),
                    onArrowClick = {
                        navController.navigate(NavigationManager.FORGOT_PASSWORD_SCREEN)
                    },
                )
                Spacer(Modifier.height(10.dp))

                SettingsCard(
                    label = stringResource(R.string.edit_profile),
                    onArrowClick = {
                        if (value.toString() == "captainRole") {
                            navController.navigate(NavigationManager.CAPTAIN_INFO_SCREEN)
                        } else if (value.toString() == "businessRole") {
                            navController.navigate(NavigationManager.BUSINESS_ACCT_INFO_SCREEN)
                        } else {
                            navController.navigate(route = AccountRoutes.userAccountInfo("voyagerRole"))
                        }
                    },
                )
            }
        },
        bottomBar = {
        },
    )
}
