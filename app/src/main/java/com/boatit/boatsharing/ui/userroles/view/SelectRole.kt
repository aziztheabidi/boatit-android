package com.boatit.boatsharing.ui.userroles

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.USER_ACCOUNT_INFO_SCREEN
import com.boatit.boatsharing.ui.userroles.viewmodel.FCMTokenViewModel
import com.boatit.boatsharing.ui.userroles.viewmodel.RoleViewModel
import com.boatit.boatsharing.uihelpers.ClickTopBarIcon
import com.boatit.boatsharing.utils.AppConstants
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectRole(
    navController: NavController,
    viewModel: RoleViewModel = koinViewModel(),
    viewModelFcm: FCMTokenViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val selectedRole by viewModel.selectedRole.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val roleState by viewModel.roleState.collectAsState()

    LaunchedEffect(roleState) {
        if (roleState is NetworkResponse.Success) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    viewModelFcm.fcm(AppConstants.USER_ID.toString(), token)
                }
            }
            when (selectedRole) {
                "Voyager" -> {
                    navController.navigate(route = "$USER_ACCOUNT_INFO_SCREEN/voyagerRole")
                    viewModel.resetNearbyPlaces()
                }
                "Captain" -> {navController.navigate(NavigationManager.CAPTAIN_INFO_SCREEN)
                    viewModel.resetNearbyPlaces()}
                else -> {navController.navigate(NavigationManager.BUSINESS_ACCT_INFO_SCREEN)
                    viewModel.resetNearbyPlaces()}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            ClickTopBarIcon(R.drawable.arrow_back) { navController.popBackStack() }

            Spacer(Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.roles_h1),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.roles_h2),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(30.dp))

            RoleCard(
                imageResId = R.drawable.voyager,
                size = 160.dp,
                firstText = stringResource(R.string.voyager),
                secondText = stringResource(R.string.voyager_role_card_h2),
                onClick = {
                    viewModel.selectRole(AppConstants.USER_ID.toString(), "Voyager")
                }
            )

            Spacer(Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RoleCard(
                    imageResId = R.drawable.captain,
                    size = 100.dp,
                    firstText = stringResource(R.string.captain),
                    secondText = stringResource(R.string.captain_role_card_h2),
                    onClick = {
                        viewModel.selectRole(AppConstants.USER_ID.toString(), "Captain")
                    }
                )
            }

            Spacer(Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RoleCard(
                    imageResId = R.drawable.business,
                    size = 100.dp,
                    firstText = stringResource(R.string.business),
                    secondText = stringResource(R.string.business_role_card_h2),
                    onClick = {
                        viewModel.selectRole(AppConstants.USER_ID.toString(), "Business")
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(16.dp)
//        ) {
//            Button(
//                onClick = { navController.navigate("$DASHBOARD_SCREEN/null") },
//                shape = RoundedCornerShape(10.dp),
//                modifier = Modifier.fillMaxWidth().height(50.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal))
//            ) {
//                Text(
//                    text = stringResource(R.string.guest_button_text),
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    color = Color.White
//                )
//            }
//
//            Spacer(Modifier.height(30.dp))
//        }
    }

    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.
            padding(top = 100.dp)
        )
    }

    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
}


@Preview
@Composable
fun PreviewSelectRoleScreen() {
    SelectRole(navController = rememberNavController())
}