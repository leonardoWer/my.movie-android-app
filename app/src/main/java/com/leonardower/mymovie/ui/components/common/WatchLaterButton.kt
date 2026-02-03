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

@Composable
fun WatchLaterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    isInWatchLater: Boolean = false,
    enabled: Boolean = true
) {
    val iconResId = if (isInWatchLater) {
        R.drawable.ico__watch_later
    } else {
        R.drawable.ico__not_watch_later
    }

    GrayButton(
        text = stringResource(R.string.watch_later),
        onClick = onClick,
        modifier = modifier,
        backgroundColor = backgroundColor,
        activeBackgroundColor = backgroundColor,
        iconResourceId = iconResId,
        iconTint = if (isInWatchLater) OrangePrimary else LightGray,
        textColor = LightGray,
        isActive = isInWatchLater,
        enabled = enabled
    )
}

@Preview
@Composable
private fun Preview() {
    Column {
        WatchLaterButton(
            onClick = {},
            isInWatchLater = false
        )
        WatchLaterButton(
            onClick = {},
            isInWatchLater = true
        )
    }
}