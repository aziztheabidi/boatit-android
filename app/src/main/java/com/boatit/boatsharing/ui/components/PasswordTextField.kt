@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:no-consecutive-blank-lines",
)

package com.boatit.boatsharing.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    isError: Boolean,
    onClearError: () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester = FocusRequester(),
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .background(Color.Transparent)
            .focusRequester(focusRequester),
        placeholder = { Text(stringResource(R.string.password_placeholder)) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions =
            KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
        keyboardActions = keyboardActions,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            Row(horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        painter =
                            if (passwordVisibility) {
                                painterResource(
                                    R.drawable.visibility_icon,
                                )
                            } else {
                                painterResource(R.drawable.visibility_off_icon)
                            },
                        contentDescription = "Toggle visibility",
                    )
                }
                if (value.isNotEmpty()) {
                    IconButton(onClick = {
                        onValueChange("")
                        onClearError()
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "")
                    }
                }
            }
        },
        isError = isError,
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) Color.Red else Color.Blue,
                unfocusedBorderColor = if (isError) Color.Red else Color.Gray,
                unfocusedTextColor = Color.Gray,
                errorLabelColor = Color.Red,
            ),
    )

    if (isError && !errorMessage.isNullOrEmpty()) {
        Text(
            modifier = Modifier.padding(start = 12.dp),
            style =
                TextStyle(
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                ),
            textAlign = TextAlign.Start,
            text = errorMessage,
        )
    }
}


