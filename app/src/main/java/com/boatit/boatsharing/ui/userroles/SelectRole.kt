package com.boatit.boatsharing.ui.userroles

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.USER_ACCOUNT_INFO_SCREEN
import com.boatit.boatsharing.uihelpers.ClickTopBarIcon

@Composable
fun SelectRole(navController: NavController) {


    Box(modifier = Modifier.fillMaxSize().background(color = Color.White)
    ) {

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

                        navController.navigate(route = "$USER_ACCOUNT_INFO_SCREEN/voyagerRole")




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

                                navController.navigate(route = "$USER_ACCOUNT_INFO_SCREEN/captainRole")


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



                                navController.navigate(route = "$USER_ACCOUNT_INFO_SCREEN/businessRole")




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
                    navController.navigate(NavigationManager.DASHBOARD_SCREEN)
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