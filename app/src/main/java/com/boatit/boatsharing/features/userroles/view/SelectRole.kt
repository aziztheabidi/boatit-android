@file:Suppress("ktlint:standard:function-naming")

package com.boatit.boatsharing.features.userroles

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.navigation.navigateWithClearStack
import com.boatit.boatsharing.features.userroles.viewmodel.FCMTokenViewModel
import com.boatit.boatsharing.features.userroles.viewmodel.RoleViewModel
import com.boatit.boatsharing.ui.components.ClickTopBarIcon
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.boatit.boatsharing.data.local.session.SessionController
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@Composable
fun SelectRole(
    navController: NavController,
    viewModel: RoleViewModel = koinViewModel(),
    viewModelFcm: FCMTokenViewModel = koinViewModel(),
    sessionController: SessionController = get(SessionController::class.java),
    userSessionStore: UserSessionStore = get(UserSessionStore::class.java),
) {
    val context = LocalContext.current
    val selectedRole by viewModel.selectedRole.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val roleState by viewModel.roleState.collectAsState()

    LaunchedEffect(roleState) {
        if (roleState is NetworkResponse.Success) {
            // Send FCM token
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    val userId = userSessionStore.currentUserId()
                    if (userId.isNotBlank()) {
                        viewModelFcm.updateFcmToken(userId, token)
                    }
                }
            }

            val loginRoute = sessionController.logoutAndResolveRoute()
            navController.navigateWithClearStack(loginRoute, clearStack = true)

            // Reset any state if needed
            viewModel.resetRoleState()
        }
    }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            ClickTopBarIcon(R.drawable.arrow_back) {
                navController.popBackStack()
            }

            Spacer(Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.roles_h1),
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.roles_h2),
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(Modifier.height(30.dp))

            RoleCard(
                imageResId = R.drawable.voyager,
                size = 150.dp,
                firstText = stringResource(R.string.voyager),
                secondText = stringResource(R.string.voyager_role_card_h2),
                onClick = {
                    val userId = userSessionStore.currentUserId()
                    if (userId.isNotBlank()) {
                        viewModel.selectRole(userId, "Voyager")
                    }
                },
            )

            Spacer(Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .weight(1f),
                ) {
                    RoleCard(
                        imageResId = R.drawable.captain,
                        size = 100.dp,
                        firstText = stringResource(R.string.captain),
                        secondText = stringResource(R.string.captain_role_card_h2),
                        onClick = {
                            val userId = userSessionStore.currentUserId()
                            if (userId.isNotBlank()) {
                                viewModel.selectRole(userId, "Captain")
                            }
                        },
                    )
                }

                Box(
                    modifier =
                        Modifier
                            .weight(1f),
                ) {
                    RoleCard(
                        imageResId = R.drawable.business,
                        size = 100.dp,
                        firstText = stringResource(R.string.business),
                        secondText = stringResource(R.string.business_role_card_h2),
                        onClick = {
                            val userId = userSessionStore.currentUserId()
                            if (userId.isNotBlank()) {
                                viewModel.selectRole(userId, "Business")
                            }
                        },
                    )
                }
            }

            //  Spacer(modifier = Modifier.weight(1f))
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Preview
@Composable
fun PreviewSelectRoleScreen() {
    SelectRole(navController = rememberNavController())
}
