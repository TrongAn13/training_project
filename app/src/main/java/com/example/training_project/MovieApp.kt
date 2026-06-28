package com.example.training_project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.training_project.ui.auth.LoginRoute
import com.example.training_project.ui.detail.DetailRoute
import com.example.training_project.ui.favorite.FavoriteRoute
import com.example.training_project.ui.home.HomeRoute
import com.example.training_project.ui.search.SearchRoute
import com.example.uicompose.theme.background_dark
import com.example.uicompose.theme.primary_blue
import com.example.uicompose.theme.text_secondary_gray
import com.example.uicompose.R

sealed class Screen(
    val route: String
) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Favorite : Screen("favorite")
    data object Detail : Screen("detail/{movieId}") {
        fun createRoute(movieId: Long) =
            "detail/$movieId"
    }
}

data class BottomNavItem(
    val screen: Screen,
    val iconRes: Int,
    val labelRes: Int
)

@Composable
fun MovieApp(startDestination: String? = null) {
    val navController = rememberNavController()
    
    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home, R.drawable.ic_home, R.string.home_tab),
        BottomNavItem(Screen.Search, R.drawable.ic_search2, R.string.search_tab),
        BottomNavItem(Screen.Favorite, R.drawable.ic_save, R.string.watchlist_tab)
    )

    Scaffold(
        containerColor = background_dark,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val currentRoute = currentDestination?.route
            
            if (currentRoute in listOf(Screen.Home.route, Screen.Search.route, Screen.Favorite.route)) {
                Column {
                    HorizontalDivider(
                        modifier = Modifier.background(background_dark),
                        thickness = 0.5.dp,
                        color = primary_blue
                    )
                    NavigationBar(
                        containerColor = background_dark
                    ) {
                        bottomNavItems.forEach { item ->
                            val selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
                            NavigationBarItem(
                                icon = { 
                                    Icon(
                                        painter = painterResource(id = item.iconRes), 
                                        contentDescription = stringResource(id = item.labelRes)
                                    ) 
                                },
                                label = { Text(stringResource(id = item.labelRes)) },
                                selected = selected,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = primary_blue,
                                    unselectedIconColor = text_secondary_gray,
                                    selectedTextColor = primary_blue,
                                    unselectedTextColor = text_secondary_gray,
                                    indicatorColor = background_dark
                                ),
                                onClick = {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination!!,
            modifier = Modifier.padding(innerPadding).background(background_dark)
        ) {
            composable(Screen.Login.route ) {
                LoginRoute(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeRoute(
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Detail.createRoute(movieId))
                    },
                    onSearchClick = {
                        navController.navigate(Screen.Search.route)
                    }
                )
            }

            composable(Screen.Search.route) {
                SearchRoute(
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Detail.createRoute(movieId))
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Favorite.route) {
                FavoriteRoute(
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Detail.createRoute(movieId))
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("movieId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getLong("movieId") ?: -1L
                DetailRoute(
                    movieId = movieId,
                    modifier = Modifier,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
