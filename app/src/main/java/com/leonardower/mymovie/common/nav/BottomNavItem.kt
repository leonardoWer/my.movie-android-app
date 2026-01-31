package com.leonardower.mymovie.common.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.leonardower.mymovie.R

sealed class BottomNavItem (
    val route: String,
    val titleResId: Int,
    val icon: @Composable () -> Unit
) {
    data object Home : BottomNavItem(
        route = "home",
        titleResId = R.string.home,
        icon = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = stringResource(R.string.home)
            )
        }
    )

    data object Search : BottomNavItem(
        route = "search",
        titleResId = R.string.search,
        icon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search)
            )
        }
    )
}