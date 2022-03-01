package com.fiqhsearcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.fiqhsearcher.prefs.PrefsViewModel
import com.fiqhsearcher.screen.home.Home
import com.fiqhsearcher.ui.theme.FiqhSearcherTheme

class MainActivity : ComponentActivity() {

    @Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = PrefsViewModel(this)
        setContent {
            val darkTheme by preferences.darkTheme.collectAsState()
            val madhab by preferences.madhab.collectAsState()
            FiqhSearcherTheme(useDarkTheme = darkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Home(
                        darkTheme = darkTheme,
                        madhab = madhab
                    )
                }
            }
        }
    }
}
