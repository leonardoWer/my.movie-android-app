package com.leonardower.mymovie.common.nav.bottom

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leonardower.mymovie.R
import com.leonardower.mymovie.common.nav.Screen

sealed class BottomNavItem (
    val route: String,
    val titleResId: Int,
    val icon: @Composable () -> Unit
) {
    data object Home : BottomNavItem(
        route = Screen.Home.route,
        titleResId = R.string.home,
        icon = {
            Icon(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(0.dp),
                imageVector = Icons.Default.Home,
                contentDescription = stringResource(R.string.home)
            )
        }
    )

    data object WatchThis : BottomNavItem(
        route = Screen.WatchThis.route,
        titleResId = R.string.watch_this,
        icon = {
            Icon(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(0.dp),
                painter = painterResource(R.drawable.ico__watch_this),
                contentDescription = stringResource(R.string.watch_this)
            )
        }
    )

    data object Search : BottomNavItem(
        route = Screen.Search.route,
        titleResId = R.string.search,
        icon = {
            Icon(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(0.dp),
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search)
            )
        }
    )
}