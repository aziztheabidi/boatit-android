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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.di.ApiConstants
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
fun SelectRole(navController: NavController, viewModel: RoleViewModel = koinViewModel(), viewModelFcm: FCMTokenViewModel = koinViewModel()) {

    val context = LocalContext.current
    var SelectedRole by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun performLogin(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                viewModelFcm.fcm(AppConstants.USER_ID.toString(),token)
                println("token:" + token)
            }
        }
        if(SelectedRole.equals("Voyager")){
            navController.navigate(route = "$DASHBOARD_SCREEN/null")
        }else if(SelectedRole.equals("Captain")){
            navController.navigate(NavigationManager.CAPTAIN_INFO_SCREEN)
        }else{
            navController.navigate(NavigationManager.SELECT_ROLE_SCREEN)
        }
    }

    fun selectRole(role: String){
        isLoading = true
        isNetworkError = true
        viewModel.role(AppConstants.USER_ID.toString(), role)
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

    Box(modifier = Modifier.fillMaxSize().background(color = Color.White)) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 120.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().
                padding(start = 20.dp, end = 20.dp, top = 40.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,

                ) {

                ClickTopBarIcon(
                    imageResId = R.drawable.arrow_back,
                    onClick = {
                        println("back icon clicked")
                    }
                )

                Spacer(Modifier.height(30.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    text = stringResource(R.string.roles_h1),

                    )
                Spacer(Modifier.height(30.dp))

                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,

                        ),
                    text = stringResource(R.string.roles_h2),

                    )

                Spacer(Modifier.height(30.dp))

                RoleCard(imageResId = R.drawable.voyager, size = 160.dp,
                    firstText = stringResource(R.string.voyager),
                    secondText = stringResource(R.string.voyager_role_card_h2),
                    onClick = {
                        println("Card clicked!")
                        SelectedRole = "Voyager"
                        selectRole("Voyager")
                    })
                Spacer(Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth().wrapContentHeight()

                    ) {
                        RoleCard(imageResId = R.drawable.captain, size = 100.dp,
                            firstText = stringResource(R.string.captain),
                            secondText = stringResource(R.string.captain_role_card_h2),
                            onClick = {
                                println("Card clicked!")
                                SelectedRole = "Captain"
                                selectRole("Captain")
                            })
                    }

                    Spacer(Modifier.width(15.dp))


                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth().wrapContentHeight()

                    ) {

                        RoleCard(imageResId = R.drawable.business, size = 100.dp,
                            firstText = stringResource(R.string.business),
                            secondText = stringResource(R.string.business_role_card_h2),
                            onClick = {
                                println("Card clicked!")
                                SelectedRole = "Business"
                                selectRole("Business")
                            })
                    }
                }

                Box(modifier = Modifier.fillMaxWidth()
                    .weight(1f)){}

            }
        }



        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(100.dp)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter,

            ) {

            Column(

                modifier = Modifier.fillMaxWidth(),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,

                ) {
            Button(
                onClick = {
                    navController.navigate(route = "$DASHBOARD_SCREEN/null")
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth().height(50.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal) )
            ) {
                Text(
                    text = stringResource(R.string.guest_button_text),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
           Spacer(Modifier.height(30.dp))
        }
}
    }
}

@Preview
@Composable
fun PreviewBusinessOnboardingOnboardingScreen() {
    SelectRole(navController = rememberNavController())
}