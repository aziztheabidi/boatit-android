@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:no-consecutive-blank-lines",
)

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
fun CustomTextField(
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
    isEditable: Boolean = true,
) {
    val localFocusRequester = remember { focusRequester }
    val isFocused = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            if (isEditable && (maxChars == null || it.length <= maxChars)) {
                onTextChange(it)
            }
        },
        enabled = isEditable,
        readOnly = !isEditable,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                .background(Color.Transparent)
                .focusRequester(localFocusRequester)
                .onFocusChanged { focusState -> isFocused.value = focusState.isFocused },
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
            if (showTrailingIcon && isEditable && textValue.isNotEmpty()) {
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

@Composable
fun ComposableUtilsTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    keyboardOptions: KeyboardOptions,
    isError: Boolean? = false,
    errorMessage: String,
) {
    val hasError = isError == true

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(10.dp),
        colors =
            CardDefaults.cardColors(
                Color(0xFFF1F1F1),
            ),
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = if (hasError) Color.Red else Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            placeholder = {
                Text(text = if (hasError) errorMessage else hint)
            },
            visualTransformation = VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            isError = hasError,
        )
    }
}


