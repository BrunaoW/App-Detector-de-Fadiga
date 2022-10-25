package com.wilsoncarolinomalachias.detectordefadiga.presentation.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wilsoncarolinomalachias.detectordefadiga.presentation.Screen

@Composable
fun BottomNav(
    navController: NavController
) {
    val navItems = listOf(
        Screen.StartCourseScreen,
        Screen.UserProfile,
        Screen.CoursesHistory
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = Color.Blue
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach { navItem ->
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = navItem.iconResourceId!!), contentDescription = navItem.route) },
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = false,
                selected = currentRoute == navItem.route,
                onClick = {
                    /*TODO*/
                }
            )
        }
    }
}