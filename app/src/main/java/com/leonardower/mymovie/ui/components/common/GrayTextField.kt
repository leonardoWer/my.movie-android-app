package com.leonardower.mymovie.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.ui.theme.GrayButton
import com.leonardower.mymovie.ui.theme.LightGray
import com.leonardower.mymovie.ui.theme.OrangePrimary
import com.leonardower.mymovie.ui.theme.SuccessGreen

@Composable
fun GrayTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    leadingIconState: IconState = IconState.None,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onFocusChange: ((Boolean) -> Unit)? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minHeight: Dp = 42.dp,
    enabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(color = LightGray),
    isError: Boolean = false,
    showSuccessBorder: Boolean = false,
    errorMessage: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isFocused) {
        onFocusChange?.invoke(isFocused)
    }

    // Цвет обводки
    val borderColor = when {
        (isError && isFocused) -> MaterialTheme.colorScheme.error
        (showSuccessBorder && isFocused) -> SuccessGreen
        isFocused -> OrangePrimary
        else -> GrayButton
    }

    // Толщина обводки
    val borderWidth = when {
        isFocused || isError || showSuccessBorder -> 1.dp
        else -> 0.8.dp
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .run {
                    if (singleLine) {
                        this.height(minHeight)
                    } else {
                        this.heightIn(min = minHeight)
                    }
                }
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RectangleShape
                )
                .background(GrayButton)
                .clickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    focusRequester.requestFocus()
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .run {
                        if (singleLine) {
                            this.height(minHeight)
                        } else {
                            this.heightIn(min = minHeight)
                        }
                    }
                    .padding(8.dp),
                verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
            ) {
                // Leading Icon
                if (leadingIcon != null) {
                    if (leadingIconState !is IconState.Invisible) {
                        Box(
                            modifier = Modifier.size(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when (leadingIconState) {
                                IconState.Loading -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = OrangePrimary
                                    )
                                }

                                IconState.Success -> {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Успешно",
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                IconState.Error -> {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Ошибка",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                else -> {
                                    Icon(
                                        imageVector = leadingIcon,
                                        contentDescription = null,
                                        tint = LightGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Текстовое поле
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = if (leadingIcon != null && leadingIconState !is IconState.Invisible) 8.dp else 0.dp,
                            end = if (trailingIcon != null) 8.dp else 0.dp,
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = textStyle.merge(LocalTextStyle.current),
                        value = value,
                        onValueChange = onValueChange,
                        cursorBrush = SolidColor(OrangePrimary),
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        singleLine = singleLine,
                        maxLines = maxLines,
                        enabled = enabled,
                        interactionSource = interactionSource,
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                // Плейсхолдер
                                if (value.isEmpty() && placeholder.isNotEmpty()) {
                                    Text(
                                        text = placeholder,
                                        style = textStyle.copy(color = LightGray),
                                        maxLines = 1
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }

                // Trailing Icon
                if (trailingIcon != null) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        tint = LightGray,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(
                                enabled = onTrailingIconClick != null,
                                onClick = { onTrailingIconClick?.invoke() }
                            )
                    )
                }
            }
        }

        // Сообщение об ошибке
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}


@Preview
@Composable
fun CustomTextFieldPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Пример без иконки
            GrayTextField(
                value = "",
                onValueChange = {},
                placeholder = "Введите текст",
                modifier = Modifier.fillMaxWidth()
            )

            // Пример с leading icon
            GrayTextField(
                value = "Текст",
                onValueChange = {},
                placeholder = "Введите текст",
                leadingIcon = Icons.Default.Email,
                modifier = Modifier.fillMaxWidth()
            )

            // Пример с trailing icon
            GrayTextField(
                value = "Текст",
                onValueChange = {},
                placeholder = "Введите текст",
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = { /* Очистить поле */ },
                modifier = Modifier.fillMaxWidth()
            )

            // Пример с обеими иконками
            GrayTextField(
                value = "",
                onValueChange = {},
                placeholder = "Введите текст",
                leadingIcon = Icons.Default.Search,
                trailingIcon = Icons.Default.Clear,
                modifier = Modifier.fillMaxWidth()
            )

            // Пример в состоянии ошибки
            GrayTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                placeholder = "Обязательное поле",
                isError = true,
                errorMessage = "Ошибка"
            )

            GrayTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = "",
                onValueChange = {},
                placeholder = "Описание",
                leadingIcon = Icons.Default.Search,
                leadingIconState = IconState.Invisible
            )

            GrayTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = "123123",
                onValueChange = {},
                placeholder = "Описание",
                singleLine = false,
                maxLines = 5
            )
        }
    }
}

// Состояния иконки
sealed class IconState {
    data object Invisible : IconState()
    data object None : IconState()
    data object Loading : IconState()
    data object Success : IconState()
    data object Error : IconState()
}