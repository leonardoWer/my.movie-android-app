package com.leonardower.mymovie.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leonardower.mymovie.R
import com.leonardower.mymovie.ui.theme.LightGray
import com.leonardower.mymovie.ui.theme.OrangePrimary

@Composable
fun RatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isRated: Boolean = false,
    rating: Int? = null,
    enabled: Boolean = true
) {
    val buttonText = if (isRated && rating != null) {
        "$rating/10" // Например: "8/10"
    } else {
        stringResource(R.string.rate)
    }

    val icon = if (isRated && rating != null) {
        Icons.Filled.Star
    } else {
        Icons.Sharp.Star
    }

    GrayButton(
        text = buttonText,
        onClick = onClick,
        modifier = modifier,
        icon = icon,
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