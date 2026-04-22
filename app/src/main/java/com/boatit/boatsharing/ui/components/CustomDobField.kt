@file:Suppress("ktlint:standard:function-naming")

package com.boatit.boatsharing.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R

@Composable
fun CustomDobField(
    textValue: String,
    placeholderText: String,
    onTextChange: (String) -> Unit,
    textAlign: TextAlign = TextAlign.Start,
    errorMessage: String?,
    isError: Boolean,
    onClearError: () -> Unit,
    showTrailingIcon: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxChars: Int? = null,
    inputType: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester = remember { FocusRequester() },
    singleLine: Boolean = true,
    maxLines: Int? = null,
    minLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    showBorder: Boolean = true,
    showShadow: Boolean = true,
) {
    val localFocusRequester = remember { focusRequester }
    val isFocused = remember { mutableStateOf(false) }

    val fieldModifier =
        Modifier
            .fillMaxWidth()
            .then(
                if (showBorder) {
                    Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                } else {
                    Modifier.shadow(4.dp, shape = RoundedCornerShape(12.dp))
                },
            )
            .background(Color.White, shape = RoundedCornerShape(12.dp))

    OutlinedTextField(
        value = textValue,
        readOnly = true,
        enabled = false,
        onValueChange = {
            if (maxChars == null || it.length <= maxChars) {
                onTextChange(it)
            }
        },
        modifier =
            fieldModifier
                .fillMaxWidth()
                .padding(0.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                .background(Color.Transparent)
                .focusRequester(localFocusRequester)
                .onFocusChanged { focusState ->
                    isFocused.value = focusState.isFocused
                },
        placeholder = { Text(placeholderText) },
        singleLine = singleLine,
        maxLines = maxLines ?: if (singleLine) 1 else Int.MAX_VALUE,
        minLines = minLines,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions =
            KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
        keyboardActions = keyboardActions,
        trailingIcon =
            if (showTrailingIcon && textValue.isNotEmpty()) {
                {
                    IconButton(
                        onClick = {
                            onTextChange("")
                            onClearError()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = if (isFocused.value) Color.Gray else Color.LightGray,
                            contentDescription = "Clear text",
                        )
                    }
                }
            } else {
                null
            },
        leadingIcon = leadingIcon,
        isError = isError,
        visualTransformation = inputType,
        textStyle = TextStyle(color = Color.Black, textAlign = textAlign),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) Color.Red else colorResource(R.color.button_normal),
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
