package com.leonardower.mymovie.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leonardower.mymovie.R
import com.leonardower.mymovie.ui.theme.LightGray
import com.leonardower.mymovie.ui.theme.OrangePrimary
import com.leonardower.mymovie.ui.theme.GrayButton as GrayButtonColor

@Composable
fun RatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = GrayButtonColor,
    isRated: Boolean = false,
    rating: Int? = null,
    enabled: Boolean = true
) {
    val buttonText = if (isRated && rating != null) {
        "$rating/10" // Например: "8/10"
    } else {
        stringResource(R.string.rate)
    }

    val iconResId = if (isRated && rating != null) {
        R.drawable.ico__star
    } else {
        R.drawable.ico__star_border
    }

    GrayButton(
        text = buttonText,
        onClick = onClick,
        modifier = modifier,
        backgroundColor = backgroundColor,
        activeBackgroundColor = backgroundColor,
        iconResourceId = iconResId,
        iconTint = if (isRated) OrangePrimary else LightGray,
        textColor = LightGray,
        isActive = isRated,
        enabled = enabled
    )
}

@Preview
@Composable
private fun Preview() {
    Column {
        RatingButton(
            onClick = {},
            isRated = false
        )
        RatingButton(
            onClick = {},
            isRated = true,
            rating = 8
        )
    }
}