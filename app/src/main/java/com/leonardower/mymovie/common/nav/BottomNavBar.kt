package com.leonardower.mymovie.common.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leonardower.mymovie.ui.theme.DarkBg
import com.leonardower.mymovie.ui.theme.GrayBg
import com.leonardower.mymovie.ui.theme.LightGray

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search
    )

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        containerColor = GrayBg,
        tonalElevation = 2.dp,
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
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
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = LightGray,
                    unselectedTextColor = LightGray,
                    indicatorColor = Color.Transparent
                ),
                alwaysShowLabel = false,
            )
        }
    }
}