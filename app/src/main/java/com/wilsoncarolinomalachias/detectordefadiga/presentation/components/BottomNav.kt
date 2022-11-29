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
    val screens = Screen.getAllScreens()
    val mainNavigationScreenList = listOf(
        Screen.StartCourseScreen,
        Screen.UserProfile,
        Screen.CoursesHistory
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = Color.Blue
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = screens.find { navBackStackEntry?.destination?.route == it.route }

        mainNavigationScreenList.forEach { navItem ->
            val isIconSelected = currentScreen?.iconResourceId == navItem.iconResourceId

            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = navItem.iconResourceId!!), contentDescription = navItem.route) },
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = false,
                selected = isIconSelected,
                onClick = {
                    navController.navigate(navItem.route)
                }
            )
        }
    }
}