package com.boatit.boatsharing.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AlertView(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onCancel: (() -> Unit)? = null,
) {

        val dialogState = remember { mutableStateOf(true) }

        if (dialogState.value) {
            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text(text = title, fontWeight = FontWeight.Bold)
                },
                text = {
                    Text(text = message)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onConfirm()
                            dialogState.value = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            if (onCancel != null) {
                                onCancel()
                            }
                            dialogState.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }


}
