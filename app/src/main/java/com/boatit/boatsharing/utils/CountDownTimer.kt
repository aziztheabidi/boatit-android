package com.boatit.boatsharing.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun CountDownTimer(
    text:String,
    onResendClick: () -> Unit,
    onStartTimer:() -> Unit,

) {
    var timeLeft by remember { mutableIntStateOf(30) }
    var showResendText by remember { mutableStateOf(false) }


    LaunchedEffect(timeLeft) {
        onStartTimer()
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1
        } else {
            showResendText = true

        }
    }


    val timerText = if (timeLeft > 0) {
        "00:${timeLeft}"
    } else {
        "00:00"
    }

    Spacer(modifier = Modifier.height(15.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
           horizontalArrangement = Arrangement.spacedBy(5.dp),

    ) {


        val annotatedText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp)
            ) {
                append("$timerText ")
            }

            if (showResendText) {

                pushStringAnnotation(tag = "resend", annotation = "resend")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold, fontSize = 16.sp,
                        color = Color.Black
                    )
                ) {
                    append(text)
                }
            }
        }

        Text(
            text = annotatedText,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                .clickable
                {
                    onResendClick()
                    timeLeft = 30
                    showResendText = false
                    println("click")
                }
            ,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            textAlign = TextAlign.Center
        )


    }
}
