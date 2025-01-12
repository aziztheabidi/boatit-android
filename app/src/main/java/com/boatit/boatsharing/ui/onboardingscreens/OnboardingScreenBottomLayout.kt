package com.boatit.boatsharing.ui.onboardingscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R
@Composable
fun OnboardingScreenBottomLayout(
    onIconClick: () -> Unit,
    onSkipClick: () -> Unit,
    drawableResId: Int,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentHeight(),  // Wrap content height
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Text(
            text = stringResource(R.string.skip),
            modifier = Modifier.clickable {
                onSkipClick()
            },
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        )

        Image(
            painter = painterResource(id = drawableResId),
            contentDescription = "Image",
            modifier = Modifier
                .size(40.dp)
        )


        Image(
            painter = painterResource(id = R.drawable.right_circle),
            contentDescription = "Icon",
            modifier = Modifier
                .size(50.dp)
                .shadow(1.dp, CircleShape)

                .clickable {
                    onIconClick()
                }
        )


    }
}
