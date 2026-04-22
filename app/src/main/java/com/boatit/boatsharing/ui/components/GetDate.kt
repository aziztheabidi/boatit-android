package com.boatit.boatsharing.ui.components

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun getDate(): String {
    return remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd, MMMM, yyyy")
            currentDate.format(formatter)
        } else {
            // For devices with Android < API 26
            val currentDate = Date()
            val formatter = SimpleDateFormat("dd, MMMM, yyyy", Locale.getDefault())
            formatter.format(currentDate)
        }
    }
}
