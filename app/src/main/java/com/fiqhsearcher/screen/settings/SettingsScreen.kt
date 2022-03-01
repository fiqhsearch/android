package com.fiqhsearcher.screen.settings

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fiqhsearcher.preferences.PreferencesViewModel

@Composable
fun SettingsScreen(
    preferences: PreferencesViewModel = hiltViewModel(),
    navigator: NavController,
) {

}