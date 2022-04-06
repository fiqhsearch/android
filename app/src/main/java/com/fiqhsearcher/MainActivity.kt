package com.fiqhsearcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.fiqhsearcher.preferences.PreferencesViewModel
import com.fiqhsearcher.screen.SearcherNavigator
import com.fiqhsearcher.ui.theme.FiqhSearcherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences by viewModels<PreferencesViewModel>()
        setContent {
            val darkTheme by preferences.darkTheme.collectAsState()
            FiqhSearcherTheme(useDarkTheme = darkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SearcherNavigator()
                }
            }
        }
    }
}