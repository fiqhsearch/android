package com.fiqhsearcher.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fiqhsearcher.screen.home.HomeScreen
import com.fiqhsearcher.screen.login.LoginScreen
import com.fiqhsearcher.screen.search.SearchScreen
import com.fiqhsearcher.screen.settings.SettingsScreen

val queryArg = navArgument("query") { type = NavType.StringType }

@Composable
fun SearcherNavigator() {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = "home") {
        composable("home") {
            HomeScreen(navigator = controller)
        }
        composable("settings") {
            SettingsScreen(navigator = controller)
        }
        composable("search/{query}", listOf(queryArg)) {
            val query = it.arguments?.getString("query") ?: return@composable
            SearchScreen(navigator = controller, query = query)
        }
        composable("login") {
            LoginScreen(navigator = controller)
        }
    }
}