package com.fiqhsearcher.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fiqhsearcher.preferences.PreferencesViewModel
import com.fiqhsearcher.screen.home.HomeScreen
import com.fiqhsearcher.screen.login.CreateAccountScreen
import com.fiqhsearcher.screen.login.LoginScreen
import com.fiqhsearcher.screen.settings.SettingsScreen

@Composable
fun SearcherNavigator() {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = "home") {
        composable("home") {
            HomeScreen(navigator = controller)
        }
        composable("settings") {
            SettingsScreen()
        }
        composable("login") {
            LoginScreen(navigator = controller)
        }
        composable("createAccount") {
            CreateAccountScreen(navigator = controller)
        }
    }
}