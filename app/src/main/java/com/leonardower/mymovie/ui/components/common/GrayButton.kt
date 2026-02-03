package com.leonardower.mymovie.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardower.mymovie.ui.theme.LightGray
import com.leonardower.mymovie.ui.theme.OrangePrimary
import com.leonardower.mymovie.ui.theme.GrayButton as GrayButtonColor

@Composable
fun GrayButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconResourceId: Int? = null,
    iconSize: Dp = 20.dp,
    iconTint: Color = LightGray,
    textColor: Color = Color.White,
    backgroundColor: Color = GrayButtonColor,
    isActive: Boolean = false,
    activeIconTint: Color = OrangePrimary,
    activeBackgroundColor: Color = GrayButtonColor,
    enabled: Boolean = true,
    height: Dp = 42.dp,
    alignTextCenter: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp)
) {
    val currentIconTint = if (isActive && enabled) activeIconTint else iconTint
    val currentBackgroundColor = if (isActive && enabled) activeBackgroundColor else backgroundColor

    Button(
        modifier = modifier
            .height(height),
        contentPadding = contentPadding,
        shape = RectangleShape,
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = currentBackgroundColor,
            contentColor = textColor,
            disabledContainerColor = currentBackgroundColor.copy(alpha = 0.5f),
            disabledContentColor = textColor.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = if (!alignTextCenter) Arrangement.Start else Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка из ресурса или ImageVector
            if (iconResourceId != null) {
                Icon(
                    painter = painterResource(id = iconResourceId),
                    contentDescription = text,
                    modifier = Modifier.size(iconSize),
                    tint = if (enabled) currentIconTint else currentIconTint.copy(alpha = 0.5f)
                )
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.size(iconSize),
                    tint = if (enabled) currentIconTint else currentIconTint.copy(alpha = 0.5f)
                )
            }

            if (iconResourceId != null || icon != null)
                Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = if (enabled) textColor else textColor.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    GrayButton(
        text = "example",
        onClick = {}
    )
}