package com.offbyabit.cricnerd

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.offbyabit.cricnerd.screens.*
import com.offbyabit.cricnerd.screens.match_detail_screen.MatchDetailScreen

@Composable
fun Navigation(
    navController: NavHostController
) {
//    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = NavigationRoot.MainScreen.route) {
        composable(
            route = NavigationRoot.MainScreen.route
        ) {
            HomeScreen(navController)
        }
        composable(
            route = NavigationRoot.SettingScreen.route
        ) {
            SettingScreen(navController)
        }
        composable(
            route = NavigationRoot.MatchesScreen.route
        ) {
            MatchesScreen(navController)
        }
        composable(
            route = NavigationRoot.MatchDetailScreen.route,
            arguments = listOf(
                navArgument(MATCH_ID_KEY) {
                    type = NavType.StringType
                }
            )
        ) {
//            Log.d("match-id", it.arguments?.getString(MATCH_ID_KEY).toString())
            val id = (it.arguments?.getString(MATCH_ID_KEY).toString())
            MatchDetailScreen(navController, id)
        }
    }
}