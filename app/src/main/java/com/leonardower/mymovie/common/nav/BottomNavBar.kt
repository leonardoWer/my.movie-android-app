package com.leonardower.mymovie.common.nav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leonardower.mymovie.ui.theme.DarkBg
import com.leonardower.mymovie.ui.theme.GrayBg
import com.leonardower.mymovie.ui.theme.LightGray

@Composable
fun BottomNavigationBar(
    navController: NavController?,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            color = GrayBg,
            thickness = 0.8.dp,
            modifier = Modifier.fillMaxWidth()
        )

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(0.dp),
            containerColor = DarkBg,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            val navBackStackEntry = navController?.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.value?.destination?.route

            items.forEach { item ->
                val isSelected = currentRoute == item.route

                NavigationBarItem(
                    modifier = Modifier
                        .height(56.dp)
                        .padding(0.dp),
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController?.navigate(item.route) {
                                // Очищаем back stack до корня
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Избегаем дублирования экранов
                                launchSingleTop = true
                                // Восстанавливаем состояние
                                restoreState = true
                            }
                        }
                    },
                    icon = item.icon,
                    label = {
                        Text(
                            text = stringResource(item.titleResId),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .graphicsLayer { translationY = -20f },
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = LightGray,
                        unselectedTextColor = LightGray,
                        indicatorColor = Color.Transparent
                    ),
                )
            }
        }
    }
}


@Preview
@Composable
private fun BottomNavigationBarPreview() {
    BottomNavigationBar(null)
}