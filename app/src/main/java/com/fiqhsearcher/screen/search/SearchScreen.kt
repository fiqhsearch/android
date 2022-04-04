package com.fiqhsearcher.screen.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.fiqh.Fiqh
import com.fiqhsearcher.fiqh.Madhab
import com.fiqhsearcher.screen.search.State.*

@Composable
fun SearchScreen(
    navigator: NavController,
    query: String,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val results by searchViewModel
        .search(LocalContext.current, query, Madhab.HANBALI, Fiqh.IBADAT)
        .collectAsState(initial = State.loading())
    when (results) {
        is Loading -> {
            Text("")
        }
        is Success -> {
            Text("")
        }
        is Failed -> {
            Text("")
        }
    }
}