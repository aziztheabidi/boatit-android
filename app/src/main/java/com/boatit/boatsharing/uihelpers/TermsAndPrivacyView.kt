package com.boatit.boatsharing.uihelpers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TermsAndPrivacyView(onClick: () -> Unit) {


    Box(
        modifier = Modifier
            .fillMaxWidth().
            height(80.dp),
        contentAlignment = Alignment.TopCenter
    ) {


        val annotatedText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp)
            ) {
                append("By using Boatit, you agree to the\n")
            }

            pushStringAnnotation(tag = "terms", annotation = "terms")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,fontSize = 16.sp,
                color = Color.Black)
            ) {
                append("Terms")
            }

            withStyle(style = SpanStyle(color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp)
            ) {
                append(" and ")
            }

            pushStringAnnotation(tag = "terms", annotation = "terms")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,fontSize = 16.sp,
                color = Color.Black)
            ) {
                append("Privacy Policy")
            }
            pop()
        }

        Text(
            text = annotatedText,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                            .clickable
                        {
                            onClick()
                            
                        },
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ), textAlign = TextAlign.Center
        )


    }
}