package com.boatit.boatsharing.ui.onboardingscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.NavigationManager


@Composable
fun BusinessOnboarding(navController: NavController) {


    Box(modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 150.dp) // Add padding from the bottom
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
                    text = stringResource(R.string.business),
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
                    text = stringResource(R.string.business_onboarding_text),
                    modifier = Modifier.padding(10.dp)
                )




                Spacer(modifier = Modifier.weight(1f))


            }
        }


        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter,

        ) {
            Column(
                modifier = Modifier.wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {


                Button(
                    onClick = {


                         navController.navigate(NavigationManager.CREATE_ACCOUNT_STEP_ONE_SCREEN)


                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth().height(50.dp)
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = stringResource(R.string.create_account),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
                val annotatedText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Normal, fontSize = 14.sp)) {
                        append(stringResource(R.string.already_registered))
                    }

                    pushStringAnnotation(tag = "LogIn", annotation = "LogIn")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,fontSize = 16.sp,
                        color = Color.White)) {
                        append(stringResource(R.string.login))
                    }
                    pop()
                }

                Text(
                    text = annotatedText,
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp).clickable {
                        navController.navigate(NavigationManager.LOGIN_SCREEN)
                    },


                )

                Spacer(Modifier.height(30.dp))
            }
        }


    }

}


@Preview
@Composable
fun PreviewBusinessOnboardingOnboardingScreen() {
    BusinessOnboarding(navController = rememberNavController())
}

