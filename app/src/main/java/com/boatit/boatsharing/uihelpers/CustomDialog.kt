package com.boatit.boatsharing.uihelpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.boatit.boatsharing.R
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun CustomDialog(onDismiss: () -> Unit,value: String) {
    val delayDuration: Long = 3000L
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (value == "find_boat") {
                BookingConfirmationOverlay()
                LaunchedEffect(Unit) {
                    delay(delayDuration)
                    onDismiss()

                }
            }

            else {
                PaymentSuccessfulScreen("your registered email.")
                LaunchedEffect(Unit) {
                    delay(delayDuration)
                    onDismiss()
                }
            }
        }
    }
}

@Composable
fun BookingConfirmationOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White).alpha(0.65f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Loading Spinner
            CircularProgressIndicator(
                color = Color.Black,
                strokeWidth = 5.dp,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bold Confirmation Text
            Text(
                text = "We’re confirming your booking...",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Instruction Text
            Text(
                text = "Please do not close this app or use the back button.",
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun PaymentSuccessfulScreen(email: String) {
    Column(
        modifier = Modifier
            .fillMaxSize().background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Payment Successful",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,

        )

        Spacer(modifier = Modifier.height(24.dp))



        Image(
            painter = painterResource(id = R.drawable.payment_done),
            contentDescription = "response icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )


        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = buildAnnotatedString {
                append("We have sent an email to ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(email)
                }
               // append(" with receipt of this Voyage.")
            },
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(onDateSelected: (String) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDateMillis = datePickerState.selectedDateMillis
    val selectedDate = selectedDateMillis?.let {
        val calendar = Calendar.getInstance().apply { timeInMillis = it }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Months are 0-based, so add 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        String.format("%04d-%02d-%02d", year, day, month)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate.toString())
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

